package com.github.spa_ce42.beep2.render;

import com.github.spa_ce42.projectxl.core.Cleanable;
import com.github.spa_ce42.projectxl.entity.AbstractModel;
import com.github.spa_ce42.projectxl.entity.Entity;
import com.github.spa_ce42.projectxl.entity.Material;

import java.util.ArrayList;
import java.util.List;

public class GeometryModel extends AbstractModel {
    private final String id;
    private final List<Entity> entitiesList;
    private final List<Material> meshList;

    public GeometryModel(String id, List<Material> meshList) {
        this.id = id;
        this.meshList = meshList;
        entitiesList = new ArrayList<>();
    }

    @Override
    public void clean() {
        meshList.forEach(Cleanable::clean);
    }

    public List<Entity> getEntitiesList() {
        return entitiesList;
    }

    public String getId() {
        return id;
    }

    @Override
    public List<Material> getMaterialList() {
        return this.meshList;
    }
}