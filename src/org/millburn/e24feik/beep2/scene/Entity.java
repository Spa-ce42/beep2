package org.millburn.e24feik.beep2.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Entity {
    private final String id;
    private final String modelId;
    private final Matrix4f modelMatrix;
    private final Vector3f position;
    private final Quaternionf rotation;
    private float scale;

    public Entity(String id, String modelId) {
        this.id = id;
        this.modelId = modelId;
        this.modelMatrix = new Matrix4f();
        this.position = new Vector3f();
        this.rotation = new Quaternionf();
        this.scale = 1;
    }

    public String getId() {
        return id;
    }

    public String getModelId() {
        return modelId;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public final void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setRotation(float x, float y, float z, float angle) {
        this.rotation.fromAxisAngleRad(x, y, z, angle);
    }

    public void updateModelMatrix() {
        modelMatrix.translationRotateScale(position, rotation, scale);
    }
}
