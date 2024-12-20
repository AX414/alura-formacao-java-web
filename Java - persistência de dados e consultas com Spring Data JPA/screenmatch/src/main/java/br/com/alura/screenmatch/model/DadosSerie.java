package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao,
                         @JsonProperty("imdbVotes") String votos,
                         @JsonProperty("Genre") String categoria,
                         @JsonProperty("Plot") String sinopse,
                         @JsonProperty("Actors") String atores,
                         @JsonProperty("Poster") String poster) {

    public DadosSerie {

    }

    public String titulo() {
        return titulo;
    }

    @Override
    public Integer totalTemporadas() {
        return totalTemporadas;
    }

    @Override
    public String avaliacao() {
        return avaliacao;
    }

    @Override
    public String votos() {
        return votos;
    }

    @Override
    public String categoria() { return categoria; }

    @Override
    public String sinopse() { return sinopse; }

    @Override
    public String atores() { return atores; }

    @Override
    public String poster() { return poster; }

    @Override
    public String toString() {
        return "DadosSerie{" +
                "titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao='" + avaliacao + '\'' +
                ", votos='" + votos + '\'' +
                ", categoria='" + categoria + '\'' +
                ", sinopse='" + sinopse + '\'' +
                ", atores='" + atores + '\'' +
                ", poster='" + poster + '\'' +
                '}';
    }
}