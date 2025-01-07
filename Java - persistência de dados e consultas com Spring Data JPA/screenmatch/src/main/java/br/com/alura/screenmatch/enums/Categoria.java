package br.com.alura.screenmatch.enums;

public enum Categoria {
    ACAO("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime"),
    ANIMACAO("Animation");

    private String categoriaOmdb;

    Categoria(String categoriaOmdb){
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("\nNenhuma categoria encontrada para a string fornecida: " + text);
    }

    // Método para obter o nome correto a ser salvo no banco de dados
    @Override
    public String toString() {
        return this.name();  // Retorna o nome do enum em maiúsculas
    }
}
