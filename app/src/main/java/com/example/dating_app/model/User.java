package com.example.dating_app.model;

public class User {
    private String name;

    private String email;
    private int age;
    private String imageUrl;
    private float distance;

    // Constructor
    public User(String name, String email, int age, String imageUrl, float distance) {
        this.name = name;

        this.email = email;
        this.age = age;
        this.imageUrl = imageUrl;
        this.distance = distance;
    }

    // Getter and Setter Methods
    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public float getDistance() {
        return distance;
    }
}
