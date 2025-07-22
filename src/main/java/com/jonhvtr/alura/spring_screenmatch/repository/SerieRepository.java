package com.jonhvtr.alura.spring_screenmatch.repository;

import com.jonhvtr.alura.spring_screenmatch.model.Category;
import com.jonhvtr.alura.spring_screenmatch.model.Episode;
import com.jonhvtr.alura.spring_screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTitleContainingIgnoreCase(String nameSerie);

    List<Serie> findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(String nameActor, double rating);

    List<Serie> findTop5ByOrderByRatingDesc();

    List<Serie> findByGenre(Category category);

    List<Serie> findByTotalSeasonsLessThanEqualAndRatingGreaterThanEqual(Integer totalSeasons, double rating);

    @Query("SELECT s FROM Serie s WHERE s.totalSeasons <= :totalSeasons AND s.rating >= :rating")
    List<Serie> seriesBySeasonAndRating(Integer totalSeasons, double rating);

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE e.title ILIKE %:excerpt%")
    List<Episode> searchForEpisodeByExcerpt(String excerpt);

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s = :serie ORDER BY e.rating DESC LIMIT 5")
    List<Episode> searchTopEpisodeBySerie(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s = :serie AND YEAR(e.releaseDate) >= :releaseYear")
    List<Episode> searchEpisodeByReleaseDate(Serie serie, int releaseYear);

    @Query("SELECT s FROM Serie s JOIN s.episodes e GROUP BY s ORDER BY MAX(e.releaseDate) DESC LIMIT 5")
    List<Serie> findReleases();

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s.id = :id AND e.season = :numero")
    List<Episode> getEpisodeBySeason(Long id, Long numero);
}
