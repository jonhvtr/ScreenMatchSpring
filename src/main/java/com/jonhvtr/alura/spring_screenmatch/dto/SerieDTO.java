package com.jonhvtr.alura.spring_screenmatch.dto;

import com.jonhvtr.alura.spring_screenmatch.model.Category;

public record SerieDTO(Long id,
                       String title,
                       Category genre,
                       String actors,
                       String poster,
                       String plot,
                       Integer totalSeasons,
                       double rating) {
}
