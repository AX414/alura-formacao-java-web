package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.enums.Categoria;

public record SerieDTO( Long id,
                        String titulo,
                        Integer totalTemporadas,
                        Double avaliacao,
                        Categoria categoria,
                        String sinopse,
                        String atores,
                        String poster) {
}
