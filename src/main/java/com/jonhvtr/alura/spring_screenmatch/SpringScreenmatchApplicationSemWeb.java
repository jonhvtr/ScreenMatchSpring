//package com.jonhvtr.alura.spring_screenmatch;
//
//import com.jonhvtr.alura.spring_screenmatch.principal.NewPrincipal;
//import com.jonhvtr.alura.spring_screenmatch.repository.SerieRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class SpringScreenmatchApplicationSemWeb implements CommandLineRunner {
//
//    @Autowired
//    private SerieRepository serieRepository;
//    public static void main(String[] args) {
//        SpringApplication.run(SpringScreenmatchApplicationSemWeb.class, args);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        NewPrincipal newPrincipal = new NewPrincipal(serieRepository);
//
//        newPrincipal.exibeMenu();
//    }
//}
