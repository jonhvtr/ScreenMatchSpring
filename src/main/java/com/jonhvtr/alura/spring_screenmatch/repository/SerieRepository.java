package com.jonhvtr.alura.spring_screenmatch.repository;

import com.jonhvtr.alura.spring_screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {
}
