package com.github.spa_ce42.beep2;

import com.github.spa_ce42.beep2.logic.Point;
import com.github.spa_ce42.beep2.logic.Space;
import com.github.spa_ce42.beep2.logic.SpaceContext;
import com.github.spa_ce42.beep2.logic.SpaceContextLoader;
import com.github.spa_ce42.beep2.render.GeometryRenderer;
import com.github.spa_ce42.beep2.util.GeometryEntityBuilder;
import com.github.spa_ce42.beep2.util.LineEntityBuilder;
import com.github.spa_ce42.beep2.util.Triple;
import com.github.spa_ce42.projectxl.core.ApplicationLogic;
import com.github.spa_ce42.projectxl.core.Configuration;
import com.github.spa_ce42.projectxl.core.Engine;
import com.github.spa_ce42.projectxl.core.Window;
import com.github.spa_ce42.projectxl.entity.Entity;
import com.github.spa_ce42.projectxl.event.MouseInput;
import com.github.spa_ce42.projectxl.render.Renderer;
import com.github.spa_ce42.projectxl.scene.Camera;
import com.github.spa_ce42.projectxl.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.util.List;

import static com.github.spa_ce42.beep2.logic.Space.OBS;
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

public class Main implements ApplicationLogic {
    private static final Vector3f UP = new Vector3f(0, 1, 0);
    private final float scale = 0.25f;
    private Space space;
    private List<Point> bees;
    private List<Point> exits;
    private Vector2f lastMousePos;
    private boolean ended = false;
    private Entity lastDrawnPath;
    private Entity lastDrawnNeighborsThatAreObstacles;
    private final int setupPointer = 2;
    private int beePointer = 0;

    private Entity drawObstaclesFromPointList(Scene scene, Space space, List<Point> points, float scale, float red, float green, float blue, float alpha) {
        LineEntityBuilder leb = new LineEntityBuilder();

        for(Point point : points) {
            if(space.get(point.x, point.y, point.z) != OBS) {
                continue;
            }

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

    private Entity drawCubesFromPointList(Scene scene, List<Point> points, float scale, float red, float green, float blue, float alpha) {
        GeometryEntityBuilder geb = new GeometryEntityBuilder();

        for(Point point : points) {
            float nx = point.x * scale;
            float ny = point.y * scale;
            float nz = point.z * scale;

            geb.addCubeVerticesTwoPoints(
                    nx, ny, nz,
                    nx + scale, ny + scale, nz + scale,
                    red, green, blue, alpha
            );
        }

        return geb.build(scene);
    }

    @Override
    public void initialize(Window window, Scene scene, Renderer renderer) {
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

        SpaceContextLoader scl = new SpaceContextLoader("beesetup" + this.setupPointer + ".txt");
        SpaceContext sc = scl.load();
        scl.close();
        exits = sc.getExits();
        bees = sc.getBees();

        this.drawCubesFromPointList(scene, this.exits, this.scale, 0.2f, 0.8f, 0.3f, 1);
        this.drawCubesFromPointList(scene, this.bees, this.scale, 0.9f, 0.9f, 0.1f, 1);

        this.space = new Space(sc);
        this.space.aStarPrepare(bees.get(this.beePointer), exits.get(this.beePointer));

        LineEntityBuilder leb = new LineEntityBuilder();
        leb.addCubeVerticesTwoPoints(0, 0, 0, this.space.getWidth() * this.scale, this.space.getHeight() * this.scale, this.space.getLength() * scale, 1, 1, 1, 1);
        leb.build(scene);

        renderer.addRenderable(new GeometryRenderer());
        window.setVisible(true);
    }

    private float cameraPitchLimit = 1.57f;

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
        if(Math.abs(pitch) >= this.cameraPitchLimit) {
            pitch = sign * (this.cameraPitchLimit);
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

        String s = "Program - " + ", x: " + position.x + ", y: " + position.y + ", z: " + position.z + ", yaw: " + yaw + ", pitch: " + pitch;

        if(!ended) {
            Triple<List<Point>, List<Point>, Boolean> p = this.space.aStarStep();
            this.ended = p.z();

            if(this.lastDrawnPath != null) {
                scene.removeEntity(this.lastDrawnPath);
                scene.removeModel(this.lastDrawnPath.getModelId());
            }

            if(this.lastDrawnNeighborsThatAreObstacles != null) {
                scene.removeEntity(this.lastDrawnNeighborsThatAreObstacles);
                scene.removeModel(this.lastDrawnNeighborsThatAreObstacles.getModelId());
            }

            this.lastDrawnPath = this.drawCubesFromPointList(scene, p.x(), this.scale, 0.2f, 0.3f, 0.8f, 1);
            this.lastDrawnNeighborsThatAreObstacles = this.drawObstaclesFromPointList(scene, this.space, p.y(), this.scale, 0.8f, 0.2f, 0.3f, 1);
        }

        if(ended) {
            if(this.beePointer < 15) {
                ++this.beePointer;
                this.space.aStarPrepare(this.bees.get(this.beePointer), this.exits.get(this.beePointer));
                this.ended = false;
            }
            if(this.beePointer >= 15) {
                s = s + " (ended)";
            }
        }

        window.setTitle(s);
    }

    @Override
    public void clean() {

    }

    public static void main(String[] args) {
        Configuration c = new Configuration();
        c.ups = 10;
        c.fps = 60;
        Engine engine = new Engine(c, new Main());
        engine.run();
        engine.clean();
    }
}
