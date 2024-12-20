package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", unique = true)
    private String titulo;

    @Column(name = "temporada")
    private Integer temporada;

    @Column(name = "numeroEpisodio")
    private Integer numeroEpisodio;

    @Column(name = "avaliacao")
    private Double avaliacao;

    @Column(name = "dataLancamento")
    private LocalDate dataLancamento;

    @ManyToOne
    @JoinColumn(name = "serie_id", nullable = false)
    private Serie serie;

    public Episodio() {
    }

    public Episodio(Integer temporada, DadosEpisodios e) {
        this.temporada = temporada;
        this.titulo = e.titulo();
        this.numeroEpisodio = e.numeroEpisodio();
        try {
            if(!(e.avaliacao().equals("N/A"))){
                this.avaliacao = Double.valueOf(e.avaliacao());
            }
        } catch (NumberFormatException ex) {
            this.avaliacao = null;
        }

        try {
            this.dataLancamento = e.dataLancamento();
        } catch (DateTimeParseException ex) {
            this.dataLancamento = null;
        }

    }

    public Episodio(Serie serie, String titulo, Integer temporada, Integer numeroEpisodio, Double avaliacao, LocalDate dataLancamento) {
        this.serie = serie;
        this.titulo = titulo;
        this.temporada = temporada;
        this.numeroEpisodio = numeroEpisodio;
        this.avaliacao = avaliacao;
        this.dataLancamento = dataLancamento;
    }

    public Episodio(DadosEpisodios dadosEpisodios) {
        this.titulo = dadosEpisodios.titulo();
        this.temporada = Integer.valueOf(dadosEpisodios.temporada());
        this.numeroEpisodio = dadosEpisodios.numeroEpisodio();
        this.dataLancamento = dadosEpisodios.dataLancamento();
        try {
            this.avaliacao = Double.valueOf(dadosEpisodios.avaliacao());
        } catch (NumberFormatException ex) {
            this.avaliacao = null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return "| Temporada: " + temporada +
                " | Episódio: " + numeroEpisodio +
                " | Título: " + titulo +
                " | Avaliação: " + avaliacao +
                " | Data de Lançamento: " + dataLancamento +
                " |";
    }
}
