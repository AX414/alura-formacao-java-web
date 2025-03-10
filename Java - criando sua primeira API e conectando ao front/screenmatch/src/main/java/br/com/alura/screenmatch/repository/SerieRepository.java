package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.enums.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long>{
    Optional<Serie> findById(Long id);
    Optional<Serie> findByTitulo(String titulo);
    List<Serie> findDistinctByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacaoSerie);
    List<Serie> findDistinctTop5ByOrderByAvaliacaoDesc();
    List<Serie> findDistinctByCategoria(Categoria categoria);
    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int totalTemporadas, Double avaliacao);
    List<Serie> findTop5ByOrderByEpisodiosDataLancamentoDesc();


    @Query("SELECT s FROM Serie s Join s.episodios e GROUP BY s ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> lancamentosMaisRecentes();

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :temporada")
    List<Episodio> obterEpisodiosPorTemporada(Long id, Integer temporada);

}
