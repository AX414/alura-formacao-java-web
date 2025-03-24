package br.com.alura.screenmatch_frases;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.alura.screenmatch_frases")
public class ScreenmatchFrasesApplication {

    public static void main(String[] args) {

        // Carregar as variáveis no sistema
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(ScreenmatchFrasesApplication.class, args);
    }

}
