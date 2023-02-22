package org.millburn.e24feik.beep2.scene;


import org.millburn.e24feik.beep2.core.Cleanable;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final String id;
    private final List<Entity> entitiesList;
    private final List<Material> meshList;

    public Model(String id, List<Material> meshList) {
        this.id = id;
        this.meshList = meshList;
        entitiesList = new ArrayList<>();
    }

    public void clean() {
        meshList.forEach(Cleanable::clean);
    }

    public List<Entity> getEntitiesList() {
        return entitiesList;
    }

    public String getId() {
        return id;
    }

    public List<Material> getMaterialList() {
        return this.meshList;
    }
}