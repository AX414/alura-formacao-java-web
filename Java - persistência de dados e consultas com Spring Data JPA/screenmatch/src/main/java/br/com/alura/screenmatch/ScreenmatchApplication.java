package br.com.alura.screenmatch;

import br.com.alura.screenmatch.main.Main;
import br.com.alura.screenmatch.repository.EpisodioRepository;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.aspectj.apache.bcel.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    //Injeção de dependencia = delega a responsabilidade de instanciar uma
    //classe que precisamos usar toda hora
    @Autowired
    private SerieRepository serieRepository;

    @Autowired
    private EpisodioRepository episodioRepository;

    public static void main(String[] args) {
        // Carregar as variáveis no sistema
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(ScreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Main main = new Main(serieRepository, episodioRepository);
        main.consultarAPI();
    }
}
