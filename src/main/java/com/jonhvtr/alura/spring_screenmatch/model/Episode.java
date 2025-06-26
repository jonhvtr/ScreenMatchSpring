package com.jonhvtr.alura.spring_screenmatch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Getter
@Setter
@AllArgsConstructor
public class Episode {
    private Integer season;
    private String title;
    private Integer numEpisode;
    private Double rating;
    private LocalDate releaseDate;

    public Episode(Integer numSeason, DataEpisode dataEpisode) {
        this.season = numSeason;
        this.title = dataEpisode.title();
        this.numEpisode = dataEpisode.numEpisode();
        try {
            this.rating = Double.valueOf(dataEpisode.rating());
        } catch (NumberFormatException e) {
            this.rating = 0.0;
        }
        try {
            this.releaseDate = LocalDate.parse(dataEpisode.releaseDate());
        } catch (DateTimeParseException e) {
            this.releaseDate = null;
        }
    }

    @Override
    public String toString() {
        return "season=" + season +
                ", title='" + title + '\'' +
                ", numEpisode=" + numEpisode +
                ", rating=" + rating +
                ", releaseDate=" + releaseDate;
    }
}
