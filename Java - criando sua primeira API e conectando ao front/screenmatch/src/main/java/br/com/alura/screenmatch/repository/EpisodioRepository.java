package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EpisodioRepository extends JpaRepository<Episodio, Long> {

    Optional<Episodio> findFirstBySerie_TituloAndTituloIgnoreCase(String tituloSerie, String tituloEpisodio);
    List<Episodio> findDistinctTop10BySerieTituloAndAvaliacaoIsNotNullOrderByAvaliacaoDesc(String tituloSerie);

    @Query("SELECT e.titulo FROM Episodio e WHERE e.serie.titulo = :tituloSerie")
    List<String> findDistinctTituloBySerieTitulo(String tituloSerie);
    List<Episodio> findDistinctBySerieTitulo(String tituloSerie);

    @Query("SELECT e FROM Episodio e WHERE e.titulo LIKE %:trechoTitulo%")
    List<Episodio> findByTrechoTitulo(@Param("trechoTitulo") String trechoTitulo);
    List<Episodio> findDistinctTop5BySerieTituloAndAvaliacaoIsNotNullOrderByAvaliacaoDesc(String tituloSerie);
    List<Episodio> findDistinctBySerieTituloAndDataLancamentoGreaterThanEqual(String tituloSerie, LocalDate dataLancamento);

}


