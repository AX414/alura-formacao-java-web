package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.EpisodioRepository;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private final SerieRepository serieRepository;
    private final EpisodioRepository episodioRepository;
    private Scanner lerString = new Scanner(System.in);
    private Scanner lerInt = new Scanner(System.in);
    private Scanner lerDouble = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    public Main(SerieRepository serieRepository, EpisodioRepository episodioRepository) {
        this.serieRepository = serieRepository;
        this.episodioRepository = episodioRepository;
    }

    public void consultarAPI() {
        int opcao;
        do {
            System.out.println("\nDeseja pesquisar alguma série? (1-SIM | 0-NÃO)");
            opcao = lerInt.nextInt();

            if (opcao != 0) {
                System.out.println("\nDigite o nome da série para efetuar a consulta: ");
                var nome = lerString.nextLine();
                var json = consumoAPI.obterDados(ENDERECO + nome.replace(" ", "+") + API_KEY);

                // Apresenta dados da série
                DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
                if (dados == null) {
                    System.out.println("\nNenhum resultado encontrado.");
                } else {
                    List<DadosTemporada> temporadas = new ArrayList<DadosTemporada>();
                    List<DadosEpisodios> todosEpisodiosDeTodasTemporadas = new ArrayList<DadosEpisodios>();
                    List<Episodio> episodios = new ArrayList<Episodio>();

                    verificaUnicidadeSerieNoBD(dados);

                    do {
                        System.out.println("\n<Menu>");
                        System.out.println("1  -> Apresentar todos os episódios e temporadas.");
                        System.out.println("2  -> Apresentar todos os títulos de episódios.");
                        System.out.println("3  -> Apresentar utilizando streams e lambdas.");
                        System.out.println("4  -> Apresentar os dez mais avaliados.");
                        System.out.println("5  -> Apresentar episódios e temporadas.");
                        System.out.println("6  -> Apresentar episódios a partir de um ano.");
                        System.out.println("7  -> Apresentar temporada por um episódio.");
                        System.out.println("8  -> Apresentar média de avaliações por temporada.");
                        System.out.println("9  -> Apresentar estatísticas.");
                        System.out.println("0  -> Encerrar consulta.");
                        System.out.println("\n\nSelecione uma das opções do menu para prosseguir: ");
                        opcao = lerInt.nextInt();

                        switch (opcao) {
                            case 1:

                                temporadas = apresentandoTodosOsEpisodiosETemporadas(temporadas, json, nome, dados);

                                if (temporadas != null) {
                                    int i = 0, j = 0;

                                    //Para cada temporada
                                    for(i = 0; i < temporadas.size(); i++) {
                                        //Verifique cada episódio
                                        for (j = 0; j < temporadas.get(i).episodios().size(); j++) {
                                            verificaUnicidadeEpisodioNoBD(dados.titulo(), temporadas.get(i).episodios().get(j));
                                        }
                                    }
                                }

                                break;
                            case 2:
                                apresentandoTodosOsTitulos(dados.titulo());
                                break;
                            case 3:
                                utilizandoStreamsELambdas(dados.titulo());
                                break;
                            case 4:
                                apresentandoDezMaisAvaliados(dados.titulo());
                                break;
                            case 5:
                                apresentandoEpisodiosETemporadas(dados.titulo());
                                break;
                            case 6:
                                apresentarEpisodiosAPartirDeUmAno(dados.titulo());
                                break;
                            case 7:
                                apresentarTemporadaPorEpisodio(dados.titulo());
                                break;
                            case 8:
                                apresentarAvaliacoesPorTemporada(dados.titulo());
                                break;
                            case 9:
                                apresentarEstatisticas(dados.titulo());
                                break;
                            case 0:
                                System.out.println("\nEncerrando a consulta.");
                                consultarAPI();
                                break;
                            default:
                                System.out.println("\nOpção inválida.");
                                break;
                        }
                    } while (opcao != 0);
                }
            }else {
                System.out.println("\n<Menu>");
                System.out.println("1 -> Apresentar todas as séries pesquisadas.");
                System.out.println("2 -> Apresentar todas as séries que um determinado ator trabalhou.");
                System.out.println("0  -> Encerrar consulta.");
                System.out.println("\n\nSelecione uma das opções do menu para prosseguir: ");
                opcao = lerInt.nextInt();

                switch (opcao){
                    case 1:
                        listarSeriesBuscadas();
                        break;
                    case 2:
                        listarSeriesPorAtor();
                        break;
                    case 0:
                        System.out.println("\nEncerrando a consulta.");
                        break;
                    default:
                        System.out.println("\nOpção inválida.");
                        break;
                }
            }
        } while (opcao != 0);
    }

    private void verificaUnicidadeSerieNoBD(DadosSerie dados) {
        try {
            // Verifica se já existe uma série com o mesmo título
            Optional<Serie> serieExistente = serieRepository.findByTitulo(dados.titulo());

            if (serieExistente.isEmpty()) {
                // Se não existir, salva a nova série
                Serie serie = new Serie(dados);
                serieRepository.save(serie);
            }
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro ao verificar a unicidade da série no banco:\n");
            e.printStackTrace();
        }
    }

    public void verificaUnicidadeEpisodioNoBD(String tituloSerie, DadosEpisodios dadosEpisodio) {
        try {
            Optional<Episodio> episodioExistente = episodioRepository.findByTituloContainingIgnoreCase(dadosEpisodio.titulo(), tituloSerie);
            Optional<Serie> serieEncontrada = serieRepository.findByTitulo(tituloSerie);

            if (episodioExistente.isEmpty()) {
                // Se não existir, salva a nova série
                Episodio ep = new Episodio(dadosEpisodio);
                ep.setSerie(serieEncontrada.get());
                episodioRepository.save(ep);
            }
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro ao verificar a unicidade do episódio no banco:\n");
            e.printStackTrace();
        }
    }

    //1
    private List<DadosTemporada> apresentandoTodosOsEpisodiosETemporadas(List<DadosTemporada> temporadas, String json, String nome, DadosSerie dados) {
        try {
            if (dados.totalTemporadas() != null) {
                for (int i = 1; i <= dados.totalTemporadas(); i++) {
                    json = consumoAPI.obterDados(ENDERECO + nome.replace(" ", "+") + "&season=" + i + API_KEY);
                    DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

                    // Preenche a temporada em cada episódio, afinal, pesquisar por season,
                    // no array de episodes, não há o season dentro deles
                    int temporadaAtual = i;
                    List<DadosEpisodios> episodiosCorrigidos = dadosTemporada.episodios().stream()
                            .map(episodio -> new DadosEpisodios(
                                    episodio.titulo(),
                                    episodio.numeroEpisodio(),
                                    String.valueOf(temporadaAtual), // Atribui o número da temporada
                                    episodio.avaliacao(),
                                    episodio.dataLancamento()
                            ))
                            .toList();

                    // Cria um novo objeto DadosTemporada com os episódios corrigidos
                    dadosTemporada = new DadosTemporada(temporadaAtual, episodiosCorrigidos);
                    temporadas.add(dadosTemporada);
                    dadosTemporada.imprimirTemporada();
                }
                return temporadas;
            } else {
                System.out.println("\nA API não conseguiu retornar todas as temporadas, tente outra série.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta:\n");
            e.printStackTrace();
            return null;
        }
    }

    //2 -> BD
    private void apresentandoTodosOsTitulos(String tituloSerie) {
        try {
            List<String> todosEpisodios = episodioRepository.findEpisodioTitulosBySerieTitulo(tituloSerie);
            StringColumn titulos = StringColumn.create("Apresentando todos os títulos de episódios", todosEpisodios.stream().toArray(String[]::new));
            Table table = Table.create().addColumns(titulos);
            System.out.println(table);
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //3 -> BD
    private void utilizandoStreamsELambdas(String tituloSerie) {
        try {
            System.out.println("\n\nUtilizando streams e lambdas: ");
            List<Episodio> todosEpisodiosDeTodasTemporadas = episodioRepository.findAllBySerieTitulo(tituloSerie).stream()
                    .toList();
            todosEpisodiosDeTodasTemporadas.forEach(x -> System.out.println(x.toString()));
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //4 -> BD
    private void apresentandoDezMaisAvaliados(String tituloSerie) {
        try {
            List<Episodio> top10Episodios = episodioRepository.findTop10BySerieTitulo(tituloSerie);
            StringColumn titulos = StringColumn.create("Títulos", top10Episodios.stream().map(Episodio::getTitulo).toArray(String[]::new));
            IntColumn temporadas = IntColumn.create("Temporadas", top10Episodios.stream().map(Episodio::getTemporada).toArray(Integer[]::new));
            DoubleColumn avaliacoes = DoubleColumn.create("Avaliação", top10Episodios.stream().map(Episodio::getAvaliacao).toArray(Double[]::new));
            Table table = Table.create().addColumns(titulos, temporadas, avaliacoes);
            System.out.println(table);
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //5 -> BD
    private void apresentandoEpisodiosETemporadas(String tituloSerie) {
        try {
            List<Episodio> listaEpisodios = episodioRepository.findAllBySerieTitulo(tituloSerie);
            StringColumn titulos = StringColumn.create("Títulos", listaEpisodios.stream().map(Episodio::getTitulo).toArray(String[]::new));
            IntColumn temporadas = IntColumn.create("Temporadas", listaEpisodios.stream().map(Episodio::getTemporada).toArray(Integer[]::new));
            DoubleColumn avaliacoes = DoubleColumn.create("Avaliação", listaEpisodios.stream().map(Episodio::getAvaliacao).toArray(Double[]::new));

            // Formatador para exibir as datas no formato desejado
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            StringColumn dataLancamentos = StringColumn.create("Datas de lançamento",
                    listaEpisodios.stream()
                            .map(e -> e.getDataLancamento() != null ? e.getDataLancamento().format(formatter) : "N/A")
                            .toArray(String[]::new));

            Table table = Table.create().addColumns(titulos, temporadas, avaliacoes, dataLancamentos)
                    .sortAscendingOn("Temporadas", "Datas de lançamento");


            System.out.println(table);
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //6 -> BD
    private void apresentarEpisodiosAPartirDeUmAno(String tituloSerie) {
        try {
            System.out.println("\nA partir de que ano você quer ver estes episódios?");
            var ano = lerInt.nextInt();
            lerInt.nextLine();

            System.out.println("\nApresentando episódios a partir do ano de " + ano + ": ");
            LocalDate dataBusca = LocalDate.of(ano, 1, 1);

            boolean nenhumResultado = episodioRepository.findAllBySerieTitulo(tituloSerie).stream()
                    .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                    .peek(System.out::println)
                    .count() == 0;

            if (nenhumResultado) {
                System.out.println("Nenhum episódio encontrado a partir do ano " + ano + ".");
            }
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //7 -> BD
    private void apresentarTemporadaPorEpisodio(String tituloSerie) {
        try {
            System.out.println("\nDigite o título completo ou o trecho de um episódio: ");
            var tituloEpisodio = lerString.nextLine();

            //Optional é utilizado quando os valores podem ou não serem retornados
            Optional<Episodio> episodioBuscado = episodioRepository.findByTituloContainingIgnoreCase(tituloSerie, tituloEpisodio);
            //Procura se existe
            episodioBuscado.ifPresent(episodio -> System.out.println("\nEpisódio encontrado:\n\n| " + episodio.getTitulo() + " | Temporada: " + episodio.getTemporada() + " |"));
            if (episodioBuscado.isEmpty()) {
                System.out.println("\nEpisódio não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //8 -> BD
    private void apresentarAvaliacoesPorTemporada(String tituloSerie) {
        try {
            //Apresenta a média de avaliação por temporada
            Map<Integer, Double> avaliacaoPorTemporada = episodioRepository.findAllBySerieTitulo(tituloSerie).stream()
                    .filter(e -> e.getAvaliacao() != null && e.getAvaliacao() > 0.0)
                    .collect(Collectors.groupingBy(Episodio::getTemporada,
                            Collectors.averagingDouble(Episodio::getAvaliacao)));
            System.out.println("* " + avaliacaoPorTemporada);
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //9 -> BD
    private void apresentarEstatisticas(String tituloSerie) {
        try {
            //Apresenta estatísticas
            DoubleSummaryStatistics est = episodioRepository.findAllBySerieTitulo(tituloSerie).stream()
                    .filter(e -> e.getAvaliacao() != null && e.getAvaliacao() > 0.0)
                    .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
            System.out.println("\nEstatísticas do Double Summary Statistics:" + est);

            //Não é necessário somar todas as avaliações
            //imprimimos apenas o necessário
            System.out.println("\nApresentando as estatísticas que eu me importo:"
                    + "\n* Média: " + est.getAverage()
                    + "\n* Menor Avaliação: " + est.getMin()
                    + "\n* Melhor Avaliação: " + est.getMax()
                    + "\n* Quantidade de episódios avaliados: " + est.getCount());
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //10 -> BD
    private void listarSeriesBuscadas() {
        try {
            List<Serie> series = serieRepository.findAll();
            imprimirSeries(series);
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //11
    private void listarSeriesPorAtor() {
        try {
            System.out.println("\nDigite o nome do ator: ");
            var nomeAtor = lerString.nextLine();

            System.out.println("\nDigite a avaliação das séries: ");
            var avaliacaoSerie = lerDouble.nextDouble();

            //Apresentando series que o ator trabalhou com a avaliação maior que o valor inserido
            List<Serie> series = serieRepository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacaoSerie);
            imprimirSeries(series);
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    private void imprimirSeries(List<Serie> series) {
        // Colunas
        StringColumn nomes = StringColumn.create("Nome", series.stream().map(Serie::getTitulo).toArray(String[]::new));
        StringColumn categorias = StringColumn.create("Categoria", series.stream().map(serie -> serie.getCategoria().name()).toArray(String[]::new));
        StringColumn atores = StringColumn.create("Atores", series.stream().map(Serie::getAtores).toArray(String[]::new));
        IntColumn totalTemporadas = IntColumn.create("Total de Temporadas", series.stream().map(Serie::getTotalTemporadas).toArray(Integer[]::new));
        DoubleColumn mediaAvaliacao = DoubleColumn.create("Média de Avaliação", series.stream().map(Serie::getAvaliacao).toArray(Double[]::new));

        Table table = Table.create("Séries do BD:")
                .addColumns(nomes, categorias, atores, totalTemporadas, mediaAvaliacao)
                .sortAscendingOn("Nome");

        System.out.println(table);
    }
}

