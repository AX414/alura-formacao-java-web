package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/series") //É subentendido que seram /series no mapeamento
public class SerieController {

    @Autowired
    private SerieService serieService;

    @GetMapping
    public List<SerieDTO> apresentarTodasSeries(){
        return serieService.apresentarTodasSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterCincoMelhoresSeries(){
        return serieService.obterCincoMelhoresSeries();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos() {
        return serieService.lancamentosMaisRecentes();
    }

    @GetMapping("/{id}")
    public SerieDTO obterDetalhes(@PathVariable Long id){
        return serieService.obterPorId( id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id){
        return serieService.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{temporada}")
    public List<EpisodioDTO> obterEpisodiosPorTemporadas(@PathVariable Long id, @PathVariable Integer temporada){
        return serieService.obterEpisodiosPorTemporadas(id, temporada);
    }

}
