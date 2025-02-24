package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.repository.EpisodioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EpisodioService {

    @Autowired
    private EpisodioRepository episodioRepository;

    public List<EpisodioDTO> converterDTO(List<Episodio> lista){
        return lista.stream().map(e-> new EpisodioDTO(
                        e.getId(),
                        e.getTitulo(),
                        e.getTemporada(),
                        e.getNumeroEpisodio(),
                        e.getAvaliacao(),
                        e.getDataLancamento(),
                        e.getSerie().getTitulo()
                ))
                .collect(Collectors.toList());
    }

    public List<EpisodioDTO> apresentarEpisodios(){
        return converterDTO(episodioRepository.findAll());
    }
}
