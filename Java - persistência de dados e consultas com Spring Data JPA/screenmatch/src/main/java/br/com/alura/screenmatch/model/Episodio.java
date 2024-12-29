package br.com.alura.screenmatch.model;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
            if (!(e.avaliacao().equals("N/A"))) {
                this.avaliacao = Double.valueOf(e.avaliacao());
            }
        } catch (Exception ex) {
            this.avaliacao = null;
        }
        try {
            if (!(e.dataLancamento().equals("N/A"))) {
                this.dataLancamento = LocalDate.parse(e.dataLancamento());
            }
        } catch (Exception ex) {
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
        try {
            if (!(dadosEpisodios.avaliacao().equals("N/A"))) {
                this.avaliacao = Double.valueOf(dadosEpisodios.avaliacao());
            }
        } catch (Exception ex) {
            this.avaliacao = null;
        }
        try {
            if (!(dadosEpisodios.dataLancamento().equals("N/A"))) {
                this.dataLancamento = LocalDate.parse(dadosEpisodios.dataLancamento());
            }
        } catch (Exception ex) {
            this.dataLancamento = null;
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyy");
        return "| Temporada: " + temporada +
                " | Episódio: " + numeroEpisodio +
                " | Título: " + titulo +
                " | Avaliação: " + avaliacao +
                " | Data de Lançamento: " +
                (dataLancamento == null ? "N/A" : dataLancamento.format(dtf)) +
                " |";
    }
}
