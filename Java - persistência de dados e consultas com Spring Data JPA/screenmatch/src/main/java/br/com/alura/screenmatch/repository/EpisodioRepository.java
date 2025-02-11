package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EpisodioRepository extends JpaRepository<Episodio, Long> {

    Optional<Episodio> findFirstBySerieTituloAndTituloContainingIgnoreCase(String tituloSerie, String tituloEpisodio);

    List<Episodio> findTop10BySerieTituloAndAvaliacaoIsNotNullOrderByAvaliacaoDesc(String tituloSerie);

    List<String> findTituloBySerieTitulo(String tituloSerie);

    List<Episodio> findBySerieTitulo(String tituloSerie);
}
