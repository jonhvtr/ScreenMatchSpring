package com.jonhvtr.alura.spring_screenmatch.principal;

import com.jonhvtr.alura.spring_screenmatch.model.*;
import com.jonhvtr.alura.spring_screenmatch.repository.SerieRepository;
import com.jonhvtr.alura.spring_screenmatch.service.ConsumingData;
import com.jonhvtr.alura.spring_screenmatch.service.ConvertingData;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class NewPrincipal {
    private final Scanner scan = new Scanner(System.in);
    private final ConsumingData consumingData = new ConsumingData();
    private final ConvertingData convertingData = new ConvertingData();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    Dotenv dotenv = Dotenv.load();
    private final String API_KEY = "&apikey=" + dotenv.get("API_KEY");
    private final List<DataSerie> dataSeries = new ArrayList<>();

    private final SerieRepository serieRepository;

    private List<Serie> series = new ArrayList<>();

    public NewPrincipal(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {

            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Buscar séries buscadas
                    4 - Buscar por título
                    5 - Buscar séries por autor
                    6 - Top 5 Séries
                    7 - Buscar Série por Categoria
                    8 - Buscar Séries por temporada
                    
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
                case 4:
                    searchSerieByTitle();
                    break;
                case 5:
                    searchSerieByActors();
                    break;
                case 6:
                    searchTop5Serie();
                    break;
                case 7:
                    searchSeriesByCategory();
                    break;
                case 8:
                    searchSeriesByNumberOfSeasons();
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
        Serie serie = new Serie(dataSerie);
        serieRepository.save(serie);
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
        searchHistory();
        System.out.println("Escolha uma série:");
        var nameSerie = scan.nextLine();

        Optional<Serie> serie = serieRepository.findByTitleContainingIgnoreCase(nameSerie);

        if (serie.isPresent()) {
            var seriesFound = serie.get();
            List<DataSeason> seasons = new ArrayList<>();

            for (int i = 1; i <= seriesFound.getTotalSeasons(); i++) {
                var json = consumingData.getData(ADDRESS +
                        seriesFound.getTitle().replace(" ", "+") + "&season=" + i + API_KEY);
                DataSeason dataSeason = convertingData.getData(json, DataSeason.class);
                seasons.add(dataSeason);
            }
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(d -> d.episodesList().stream()
                            .map(e -> new Episode(d.season(), e)))
                    .toList();

            seriesFound.setEpisodes(episodes);
            serieRepository.save(seriesFound);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void searchHistory() {
        series = serieRepository.findAll();
        series.stream().sorted(Comparator.comparing(Serie::getGenre)).forEach(System.out::println);
    }

    private void searchSerieByTitle() {
        System.out.println("Escolha uma série:");
        var nameSerie = scan.nextLine();

        Optional<Serie> foundSeries = serieRepository.findByTitleContainingIgnoreCase(nameSerie);

        if (foundSeries.isPresent()) {
            System.out.println("Dados da série: " + foundSeries.get());
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void searchSerieByActors() {
        System.out.println("Qual o nome para a busca?");
        var nameActor = scan.nextLine();
        System.out.println("Avaliações a partir de que valor?");
        var rating = scan.nextDouble();
        List<Serie> foundSeries = serieRepository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(nameActor
                , rating);
        System.out.println("Séries em que " + nameActor + " trabalhou: ");
        foundSeries.forEach(s -> System.out.println(s.getTitle() + " avaliação: " + s.getRating()));
    }

    private void searchTop5Serie() {
        List<Serie> topSeries = serieRepository.findTop5ByOrderByRatingDesc();
        topSeries.forEach(s -> System.out.println(s.getTitle() + " avaliação: " + s.getRating()));
    }

    private void searchSeriesByCategory() {
        System.out.println("Escolha uma categoria:");
        var nameCategory = scan.nextLine();
        Category category = Category.fromString(nameCategory);
        List<Serie> seriesByCategory = serieRepository.findByGenre(category);
        System.out.println("Séries da Categoria" + seriesByCategory);
        seriesByCategory.forEach(System.out::println);
    }

    private void searchSeriesByNumberOfSeasons() {
        System.out.println("Digite o números de temporadas que deseja:");
        var numSeasons = scan.nextInt();
        System.out.println("Digite o número de avaliações que deseja:");
        var rating = scan.nextDouble();
        List<Serie> seriesByNumberOfSeasons =
                serieRepository.findByTotalSeasonsLessThanEqualAndRatingGreaterThanEqual(numSeasons, rating);
        System.out.println("Séries encontradas:");
        seriesByNumberOfSeasons.forEach(System.out::println);
    }

}
