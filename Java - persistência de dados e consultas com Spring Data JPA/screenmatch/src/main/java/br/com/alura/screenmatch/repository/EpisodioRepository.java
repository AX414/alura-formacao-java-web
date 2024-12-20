package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EpisodioRepository extends JpaRepository<Episodio, Long>{
    Optional<Episodio> findByTitulo(String titulo);
    Optional<Episodio> findByTituloAndSerie_Titulo(String tituloEpisodio, String tituloSerie);
}
