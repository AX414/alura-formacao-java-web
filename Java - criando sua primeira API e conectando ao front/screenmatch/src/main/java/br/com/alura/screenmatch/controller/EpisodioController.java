package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.service.EpisodioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/episodios")
public class EpisodioController {

    @Autowired
    private EpisodioService episodioService;

    @GetMapping("/episodios")
    public List<EpisodioDTO> apresentarEpisodios(){
        return episodioService.apresentarEpisodios();
    }
}
