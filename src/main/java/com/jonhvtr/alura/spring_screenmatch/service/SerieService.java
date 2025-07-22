package com.jonhvtr.alura.spring_screenmatch.service;

import com.jonhvtr.alura.spring_screenmatch.dto.EpisodeDTO;
import com.jonhvtr.alura.spring_screenmatch.dto.SerieDTO;
import com.jonhvtr.alura.spring_screenmatch.model.Category;
import com.jonhvtr.alura.spring_screenmatch.model.Episode;
import com.jonhvtr.alura.spring_screenmatch.model.Serie;
import com.jonhvtr.alura.spring_screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerieService {
    @Autowired
    private SerieRepository serieRepository;

    private List<SerieDTO> convertingData(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(
                        s.getId(), s.getTitle(), s.getGenre(), s.getActors(), s.getPoster(),
                        s.getPlot(), s.getTotalSeasons(), s.getRating()))
                .toList();
    }

    private List<EpisodeDTO> convertingDataEpisodes(List<Episode> episodes) {
        return episodes.stream()
                .map(e -> new EpisodeDTO(e.getTitle(), e.getSeason(), e.getNumEpisode()))
                .toList();
    }

    public List<SerieDTO> findAllSeries() {
        return convertingData(serieRepository.findAll());
    }

    public List<SerieDTO> findTopFiveSeries() {
        return convertingData(serieRepository.findTop5ByOrderByRatingDesc());
    }

    public List<SerieDTO> findRelease() {
        return convertingData(serieRepository.findReleases());
    }

    public SerieDTO getById(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(
                    s.getId(), s.getTitle(), s.getGenre(), s.getActors(), s.getPoster(),
                    s.getPlot(), s.getTotalSeasons(), s.getRating());
        }
        return null;
    }

    public List<EpisodeDTO> findAllSeason(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return convertingDataEpisodes(s.getEpisodes());
        }
        return null;
    }

    public List<EpisodeDTO> findSeasonByNumber(Long id, Long numero) {
        return convertingDataEpisodes(serieRepository.getEpisodeBySeason(id, numero));
    }

    public List<SerieDTO> getSeriesByCategory(String nomeCategoria) {
        Category category = Category.fromString(nomeCategoria);
        return convertingData(serieRepository.findByGenre(category));
    }
}
