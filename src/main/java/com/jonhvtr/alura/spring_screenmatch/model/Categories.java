package com.jonhvtr.alura.spring_screenmatch.model;

public enum Categories {
    ACTION("Action"),
    ROMANCE("Romance"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DRAMA("Drama");

    private String categorieOmdb;

    Categories(String categorieOmdb) {
        this.categorieOmdb = categorieOmdb;
    }

    public static Categories fromString(String text) {
        for (Categories categories : Categories.values()) {
            if (categories.categorieOmdb.equalsIgnoreCase(text)) {
                return categories;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada");
    }
}
