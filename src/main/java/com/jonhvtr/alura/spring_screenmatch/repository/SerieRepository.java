package com.jonhvtr.alura.spring_screenmatch.repository;

import com.jonhvtr.alura.spring_screenmatch.model.Category;
import com.jonhvtr.alura.spring_screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTitleContainingIgnoreCase(String nameSerie);

    List<Serie> findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(String nameActor, double rating);

    List<Serie> findTop5ByOrderByRatingDesc();

    List<Serie> findByGenre(Category category);

    List<Serie> findByTotalSeasonsLessThanEqualAndRatingGreaterThanEqual(Integer totalSeasons, double rating);
}
