package com.jonhvtr.alura.spring_screenmatch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series", schema = "alura_series")
@Getter
@Setter
@NoArgsConstructor
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @Enumerated(EnumType.STRING)
    private Categories genre;

    private String actors;
    private String poster;
    private String plot;
    private Integer totalSeasons;
    private double rating;

    @Transient
    private List<Episode> episodes = new ArrayList<>();

    public Serie(DataSerie dataSerie) {
        this.title = dataSerie.title();
        this.genre = Categories.fromString(dataSerie.genre().split(",")[0].trim());
        this.actors = dataSerie.actors();
        this.poster = dataSerie.poster();
        this.plot = dataSerie.plot();
        // this.plot = ChatGPTQuery.getTranslation(dataSerie.plot()).trim(); para traduzir a sinopse
        this.totalSeasons = dataSerie.totalSeasons();
        this.rating = OptionalDouble.of(Double.parseDouble(dataSerie.rating())).orElse(0.0);
    }

    @Override
    public String toString() {
        return "Serie{" +
                "genre=" + genre +
                ", title='" + title + '\'' +
                ", actors='" + actors + '\'' +
                ", poster='" + poster + '\'' +
                ", plot='" + plot + '\'' +
                ", totalSeasons=" + totalSeasons +
                ", rating=" + rating +
                '}';
    }
}
