package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.enums.Categoria;
import br.com.alura.screenmatch.model.Serie;
import jakarta.persistence.Column;

import java.time.LocalDate;

public record EpisodioDTO(Long id,
                          String titulo,
                          Integer temporada,
                          Integer numeroEpisodio,
                          Double avaliacao,
                          LocalDate dataLancamento,
                          String serie) {
}
