package com.example.sicaksumobileapp.models;

import java.io.Serializable;
import java.util.Objects;

public class SicakSuProfile implements Serializable {
    String id;
    String name;
    String surname;
    String imageUrl;

    public SicakSuProfile() {
        this.id = "";
        this.name = "";
        this.surname = "";
        this.imageUrl = "";
    }

    public SicakSuProfile(String id, String name, String surname, String imageUrl) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "SicakSuProfile{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SicakSuProfile that = (SicakSuProfile) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
