package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodios(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Episode") Integer numeroEpisodio,
        @JsonAlias("Season") String temporada,
        @JsonAlias("imdbRating") String avaliacao,  // Agora será String para lidar com "N/A"
        @JsonProperty("Released") String dataLancamento) {

    public DadosEpisodios {

    }

    public String avaliacao() {
        if (this.avaliacao.equalsIgnoreCase("N/A")) {
            return "N/A";
        }
        return avaliacao;
    }

    public Integer numeroEpisodio() {
        return numeroEpisodio;
    }

    public String temporada() {
        return temporada;
    }

    public String titulo() {
        return titulo;
    }

    public String dataLancamento() {
        if (this.dataLancamento.equalsIgnoreCase("N/A")) {
            return "N/A";
        }
        return dataLancamento;
    }

    @Override
    public String toString() {
        return "| Episódio: " + numeroEpisodio +
                " | Temporada: " + temporada +
                " | Título: " + titulo +
                " | Avaliação: " + avaliacao +
                " | Data de Lançamento: " + dataLancamento +
                " |";
    }
}
