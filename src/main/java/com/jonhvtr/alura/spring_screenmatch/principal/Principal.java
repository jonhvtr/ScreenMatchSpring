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
import java.util.*;
import java.util.stream.Collectors;

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

        // Função Lambda
        listSeason.forEach(t -> t.episodesList()
                .forEach(e -> System.out.println("Titulo: " + e.title())));

        List<DataEpisode> dataEpisodes = listSeason.stream()
                .flatMap(t -> t.episodesList().stream())
                .toList();

        System.out.println("\nTop 10 episódios");
        dataEpisodes.stream().filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primeiro filtro(N/A): " + e))
                .sorted(Comparator.comparing(DataEpisode::rating).reversed())
                .peek(e -> System.out.println("Ordenação: " + e))
                .map(e -> e.title().toUpperCase())
                .peek(e -> System.out.println("Mapeamento: " + e))
                .limit(10)
                .peek(e -> System.out.println("Limite: " +  e))
                .forEach(System.out::println);

        List<Episode> episodes = listSeason.stream()
                .flatMap(t -> t.episodesList().stream()
                        .map(d -> new Episode(t.season(), d))).toList();

        episodes.forEach(System.out::println);

        System.out.println("Digite um trecho do título do episódio:");

        String excerptTitle = scan.nextLine();
        Optional<Episode> episodeSearched = episodes.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(excerptTitle.toUpperCase()))
                .findFirst();

        if (episodeSearched.isPresent()) {
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: " + episodeSearched.get().getSeason());
        } else {
            System.out.println("Episódio não encontrado!");
        }


        System.out.println("A partir de que ano você deseja ver os episódios?");
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

        Map<Integer, Double> ratingBySeasons = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason, Collectors.averagingDouble(Episode::getRating)));

        System.out.println(ratingBySeasons);

        DoubleSummaryStatistics statistics = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));

        System.out.println("Média: " + statistics.getAverage());
        System.out.println("Melhor Episódio: " + statistics.getMax());
        System.out.println("Pior Episódio: " + statistics.getMin());
        System.out.println("Quantidade: " + statistics.getCount());
    }
}
