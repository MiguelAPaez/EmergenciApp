package com.example.miguelapaez.emergenciapp.Entities;

public class PerfilBasico extends Perfil {
    String name;
    String lastName;
    String typeId;
    String id;
    String age;
    String phone;
    String gender;

    public PerfilBasico(String email, String name, String lastName, String typeId, String id, String age, String phone, String gender) {
        super(email);
        this.name = name;
        this.lastName = lastName;
        this.typeId = typeId;
        this.id = id;
        this.age = age;
        this.phone = phone;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
