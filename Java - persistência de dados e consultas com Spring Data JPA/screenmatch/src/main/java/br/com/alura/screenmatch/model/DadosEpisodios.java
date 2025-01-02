package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodios(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Episode") Integer numeroEpisodio,
        @JsonAlias("Season") String temporada,
        @JsonAlias("imdbRating") String avaliacao,  // Agora será String para lidar com "N/A"
        @JsonProperty("Released") String dataLancamento) {

    public DadosEpisodios {

    }

    public String avaliacao() {
        if (this.avaliacao.equalsIgnoreCase("N/A")) {
            return "N/A";
        }
        return avaliacao;
    }

    public Integer numeroEpisodio() {
        return numeroEpisodio;
    }

    public String temporada() {
        return temporada;
    }

    public String titulo() {
        return titulo;
    }

    public String dataLancamento() {
        if (this.dataLancamento.equalsIgnoreCase("N/A")) {
            return "N/A";
        }
        return dataLancamento;
    }

    public static String formatarData(String dataOriginal) {
        try {
            if(dataOriginal!="N/A") {
                SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
                Date data = formatoEntrada.parse(dataOriginal);

                SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy");
                return formatoSaida.format(data);
            }else{
                return null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Retorna null em caso de erro
        }
    }

    @Override
    public String toString() {

        return "| Episódio: " + numeroEpisodio +
                " | Temporada: " + temporada +
                " | Título: " + titulo +
                " | Avaliação: " + avaliacao +
                " | Data de Lançamento: " +
                (dataLancamento == null ? "N/A" : formatarData(dataLancamento)) +
                " |";
    }
}
