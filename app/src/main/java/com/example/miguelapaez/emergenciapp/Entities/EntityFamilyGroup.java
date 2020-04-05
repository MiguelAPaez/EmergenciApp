package com.example.miguelapaez.emergenciapp.Entities;

public class EntityFamilyGroup {

    private int imgParent;
    private String name;
    private String parent;
    private String email;

    public EntityFamilyGroup(int imgParent, String name, String parent, String email) {
        this.imgParent = imgParent;
        this.name = name;
        this.parent = parent;
        this.email = email;
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

    public String getEmail() {
        return email;
    }
}
