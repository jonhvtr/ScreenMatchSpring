package com.jonhvtr.alura.spring_screenmatch.controller;

import com.jonhvtr.alura.spring_screenmatch.dto.EpisodeDTO;
import com.jonhvtr.alura.spring_screenmatch.dto.SerieDTO;
import com.jonhvtr.alura.spring_screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService serieService;

    @GetMapping
    public List<SerieDTO> getSeries() {
        return serieService.findAllSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> getTopFiveSeries() {
        return serieService.findTopFiveSeries();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> getReleases() {
        return serieService.findRelease();
    }

    @GetMapping("/{id}")
    public SerieDTO getById(@PathVariable Long id) {
        return serieService.getById(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodeDTO> getEpisode(@PathVariable Long id) {
        return serieService.findAllSeason(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodeDTO> getSeasonByNumber(@PathVariable Long id, @PathVariable Long numero) {
        return serieService.findSeasonByNumber(id, numero);
    }

    @GetMapping("/categoria/{nomeCategoria}")
    public List<SerieDTO> getSeriesByCategory(@PathVariable String nomeCategoria) {
        return serieService.getSeriesByCategory(nomeCategoria);
    }
}
