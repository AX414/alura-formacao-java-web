package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
    Exemplo de consulta da API:

    {"Title":"The Boys","Year":"2019–",
    "Rated":"TV-MA","Released":"26 Jul 2019",
    "Runtime":"31S min",
    "Genre":"Action, Comedy, Crime",
    "Director":"N/A",
    "Writer":"Eric Kripke",
    "Actors":"Karl Urban, Jack Quaid, Antony Starr",
    "Plot":"A group of vigilantes set out to take down corrupt superheroes who abuse their superpowers.",
    "Language":"English",
    "Country":"United States",
    "Awards":"Won 1 Primetime Emmy. 22 wins & 83 nominations total",
    "Poster":"https://m.media-amazon.com/images/M/MV5BMWJlN2U5MzItNjU4My00NTM2LWFjOWUtOWFiNjg3ZTMxZDY1XkEyXkFqcGc@._V1_SX300.jpg",
    "Ratings":[{"Source":"Internet Movie Database","Value":"8.7/10"}],
    "Metascore":"N/A","imdbRating":"8.7",
    "imdbVotes":"738,359","imdbID":"tt1190634",
    "Type":"series","totalSeasons":"5","Response":"True"}
     */

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

    @Override
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