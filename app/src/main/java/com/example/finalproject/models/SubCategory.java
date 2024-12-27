package com.example.finalproject.models;

public class SubCategory {
    private String subcategoryName, key;

    public SubCategory() {
    }

    public SubCategory(String subcategoryName) {
        this.subcategoryName = subcategoryName;

    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
