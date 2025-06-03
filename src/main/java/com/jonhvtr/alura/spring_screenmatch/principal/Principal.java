package com.jonhvtr.alura.spring_screenmatch.principal;

import com.jonhvtr.alura.spring_screenmatch.model.DataEpisode;
import com.jonhvtr.alura.spring_screenmatch.model.DataSeason;
import com.jonhvtr.alura.spring_screenmatch.model.DataSerie;
import com.jonhvtr.alura.spring_screenmatch.model.Episode;
import com.jonhvtr.alura.spring_screenmatch.service.ConsumingData;
import com.jonhvtr.alura.spring_screenmatch.service.ConvertingData;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Principal {
    Scanner scan = new Scanner(System.in);
    private final String ADDRESS = "http://www.omdbapi.com/?t=";
    Dotenv dotenv = Dotenv.load();
    private final String API_KEY = dotenv.get("API_KEY");
    private final ConsumingData consumingData = new ConsumingData();
    private final ConvertingData convertingData = new ConvertingData();

    public void exibeMenu() {
        System.out.println("Digite o nome de uma Série");
        String nameSerie = scan.nextLine();
        String query = URLEncoder.encode(nameSerie, StandardCharsets.UTF_8);
        String json = consumingData.getData(ADDRESS + query +
                "&apikey=" + API_KEY);

        DataSerie data = convertingData.getData(json, DataSerie.class);
        System.out.println(data);

        List<DataSeason> listSeason = new ArrayList<>();

        for (int i = 1; i <= data.totalSeasons(); i++) {
            json = consumingData.getData(ADDRESS + query +
                    "&season=" + i + "&apikey=" + API_KEY);
            DataSeason dataSeason1 = convertingData.getData(json, DataSeason.class);
            listSeason.add(dataSeason1);
        }
        listSeason.forEach(System.out::println);

        // ao invés de:
//        for (int i = 0; i < data.totalSeasons(); i++){
//            List<DataEpisode> dataEpisodeList = listSeason.get(i).episodesList();
//            for (int j = 0; j < dataEpisodeList.size(); j++){
//                System.out.println(dataEpisodeList.get(i).title());
//            }
//        }

        // Função Lambda
        listSeason.forEach(t -> t.episodesList()
                .forEach(e -> System.out.println("Titulo: " + e.title())));

        List<DataEpisode> dataEpisodes = listSeason.stream()
                .flatMap(t -> t.episodesList().stream())
                .toList();

        System.out.println("\nTop 5 episódios");
        dataEpisodes.stream().filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DataEpisode::rating).reversed())
                .limit(5).forEach(System.out::println);

        List<Episode> episodes = listSeason.stream()
                .flatMap(t -> t.episodesList().stream()
                        .map(d -> new Episode(t.season(), d))).toList();

        episodes.forEach(System.out::println);

        System.out.println("A partir de que ano você deseja ver os eposódios?");
        int ano = scan.nextInt();
        scan.nextLine();

        LocalDate dataSearch = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodes.stream()
                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(dataSearch))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getSeason() +
                                " Episódio: " + e.getNumEpisode() +
                                " Data lançamento: " + e.getReleaseDate().format(formatter)
                ));
    }
}
