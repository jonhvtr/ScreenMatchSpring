package com.jonhvtr.alura.spring_screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataSeason(@JsonAlias("Title") String title,
                         @JsonAlias("Season") Integer season,
                         @JsonAlias("totalSeasons") Integer totalSeasons,
                         @JsonAlias("Episodes") List<DataEpisode> episodesList) {
}
