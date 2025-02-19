package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.enums.Categoria;
import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.EpisodioRepository;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import tech.tablesaw.api.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private final SerieRepository serieRepository;
    private final EpisodioRepository episodioRepository;
    private final Scanner lerString = new Scanner(System.in);
    private final Scanner lerInt = new Scanner(System.in);
    private final Scanner lerDouble = new Scanner(System.in);
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();

    public Main(SerieRepository serieRepository, EpisodioRepository episodioRepository) {
        this.serieRepository = serieRepository;
        this.episodioRepository = episodioRepository;
    }

    public void consultarAPI() {
        int opcao;
        do {
            System.out.println("\nDeseja pesquisar alguma série? (1-SIM | 0-NÃO)");
            opcao = lerInt.nextInt();

            switch (opcao) {
                case 1:
                    menu1();
                    break;
                case 0:
                    menu2();
                    break;
                default:
                    System.out.println("\nOpção inválida.");
                    break;
            }
        } while (opcao != 0);
    }

    private void menu1() {
        int opcao;
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
                            for (i = 0; i < temporadas.size(); i++) {
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
    }

    private void menu2() {
        int opcao;
        do {
            System.out.println("\n<Menu>");
            System.out.println("1  -> Apresentar todas as séries pesquisadas.");
            System.out.println("2  -> Apresentar todas as séries que um determinado ator trabalhou.");
            System.out.println("3  -> Apresentar as top 5 melhores séries.");
            System.out.println("4  -> Apresentar séries por categoria.");
            System.out.println("5  -> Filtrar por total de temporadas e avaliação.");
            System.out.println("6  -> Apresentar episódio por trecho do título.");
            System.out.println("7  -> Apresentar top 5 melhores episódios de uma série.");
            System.out.println("8  -> Apresentar episódios de uma série a partir de uma data.");
            System.out.println("0  -> Encerrar consulta.");
            System.out.println("\n\nSelecione uma das opções do menu para prosseguir: ");
            opcao = lerInt.nextInt();

            switch (opcao) {
                case 1:
                    apresentarSeriesBuscadas();
                    break;
                case 2:
                    apresentarSeriesPorAtor();
                    break;
                case 3:
                    apresentarTop5Series();
                    break;
                case 4:
                    apresentarSeriesPorCategoria();
                    break;
                case 5:
                    filtrarPorTotalTemporadasEAvaliacao();
                    break;
                case 6:
                    apresentarEpisodioPorTrechoDoTitulo();
                    break;
                case 7:
                    apresentarTop5MelhoresEpisodios();
                    break;
                case 8:
                    apresentarEpisodiosPorData();
                    break;
                case 0:
                    System.out.println("\nEncerrando a consulta.");
                    break;
                default:
                    System.out.println("\nOpção inválida.");
                    break;
            }
        } while (opcao != 0);
    }

    private void verificaUnicidadeSerieNoBD(DadosSerie dados) {
        try {
            // Verifica se já existe uma série com o mesmo título no banco
            Optional<Serie> serieExistente = serieRepository.findByTitulo(dados.titulo());

            // 🔍 Obtém os dados mais recentes da API
            String json = consumoAPI.obterDados(ENDERECO + dados.titulo().replace(" ", "+") + API_KEY);
            DadosSerie dadosAtualizados = conversor.obterDados(json, DadosSerie.class);

            if (serieExistente.isEmpty()) {
                // Se a série não existe no banco, insere com os novos dados
                Serie novaSerie = new Serie(dadosAtualizados);
                serieRepository.save(novaSerie);
                System.out.println("\nSÉRIE INSERIDA NO BANCO: " + novaSerie);
            } else {
                // Se a série já existe, atualiza os dados se necessário
                Serie serieAtualizada = serieExistente.get();

                boolean alterado = false;

                // Verifica e atualiza a categoria (pegando apenas a primeira, se houver múltiplas)
                String primeiraCategoria = dadosAtualizados.categoria().split(",")[0].trim();
                Categoria novaCategoria = Categoria.fromString(primeiraCategoria);
                if (!serieAtualizada.getCategoria().equals(novaCategoria)) {
                    serieAtualizada.setCategoria(novaCategoria);
                    alterado = true;
                }

                // Atualiza a avaliação, verificando se é válida
                try {
                    double novaAvaliacao = Double.parseDouble(dadosAtualizados.avaliacao());
                    if (serieAtualizada.getAvaliacao() != novaAvaliacao) {
                        serieAtualizada.setAvaliacao(novaAvaliacao);
                        alterado = true;
                    }
                } catch (NumberFormatException e) {
                    if (serieAtualizada.getAvaliacao() != 0.0) {
                        serieAtualizada.setAvaliacao(0.0);
                        alterado = true;
                    }
                }

                // Se houve alteração, salva no banco
                if (alterado) {
                    System.out.println("\nDADOS ANTES: " + serieExistente.get());
                    System.out.println("\nDADOS DEPOIS: " + serieAtualizada);
                    serieRepository.save(serieAtualizada);
                    System.out.println("\nSÉRIE ATUALIZADA NO BANCO.");
                }
            }
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro ao verificar a unicidade da série no banco:\n");
            e.printStackTrace();
        }
    }

    public void verificaUnicidadeEpisodioNoBD(String tituloSerie, DadosEpisodios dadosEpisodio) {
        try {
            // Verifica se já existe um episódio com o mesmo título na série
            Optional<Episodio> episodioExistente = episodioRepository.findFirstBySerieTituloAndTituloContainingIgnoreCase(dadosEpisodio.titulo(), tituloSerie);
            Optional<Serie> serieEncontrada = serieRepository.findByTitulo(tituloSerie);

            if (episodioExistente.isEmpty()) {
                // Se o episódio não existir, cria e salva um novo episódio
                Episodio novoEpisodio = new Episodio(dadosEpisodio);
                novoEpisodio.setSerie(serieEncontrada.get());
                episodioRepository.save(novoEpisodio);
                System.out.println("\nNOVO EPISÓDIO INSERIDO NO BANCO: " + novoEpisodio);
            } else {
                // Se o episódio já existir, verifica se há mudanças antes de atualizar
                Episodio episodioAtualizado = episodioExistente.get();

                boolean alterado = false;

                // Atualiza a avaliação, verificando se é válida
                try {
                    double novaAvaliacao = Double.parseDouble(dadosEpisodio.avaliacao());
                    if (episodioAtualizado.getAvaliacao() != novaAvaliacao) {
                        episodioAtualizado.setAvaliacao(novaAvaliacao);
                        alterado = true;
                    }
                } catch (NumberFormatException e) {
                    if (episodioAtualizado.getAvaliacao() != 0.0) {
                        episodioAtualizado.setAvaliacao(0.0);
                        alterado = true;
                    }
                }

                // Se houve alteração, salva no banco e imprime antes/depois
                if (alterado) {
                    System.out.println("\nDADOS ANTES: " + episodioExistente.get());
                    System.out.println("\nDADOS DEPOIS: " + episodioAtualizado);
                    episodioRepository.save(episodioAtualizado);
                    System.out.println("\nEPISÓDIO ATUALIZADO NO BANCO.");
                }
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

    //2
    private void apresentandoTodosOsTitulos(String tituloSerie) {
        try {
            List<String> todosEpisodios = episodioRepository.findDistinctTituloBySerieTitulo(tituloSerie);

            if (todosEpisodios.isEmpty()) {
                System.out.println("Nenhum episódio encontrado para a série: " + tituloSerie);
                return;
            }

            StringColumn titulos = StringColumn.create("Apresentando todos os títulos de episódios",
                    todosEpisodios.stream().toArray(String[]::new));

            Table table = Table.create().addColumns(titulos);
            System.out.println(table);
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //3
    private void utilizandoStreamsELambdas(String tituloSerie) {
        try {
            System.out.println("\n\nUtilizando streams e lambdas: ");
            List<Episodio> todosEpisodiosDeTodasTemporadas = episodioRepository.findDistinctBySerieTitulo(tituloSerie).stream()
                    .toList();
            todosEpisodiosDeTodasTemporadas.forEach(x -> System.out.println(x.toString()));
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //4
    private void apresentandoDezMaisAvaliados(String tituloSerie) {
        try {
            List<Episodio> top10Episodios = episodioRepository.findDistinctTop10BySerieTituloAndAvaliacaoIsNotNullOrderByAvaliacaoDesc(tituloSerie);
            imprimirEpisodios(top10Episodios);
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //5
    private void apresentandoEpisodiosETemporadas(String tituloSerie) {
        try {
            List<Episodio> listaEpisodios = episodioRepository.findDistinctBySerieTitulo(tituloSerie);
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

    //6
    private void apresentarEpisodiosAPartirDeUmAno(String tituloSerie) {
        try {
            System.out.println("\nA partir de que ano você quer ver estes episódios?");
            var ano = lerInt.nextInt();
            lerInt.nextLine();

            System.out.println("\nApresentando episódios a partir do ano de " + ano + ": ");
            LocalDate dataBusca = LocalDate.of(ano, 1, 1);

            boolean nenhumResultado = episodioRepository.findDistinctBySerieTitulo(tituloSerie).stream()
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

    //7
    private void apresentarTemporadaPorEpisodio(String tituloSerie) {
        try {
            System.out.println("\nDigite o título completo ou o trecho de um episódio: ");
            var tituloEpisodio = lerString.nextLine();

            //Optional é utilizado quando os valores podem ou não serem retornados
            Optional<Episodio> episodioBuscado = episodioRepository.findFirstBySerieTituloAndTituloContainingIgnoreCase(tituloSerie, tituloEpisodio);
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

    //8
    private void apresentarAvaliacoesPorTemporada(String tituloSerie) {
        try {
            //Apresenta a média de avaliação por temporada
            Map<Integer, Double> avaliacaoPorTemporada = episodioRepository.findDistinctBySerieTitulo(tituloSerie).stream()
                    .filter(e -> e.getAvaliacao() != null && e.getAvaliacao() > 0.0)
                    .collect(Collectors.groupingBy(Episodio::getTemporada,
                            Collectors.averagingDouble(Episodio::getAvaliacao)));
            System.out.println(avaliacaoPorTemporada);
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //9
    private void apresentarEstatisticas(String tituloSerie) {
        try {
            //Apresenta estatísticas
            DoubleSummaryStatistics est = episodioRepository.findDistinctBySerieTitulo(tituloSerie).stream()
                    .filter(e -> e.getAvaliacao() != null && e.getAvaliacao() > 0.0)
                    .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
            imprimirEstatisticas(est);
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //10
    private void apresentarSeriesBuscadas() {
        try {
            List<Serie> series = serieRepository.findAll();
            imprimirSeries(series);
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //11
    private void apresentarSeriesPorAtor() {
        try {
            System.out.println("\nDigite o nome do ator: ");
            var nomeAtor = lerString.nextLine();

            System.out.println("\nNOME DO ATOR: " + nomeAtor);

            System.out.println("\nDigite a avaliação das séries: ");
            var avaliacaoSerie = lerDouble.nextDouble();

            //Apresentando series que o ator trabalhou com a avaliação maior que o valor inserido
            List<Serie> series = serieRepository.findDistinctByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacaoSerie);

            if (!series.isEmpty()) {
                imprimirSeries(series);
            } else {
                System.out.println("\nNenhuma série foi encontrada.");
            }

        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //12
    private void apresentarTop5Series() {
        try {
            List<Serie> series = serieRepository.findDistinctTop5ByOrderByAvaliacaoDesc();
            imprimirSeries(series);
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //13
    private void apresentarSeriesPorCategoria() {
        try {
            System.out.println("\nDigite a categoria de filmes:");
            var categoriaDigitada = lerString.nextLine();

            Categoria categoria = Categoria.fromString(categoriaDigitada);

            // Verifica se a categoria não é 'DESCONHECIDO' ou inválida
            if (!categoria.equals(Categoria.DESCONHECIDO)) {
                List<Serie> series = serieRepository.findDistinctByCategoria(categoria);
                if (!series.isEmpty()) {
                    imprimirSeries(series);
                } else {
                    System.out.println("\nNão há séries cadastradas com essa categoria.");
                }
            } else {
                System.out.println("\nCategoria desconhecida.");
            }
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //14
    private void filtrarPorTotalTemporadasEAvaliacao() {
        try {
            System.out.println("\nDigite a quantia de temporadas que deve ser igual ou menor:");
            var totalTemporadas = lerInt.nextInt();

            System.out.println("\nDigite a avaliação mínima:");
            var avaliacao = lerDouble.nextDouble();

            List<Serie> series = serieRepository.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(totalTemporadas, avaliacao);

            if (!series.isEmpty()) {
                imprimirSeries(series);
            } else {
                System.out.println("\nNão há séries cadastradas com essa categoria.");
            }

        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    //15
    private void apresentarEpisodioPorTrechoDoTitulo() {
        try {
            System.out.println("\nDigite o nome do episódio: ");
            var nome = lerString.nextLine();

            List<Episodio> episodios = episodioRepository.findByTrechoTitulo(nome);

            if (!episodios.isEmpty()) {
                imprimirEpisodios(episodios);
            } else {
                System.out.println("\nNenhum episódio foi encontrado.");
            }

        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }


    //16
    private void apresentarTop5MelhoresEpisodios() {
        try {
            System.out.println("\nDigite o título da série: ");
            var tituloSerie = lerString.nextLine();

            List<Episodio> episodios = episodioRepository.findDistinctTop5BySerieTituloAndAvaliacaoIsNotNullOrderByAvaliacaoDesc(tituloSerie);

            if (!episodios.isEmpty()) {
                imprimirEpisodios(episodios);
            } else {
                System.out.println("\nNenhum episódio foi encontrado.");
            }

        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }

    }

    //17
    private void apresentarEpisodiosPorData() {
        try {
            System.out.println("\nDigite o título da série: ");
            var tituloSerie = lerString.nextLine();

            System.out.println("\nDigite a data (formato dd-MM-yyyy): ");
            String dataInput = lerString.nextLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            try {
                LocalDate dataLancamento = LocalDate.parse(dataInput, formatter);

                List<Episodio> episodios = episodioRepository.findDistinctBySerieTituloAndDataLancamentoGreaterThanEqual(tituloSerie, dataLancamento);

                if (!episodios.isEmpty()) {
                    imprimirEpisodios(episodios);
                } else {
                    System.out.println("\nNenhum episódio foi encontrado.");
                }

            } catch (java.time.format.DateTimeParseException ex) {
                System.out.println("\nErro: A data fornecida está em um formato inválido. " +
                        "Por favor, utilize o formato dd-MM-yyyy.");
            }

        } catch (Exception e) {
            System.out.println("\nOcorreu um erro durante a consulta: \n");
            e.printStackTrace();
        }
    }

    private void imprimirSeries(List<Serie> series) {
        StringColumn nomes = StringColumn.create("Nome", series.stream().map(Serie::getTitulo).toArray(String[]::new));
        StringColumn categorias = StringColumn.create("Categoria", series.stream().map(serie -> serie.getCategoria().name()).toArray(String[]::new));
        StringColumn atores = StringColumn.create("Atores", series.stream().map(Serie::getAtores).toArray(String[]::new));
        IntColumn totalTemporadas = IntColumn.create("Total de Temporadas", series.stream().map(Serie::getTotalTemporadas).toArray(Integer[]::new));
        DoubleColumn mediaAvaliacao = DoubleColumn.create("Média de Avaliação", series.stream().map(Serie::getAvaliacao).toArray(Double[]::new));
        Table table = Table.create("Séries do BD:").addColumns(nomes, categorias, atores, totalTemporadas, mediaAvaliacao);
        System.out.println(table);
    }

    public void imprimirEpisodios(List<Episodio> episodios) {
        StringColumn series = StringColumn.create("Série", episodios.stream().map(e -> e.getSerie().getTitulo()).toArray(String[]::new));
        StringColumn titulos = StringColumn.create("Título", episodios.stream().map(Episodio::getTitulo).toArray(String[]::new));
        DateColumn dataLancamento = DateColumn.create("Data de Lançamento", episodios.stream().map(Episodio::getDataLancamento).toArray(LocalDate[]::new));
        IntColumn temporadas = IntColumn.create("Temporada", episodios.stream().map(Episodio::getTemporada).toArray(Integer[]::new));
        DoubleColumn avaliacoes = DoubleColumn.create("Avaliação", episodios.stream().map(Episodio::getAvaliacao).toArray(Double[]::new));
        Table table = Table.create("Episódios do BD:").addColumns(series, titulos, dataLancamento, temporadas, avaliacoes);
        System.out.println(table);
    }

    public void imprimirEstatisticas(DoubleSummaryStatistics est) {
        DoubleColumn media = DoubleColumn.create("Média", est.getAverage());
        DoubleColumn menorAvaliacao = DoubleColumn.create("Menor Avaliação", est.getMin());
        DoubleColumn maiorAvaliacao = DoubleColumn.create("Maior Avaliação", est.getMax());
        LongColumn qtdEps = LongColumn.create("Quantia de episódios avaliados", est.getCount());
        Table table = Table.create("Estatísticas dos episódios:").addColumns(media, menorAvaliacao, maiorAvaliacao, qtdEps);
        System.out.println(table);
    }
}

