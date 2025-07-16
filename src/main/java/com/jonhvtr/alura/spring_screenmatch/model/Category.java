package com.jonhvtr.alura.spring_screenmatch.model;

public enum Category {
    ACTION("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDY("Comedy", "Comédia"),
    CRIME("Crime", "Crime"),
    DRAMA("Drama", "Drama"),
    REALITY_TV("Reality-TV", "Reality-TV");

    private final String categoryOmdb;
    private final String categoryPortuguese;

    Category(String categoryOmdb, String categoryPortuguese) {
        this.categoryOmdb = categoryOmdb;
        this.categoryPortuguese = categoryPortuguese;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.categoryOmdb.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada: " + text);
    }

    public static Category fromPortugueses(String text) {
        for (Category category : Category.values()) {
            if (category.categoryPortuguese.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada: " + text);
    }
}