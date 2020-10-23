package com.dvc.reactivespring.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class Fruit {

    @Id
    private String id;
    private String name;
    private FruitType fruitType;

    public Fruit() {}

    public Fruit(String id, FruitType fruitType, String name){
        this.id = id;
        this.fruitType = fruitType;
        this.name = name;
    }

   public Fruit(FruitType fruitType, String name){
        this.fruitType = fruitType;
        this.name = name;
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

    public FruitType getFruitType() {
        return fruitType;
    }

    public void setFruitType(FruitType fruitType) {
        this.fruitType = fruitType;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "id=" + id +
                ", fruitType=" + fruitType +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fruit)) return false;

        Fruit fruit = (Fruit) o;

        if (!id.equals(fruit.id)) return false;
        if (fruitType != fruit.fruitType) return false;
        return name.equals(fruit.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + fruitType.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
