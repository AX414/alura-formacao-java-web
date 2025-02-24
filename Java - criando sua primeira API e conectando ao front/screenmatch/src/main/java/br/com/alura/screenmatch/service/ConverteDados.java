package br.com.alura.screenmatch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ConverteDados {
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            // Criando o ObjectMapper
            ObjectMapper mapper = new ObjectMapper();

            // Registrando o módulo JavaTimeModule para lidar com LocalDate, LocalDateTime, etc.
            mapper.registerModule(new JavaTimeModule());

            // Desabilitando a serialização de datas como timestamps
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // Validar se a resposta contém erro antes do parsing
            if (json.contains("\"Error\"")) {
                System.out.println("\nErro na resposta da API: " + json);
                return null;
            }

            // Convertendo o JSON para o objeto da classe fornecida
            return mapper.readValue(json, classe);
        } catch (Exception e) {
            throw new RuntimeException("\nErro ao converter dados: " + e.getMessage(), e);
        }
    }
}
