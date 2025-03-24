package br.com.alura.screenmatch_frases.model;


import jakarta.persistence.*;


@Entity
@Table(name="frases")
public class Frase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String poster;
    private String personagem;
    private String frase;

    public Frase() {
    }

    public Frase(Long id, String titulo, String poster, String personagem, String frase) {
        this.id = id;
        this.titulo = titulo;
        this.poster = poster;
        this.personagem = personagem;
        this.frase = frase;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPersonagem() {
        return personagem;
    }

    public void setPersonagem(String personagem) {
        this.personagem = personagem;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    @Override
    public String toString() {
        return "Frase{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", poster='" + poster + '\'' +
                ", personagem='" + personagem + '\'' +
                ", frase='" + frase + '\'' +
                '}';
    }
}
