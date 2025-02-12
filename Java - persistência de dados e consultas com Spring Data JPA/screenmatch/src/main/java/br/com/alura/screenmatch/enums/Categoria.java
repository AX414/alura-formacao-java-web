package br.com.alura.screenmatch.enums;

import java.util.Optional;

public enum Categoria {
    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime"),
    ANIMACAO("Animation", "Animação"),
    DESCONHECIDO("Unknown", "Desconhecido"); // Categoria para valores desconhecidos

    private String categoriaOmdb;
    private String categoriaTraduzida;

    Categoria(String categoriaOmdb, String categoriaTraduzida) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaTraduzida = categoriaTraduzida;
    }

    // Método para converter de string para categoria traduzida
    public static Categoria fromString(String text) {
        if (text == null || text.isBlank()) {
            return Categoria.DESCONHECIDO;  // Retorna a categoria desconhecida diretamente
        }

        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text) || categoria.categoriaTraduzida.equalsIgnoreCase(text)) {
                return categoria;  // Retorna a categoria diretamente
            }
        }

        return Categoria.DESCONHECIDO;  // Retorna a categoria desconhecida se não encontrar
    }

    // Novo método para buscar a categoria pelo nome traduzido
    public static Optional<Categoria> getByTraducao(String traducao) {
        Categoria categoria = fromString(traducao);  // Chama fromString que agora retorna Categoria

        // Verifica se a categoria não é a DESCONHECIDO
        if (!categoria.equals(Categoria.DESCONHECIDO)) {
            return Optional.of(categoria);  // Retorna a categoria se encontrada
        } else {
            return Optional.empty();  // Retorna Optional vazio se não encontrar a tradução
        }
    }

    // Método para obter o nome correto a ser salvo no banco de dados
    @Override
    public String toString() {
        return this.name();  // Retorna o nome do enum em maiúsculas
    }

    // Método adicional para lidar com categorias desconhecidas
    private static String traduzirECriarCategoria(String text) {
        // A implementação dessa função depende de como você quer lidar com categorias desconhecidas
        return "DESCONHECIDO";  // Exemplo simples
    }
}
