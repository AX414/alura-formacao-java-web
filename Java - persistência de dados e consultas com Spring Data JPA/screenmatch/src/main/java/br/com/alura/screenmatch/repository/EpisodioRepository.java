package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EpisodioRepository extends JpaRepository<Episodio, Long>{
    Optional<Episodio> findByTituloContainingIgnoreCase(String titulo);
    Optional<Episodio> findByTituloAndSerie_Titulo(String tituloEpisodio, String tituloSerie);

    @Query(value = "SELECT e.* FROM episodios e " +
            "JOIN serie s ON s.id = e.serie_id " +
            "WHERE e.avaliacao IS NOT NULL " +
            "AND s.titulo = :tituloSerie " +
            "ORDER BY e.avaliacao DESC " +
            "LIMIT 10",
            nativeQuery = true)
    List<Episodio> findTop10BySerieTitulo(@Param("tituloSerie") String tituloSerie);

    @Query(value = "SELECT e.titulo FROM episodios e " +
            "JOIN serie s ON s.id = e.serie_id " +
            "WHERE s.titulo = :tituloSerie", nativeQuery = true)
    List<String> findEpisodioTitulosBySerieTitulo(@Param("tituloSerie") String tituloSerie);

    @Query(value = "SELECT e.* FROM episodios e " +
            "JOIN serie s ON s.id = e.serie_id " +
            "WHERE s.titulo = :tituloSerie", nativeQuery = true)
    List<Episodio> findAllBySerieTitulo(@Param("tituloSerie") String tituloSerie);

}
