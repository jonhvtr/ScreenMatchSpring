package com.jonhvtr.alura.spring_screenmatch.principal;

import com.jonhvtr.alura.spring_screenmatch.model.DataSeason;
import com.jonhvtr.alura.spring_screenmatch.model.DataSerie;
import com.jonhvtr.alura.spring_screenmatch.service.ConsumingData;
import com.jonhvtr.alura.spring_screenmatch.service.ConvertingData;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
            json = consumingData.getData(ADDRESS + nameSerie.replace(" ", "+") +
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
    }
}
