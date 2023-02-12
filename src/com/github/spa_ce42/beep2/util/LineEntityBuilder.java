package com.github.spa_ce42.beep2.util;

import com.github.spa_ce42.beep2.render.GeometryMesh;
import com.github.spa_ce42.beep2.render.GeometryModel;
import com.github.spa_ce42.projectxl.entity.AbstractModel;
import com.github.spa_ce42.projectxl.entity.Entity;
import com.github.spa_ce42.projectxl.entity.Material;
import com.github.spa_ce42.projectxl.scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.lwjgl.opengl.GL11.GL_LINES;

public class LineEntityBuilder {
    private final List<Float> vertices;
    private final List<Float> colors;
    private final List<Integer> indices;
    private int vPointer;
    private int cPointer;
    private int iPointer;

    public LineEntityBuilder() {
        this.vertices = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.indices = new ArrayList<>();
    }

    private void floatListAdd(int index, float x, float y, float z) {
        this.vertices.add(index + 0, x);
        this.vertices.add(index + 1, y);
        this.vertices.add(index + 2, z);
    }

    private void floatListAdd(int index, float x, float y, float z, float w) {
        this.colors.add(index + 0, x);
        this.colors.add(index + 1, y);
        this.colors.add(index + 2, z);
        this.colors.add(index + 3, w);
    }

    public void addCubeVerticesTwoPoints(
            float x, float y, float z,
            float a, float b, float c,
            float red, float green, float blue, float alpha
    ) {
        int j = this.vPointer / 3;
        int i = this.vPointer;
        floatListAdd(i + 0, x, y, z);
        floatListAdd(i + 3, a, y, z);
        floatListAdd(i + 6, a, y, c);
        floatListAdd(i + 9, x, y, c);
        floatListAdd(i + 12, x, b, z);
        floatListAdd(i + 15, a, b, z);
        floatListAdd(i + 18, a, b, c);
        floatListAdd(i + 21, x, b, c);
        this.vPointer = this.vPointer + 24;

        i = this.cPointer;
        floatListAdd(i + 0, red, green, blue, alpha);
        floatListAdd(i + 4, red, green, blue, alpha);
        floatListAdd(i + 8, red, green, blue, alpha);
        floatListAdd(i + 12, red, green, blue, alpha);
        floatListAdd(i + 16, red, green, blue, alpha);
        floatListAdd(i + 20, red, green, blue, alpha);
        floatListAdd(i + 24, red, green, blue, alpha);
        floatListAdd(i + 28, red, green, blue, alpha);
        this.cPointer = this.cPointer + 32;

        i = this.iPointer;
        this.indices.add(i + 0, j + 0);
        this.indices.add(i + 1, j + 1);
        this.indices.add(i + 2, j + 1);
        this.indices.add(i + 3, j + 2);
        this.indices.add(i + 4, j + 2);
        this.indices.add(i + 5, j + 3);

        this.indices.add(i + 6, j + 3);
        this.indices.add(i + 7, j + 0);
        this.indices.add(i + 8, j + 4);
        this.indices.add(i + 9, j + 5);
        this.indices.add(i + 10, j + 5);
        this.indices.add(i + 11, j + 6);

        this.indices.add(i + 12, j + 6);
        this.indices.add(i + 13, j + 7);
        this.indices.add(i + 14, j + 7);
        this.indices.add(i + 15, j + 4);
        this.indices.add(i + 16, j + 0);
        this.indices.add(i + 17, j + 4);

        this.indices.add(i + 18, j + 1);
        this.indices.add(i + 19, j + 5);
        this.indices.add(i + 20, j + 2);
        this.indices.add(i + 21, j + 6);
        this.indices.add(i + 22, j + 3);
        this.indices.add(i + 23, j + 7);
        this.iPointer = this.iPointer + 24;
    }

    public Entity build(Scene scene) {
        float[] v = new float[this.vPointer];
        int i = 0;
        for(float f : this.vertices) {
            v[i++] = f;
        }

        float[] c = new float[this.cPointer];
        i = 0;
        for(float f : this.colors) {
            c[i++] = f;
        }

        int[] j = new int[this.iPointer];
        i = 0;
        for(int k : this.indices) {
            j[i++] = k;
        }

        List<Material> materials = new ArrayList<>();
        Material material = new Material();
        material.getMeshList().add(new GeometryMesh(GL_LINES, v, c, j));
        materials.add(material);

        String modelId = UUID.randomUUID().toString();
        AbstractModel model = new GeometryModel(modelId, materials);
        scene.addModel(model);

        Entity entity = new Entity(UUID.randomUUID().toString(), modelId);
        scene.addEntity(entity);
        return entity;
    }
}

