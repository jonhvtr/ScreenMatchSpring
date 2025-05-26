package com.jonhvtr.alura.spring_screenmatch;

import com.jonhvtr.alura.spring_screenmatch.model.DataSerie;
import com.jonhvtr.alura.spring_screenmatch.service.ConsumingData;
import com.jonhvtr.alura.spring_screenmatch.service.ConvertingData;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumingData consumingData = new ConsumingData();
		Dotenv dotenv = Dotenv.load();
		String apiKey = dotenv.get("API_KEY");
		var json = consumingData.getData("http://www.omdbapi.com/?t=gilmore+girls" +
				"&apikey=" + apiKey);
		System.out.println(json);
		ConvertingData convertingData = new ConvertingData();
		DataSerie data = convertingData.getData(json, DataSerie.class);
		System.out.println(data);
	}
}
