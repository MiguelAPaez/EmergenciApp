package com.example.miguelapaez.emergenciapp.Entities;

public class EntityFamilyGroup {

    private int imgParent;
    private String name;
    private String parent;

    public EntityFamilyGroup(int imgParent, String name, String parent) {
        this.imgParent = imgParent;
        this.name = name;
        this.parent = parent;
    }

    public int getImgParent() {
        return imgParent;
    }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }
}
