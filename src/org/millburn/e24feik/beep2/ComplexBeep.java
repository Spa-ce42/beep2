package org.millburn.e24feik.beep2;

import org.millburn.e24feik.beep2.core.ApplicationLogic;
import org.millburn.e24feik.beep2.core.Window;
import org.millburn.e24feik.beep2.scene.Entity;
import org.millburn.e24feik.beep2.event.MouseInput;
import org.millburn.e24feik.beep2.scene.Camera;
import org.millburn.e24feik.beep2.render.Renderer;
import org.millburn.e24feik.beep2.scene.Scene;
import org.millburn.e24feik.beep2.util.ColoredPoint;
import org.millburn.e24feik.beep2.util.Point;
import org.millburn.e24feik.beep2.render.GeometryRenderer;
import org.millburn.e24feik.beep2.util.GeometryEntityBuilder;
import org.millburn.e24feik.beep2.util.LineEntityBuilder;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43.glDebugMessageCallback;

public class ComplexBeep implements ApplicationLogic {
    private static final Vector3f UP = new Vector3f(0, 1, 0);
    private float scale = 1;
    private int width;
    private int height;
    private int length;
    private Vector2f lastMousePos;
    private Scene scene;

    private final List<Point> bees = new ArrayList<>();
    private final List<Point> exits = new ArrayList<>();
    private final List<Point> obstacles = new ArrayList<>();
    private final TemporaryPoints temporaryPoints = new TemporaryPoints();

    private Entity lastTempEntity;

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void addBee(Point bee) {
        this.bees.add(bee);
    }

    public void removeBee(Point point) {
        this.bees.remove(point);
    }

    public void addExit(Point exit) {
        this.exits.add(exit);
    }

    public void removeExit(Point point) {
        this.exits.remove(point);
    }

    public void addObstacle(Point point) {
        this.obstacles.add(point);
    }

    public void removeObstacle(Point point) {
        this.obstacles.remove(point);
    }

    private Entity drawObstaclesFromPointList(Scene scene, List<Point> points, float scale, float red, float green, float blue, float alpha) {
        LineEntityBuilder leb = new LineEntityBuilder();

        for(Point point : points) {
            float nx = point.x * scale;
            float ny = point.y * scale;
            float nz = point.z * scale;

            leb.addCubeVerticesTwoPoints(
                    nx, ny, nz,
                    nx + scale, ny + scale, nz + scale,
                    red, green, blue, alpha
            );
        }

        return leb.build(scene);
    }

    private Entity drawCubesFromPointList(Scene scene, List<Point> points, float scale, float red, float green, float blue, float alpha, boolean alertAboutObstacles) {
        GeometryEntityBuilder geb = new GeometryEntityBuilder();

        for(Point point : points) {
            float nx = point.x * scale;
            float ny = point.y * scale;
            float nz = point.z * scale;

            if(alertAboutObstacles && this.obstacles.contains(point)) {
                geb.addCubeVerticesTwoPoints(
                        nx, ny, nz,
                        nx + scale, ny + scale, nz + scale,
                        1, 0, 0, 1
                );

                continue;
            }

            geb.addCubeVerticesTwoPoints(
                    nx, ny, nz,
                    nx + scale, ny + scale, nz + scale,
                    red, green, blue, alpha
            );
        }

        return geb.build(scene);
    }

    private Entity drawCubesFromTemporaryPoints(Scene scene, float scale) {
        if(this.temporaryPoints.got()) {
            return null;
        }

        GeometryEntityBuilder geb = new GeometryEntityBuilder();

        for(ColoredPoint cp : this.temporaryPoints.getPoints()) {
            float nx = cp.x * scale;
            float ny = cp.x * scale;
            float nz = cp.x * scale;

            geb.addCubeVerticesTwoPoints(nx, ny, nz, nx + scale, ny + scale, nz + scale, cp.r, cp.g, cp.b, cp.a);
        }

        return geb.build(scene);
    }

    public void finishSetup() {
        this.drawCubesFromPointList(scene, this.exits, this.scale, 0.2f, 0.8f, 0.3f, 1, false);
        this.drawCubesFromPointList(scene, this.bees, this.scale, 0.9f, 0.9f, 0.1f, 1, false);

        LineEntityBuilder leb = new LineEntityBuilder();
        leb.addCubeVerticesTwoPoints(0, 0, 0, this.width * this.scale, this.height * this.scale, this.length * scale, 1, 1, 1, 1);
        leb.build(scene);
    }

    @Override
    public void initialize(Window window, Scene scene, Renderer renderer) {
        this.scene = scene;
        glEnable(GL_DEBUG_OUTPUT);
        glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
            String s = MemoryUtil.memUTF8(message);
            throw new IllegalStateException(s);
        }, 0);

        Camera camera = scene.getCamera();
        camera.setPosition(new Vector3f(-8, 10.5f, 6));
        camera.setYaw(0.26175f);
        camera.setPitch(-(float)(Math.PI / 8));
        camera.updateDirection();
        camera.updateView();

        renderer.addRenderable(new GeometryRenderer());
        window.setVisible(true);
        Beep.finishSetup();
    }

    @Override
    public void input(Window window, Scene scene, float deltaMillis) {
        Camera camera = scene.getCamera();
        Vector3f v = camera.getDirection();

        float cameraSpeed = 0.5f;
        if(window.isKeyPressed(GLFW_KEY_W)) {
            camera.getPosition().add(new Vector3f(v).mul(cameraSpeed * deltaMillis));
        }

        if(window.isKeyPressed(GLFW_KEY_S)) {
            camera.getPosition().sub(new Vector3f(v).mul(cameraSpeed * deltaMillis));
        }

        if(window.isKeyPressed(GLFW_KEY_A)) {
            camera.getPosition().sub(new Vector3f(v).cross(UP).normalize().mul(cameraSpeed * deltaMillis));
        }

        if(window.isKeyPressed(GLFW_KEY_D)) {
            camera.getPosition().add(new Vector3f(v).cross(UP).normalize().mul(cameraSpeed * deltaMillis));
        }

        if(window.isKeyPressed(GLFW_KEY_F)) {
            window.setFullscreen(!window.isFullscreen(), glfwGetPrimaryMonitor());
        }

        if(window.isKeyPressed(GLFW_KEY_SPACE)) {
            camera.getPosition().add(new Vector3f(0, cameraSpeed * deltaMillis, 0));
        }

        if(window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            camera.getPosition().sub(new Vector3f(0, cameraSpeed * deltaMillis, 0));
        }

        float dYaw, dPitch;
        float pitch = camera.getPitch();

        MouseInput mi = window.getMouseInput();
        Vector2f mousePos = mi.getCurrentPos();

        if(this.lastMousePos == null) {
            this.lastMousePos = mousePos;
        }

        dYaw = this.lastMousePos.x - mousePos.x;
        dPitch = this.lastMousePos.y - mousePos.y;

        this.lastMousePos = mousePos;

        float cameraRotationSpeed = 0.01f;
        camera.addYaw(-dYaw * deltaMillis * cameraRotationSpeed);

        pitch = pitch + dPitch * deltaMillis * cameraRotationSpeed;
        float sign = pitch >= 0 ? 1 : -1;
        float cameraPitchLimit = 1.57f;
        if(Math.abs(pitch) >= cameraPitchLimit) {
            pitch = sign * (cameraPitchLimit);
        }
        camera.setPitch(pitch);


        camera.updateDirection();
        camera.updateView();
    }

    @Override
    public void update(Window window, Scene scene, float deltaMillis) {
        Camera camera = scene.getCamera();
        Vector3f position = camera.getPosition();
        float yaw = camera.getYaw();
        float pitch = camera.getPitch();

        String s = "Beep 2 | x: " + position.x + ", y: " + position.y + ", z: " + position.z + ", yaw: " + yaw + ", pitch: " + pitch;

        Entity e = this.drawCubesFromTemporaryPoints(scene, this.scale);

        if(e != null) {
            this.lastTempEntity = e;
        }

        window.setTitle(s);
    }

    @Override
    public void clean() {

    }

    public void addPoint(int x, int y, int z, float r, float g, float b, float a) {
        this.temporaryPoints.addPoint(x, y, z, r, g, b, a);
    }

    public void flushPoints() {
        this.temporaryPoints.flushPoints();
    }
}
