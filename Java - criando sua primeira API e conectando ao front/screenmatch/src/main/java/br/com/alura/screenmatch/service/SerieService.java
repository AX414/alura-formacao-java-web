package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.enums.Categoria;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository serieRepository;

    public List<SerieDTO> converterDTO(List<Serie> lista){
        return lista.stream().map(s-> new SerieDTO(s.getId(),
                        s.getTitulo(),
                        s.getTotalTemporadas(),
                        s.getAvaliacao(),
                        s.getCategoria(),
                        s.getSinopse(),
                        s.getAtores(),
                        s.getPoster()))
                .collect(Collectors.toList());
    }

    public SerieDTO converterDTO(Serie s){
        return new SerieDTO(s.getId(),
                        s.getTitulo(),
                        s.getTotalTemporadas(),
                        s.getAvaliacao(),
                        s.getCategoria(),
                        s.getSinopse(),
                        s.getAtores(),
                        s.getPoster());
    }


    public List<SerieDTO> apresentarTodasSeries(){
        return converterDTO(serieRepository.findAll());
    }

    public List<SerieDTO> obterCincoMelhoresSeries(){
        return converterDTO(serieRepository.findDistinctTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> lancamentosMaisRecentes(){
        return converterDTO(serieRepository.lancamentosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);
        if (serie.isPresent()){
            return converterDTO(serie.get());
        }else{
            return null;
        }
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);
        if (serie.isPresent()){
            return serie.get().getEpisodios().stream().map(e-> new EpisodioDTO(
                    e.getId(),
                    e.getTitulo(),
                    e.getTemporada(),
                    e.getNumeroEpisodio(),
                    e.getAvaliacao(),
                    e.getDataLancamento(),
                    e.getSerie().getTitulo()
            )).toList();
        }else{
            return null;
        }
    }


    public List<EpisodioDTO> obterEpisodiosPorTemporadas(Long id, Integer temporada) {
        return serieRepository.obterEpisodiosPorTemporada(id, temporada).stream().map(e-> new EpisodioDTO(
                e.getId(),
                e.getTitulo(),
                e.getTemporada(),
                e.getNumeroEpisodio(),
                e.getAvaliacao(),
                e.getDataLancamento(),
                e.getSerie().getTitulo()
        )).toList();
    }

    public List<SerieDTO> obterSeriesPorCategoria(String nomeCategoria) {
        Categoria categoria = Categoria.fromString(nomeCategoria);
        return converterDTO(serieRepository.findDistinctByCategoria(categoria));
    }
}
