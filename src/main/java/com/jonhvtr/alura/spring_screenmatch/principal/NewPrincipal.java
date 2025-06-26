package com.jonhvtr.alura.spring_screenmatch.principal;

import com.jonhvtr.alura.spring_screenmatch.model.DataSeason;
import com.jonhvtr.alura.spring_screenmatch.model.DataSerie;
import com.jonhvtr.alura.spring_screenmatch.model.Serie;
import com.jonhvtr.alura.spring_screenmatch.service.ConsumingData;
import com.jonhvtr.alura.spring_screenmatch.service.ConvertingData;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class NewPrincipal {
    private Scanner scan = new Scanner(System.in);
    private ConsumingData consumingData = new ConsumingData();
    private ConvertingData convertingData = new ConvertingData();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    Dotenv dotenv = Dotenv.load();
    private final String API_KEY = "&apikey=" + dotenv.get("API_KEY");
    private List<DataSerie> dataSeries = new ArrayList<>();

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {

            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Buscar séries buscadas
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = scan.nextInt();
            scan.nextLine();

            switch (opcao) {
                case 1:
                    searchSerieWeb();
                    break;
                case 2:
                    searchEpisodeBySerie();
                    break;
                case 3:
                    searchHistory();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void searchSerieWeb() {
        DataSerie dataSerie = getDataSerie();
        dataSeries.add(dataSerie);
        System.out.println(dataSerie);
    }

    private DataSerie getDataSerie() {
        System.out.println("Digite o nome da série para busca");
        String nameSerie = scan.nextLine();
        String query = URLEncoder.encode(nameSerie, StandardCharsets.UTF_8);
        var json = consumingData.getData(ADDRESS + query + API_KEY);
        return convertingData.getData(json, DataSerie.class);
    }

    private void searchEpisodeBySerie() {
        DataSerie dataSerie = getDataSerie();
        List<DataSeason> seasons = new ArrayList<>();

        for (int i = 1; i <= dataSerie.totalSeasons(); i++) {
            var json = consumingData.getData(ADDRESS +
                    dataSerie.title().replace(" ", "+") + "&season=" + i + API_KEY);
            DataSeason dataSeason = convertingData.getData(json, DataSeason.class);
            seasons.add(dataSeason);
        }
        seasons.forEach(System.out::println);
    }

    private void searchHistory() {
        List<Serie> series = new ArrayList<>();
        series = dataSeries.stream().map(Serie::new).toList();
        series.stream().sorted(Comparator.comparing(Serie::getGenre)).forEach(System.out::println);
    }
}
