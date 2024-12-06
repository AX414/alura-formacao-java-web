package br.com.alura.screenmatch.service;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDados {
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Validar se a resposta contém erro antes do parsing
            if (json.contains("\"Error\"")) {
                System.out.println("Erro na resposta da API: " + json);
                return null;
            }

            return mapper.readValue(json, classe);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter dados: " + e.getMessage(), e);
        }
    }
}
