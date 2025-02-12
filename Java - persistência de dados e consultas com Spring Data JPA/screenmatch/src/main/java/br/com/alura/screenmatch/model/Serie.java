package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.enums.Categoria;
import br.com.alura.screenmatch.service.traducaoOpenAI.TraduzirComAPI;
import br.com.alura.screenmatch.service.traducaoMyMemory.ConsultaMyMemory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name="serie")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", unique=true)
    private String titulo;

    @Column(name = "totalTemporadas")
    private Integer totalTemporadas;

    @Column(name = "avaliacao")
    private Double avaliacao;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Column(name = "atores")
    private String atores;

    @Column(name = "poster")
    private String poster;

    @Column(name = "sinopse")
    private String sinopse;

    @OneToMany(mappedBy="serie", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();

    // Construtor padrão (exigido pela JPA)
    public Serie(){}

    public Serie(DadosSerie dadosSerie) {
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();

        try {
            this.avaliacao = Double.valueOf(dadosSerie.avaliacao());
        } catch (NumberFormatException e) {
            this.avaliacao = 0.0;
        }

        // Obtém a categoria original
        String categoriaOriginal = dadosSerie.categoria().split(",")[0].trim();

        // Atribui o enum Categoria, não a tradução
        this.categoria = Categoria.getByTraducao(categoriaOriginal)
                .orElse(Categoria.DESCONHECIDO);  // Atribui a categoria caso seja desconhecida

        this.atores = dadosSerie.atores();
        this.poster = dadosSerie.poster();
        traduzirSinopse(dadosSerie.sinopse());
    }


    public void traduzirSinopse(String sinopse){
        try {
            this.sinopse = TraduzirComAPI.obterTraducao(sinopse).trim();
        } catch (Exception e) {
            try{
                this.sinopse = ConsultaMyMemory.obterTraducao(sinopse);
            } catch(Exception ex){
                this.sinopse = sinopse; // Se falhar, mantém em inglês
            }
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Integer getTotalTemporadas() { return totalTemporadas; }
    public void setTotalTemporadas(Integer totalTemporadas) { this.totalTemporadas = totalTemporadas; }

    public Double getAvaliacao() { return avaliacao; }
    public void setAvaliacao(Double avaliacao) { this.avaliacao = avaliacao; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getAtores() { return atores; }
    public void setAtores(String atores) { this.atores = atores; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }

    public String getSinopse() { return sinopse; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }

    public List<Episodio> getEpisodios() { return episodios; }
    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    @Override
    public String toString() {
        return "Serie{" +
                "titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", categoria=" + categoria +
                ", atores='" + atores + '\'' +
                ", poster='" + poster + '\'' +
                ", sinopse='" + sinopse + '\'' +
                '}';
    }
}
