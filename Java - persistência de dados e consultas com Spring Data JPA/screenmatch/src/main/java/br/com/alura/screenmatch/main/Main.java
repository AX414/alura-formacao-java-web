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

    private Scanner lerString = new Scanner(System.in);
    private Scanner lerInt = new Scanner(System.in);

    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private final SerieRepository serieRepository;
    private final EpisodioRepository episodioRepository;

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
                if (dados.titulo() == null) {
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
                        System.out.println("5  -> Apresentar episódios e temporadas por um construtor personalizado.");
                        System.out.println("6  -> Apresentar episódios a partir de um ano.");
                        System.out.println("7  -> Apresentar temporada por um episódio.");
                        System.out.println("8  -> Apresentar média de avaliações por temporada.");
                        System.out.println("9  -> Apresentar estatísticas");
                        System.out.println("_______________________________________________________");
                        System.out.println("10 -> Apresentar todas as séries pesquisadas");
                        System.out.println("0  -> Encerrar consulta.");
                        System.out.println("\n\nSelecione uma das opções do menu para prosseguir: ");
                        opcao = lerInt.nextInt();

                        switch (opcao) {
                            case 1:
                                temporadas = apresentandoTodosOsEpisodiosETemporadas(temporadas, json, nome, dados);

                                //Para cada temporada
                                for(int i = 0;i<temporadas.size();i++){
                                    //Verifique cada episódio
                                    for(int j = 0; j<temporadas.get(i).episodios().size();j++){
                                        verificaUnicidadeEpisodioNoBD(dados.titulo(), temporadas.get(i).episodios().get(j));
                                    }
                                }

                                break;
                            case 2:
                                // Não roda se a lista de temporadas não estiver populada
                                if (temporadas.isEmpty())
                                    System.out.println("\nNão é possível acessar essa opção ainda (acesse as opções anteriores primeiro).");
                                else
                                    apresentandoTodosOsTitulos(temporadas);
                                break;
                            case 3:
                                // Não roda se a lista de temporadas não estiver populada
                                if (temporadas.isEmpty())
                                    System.out.println("\nNão é possível acessar essa opção ainda (acesse as opções anteriores primeiro).");
                                else
                                    todosEpisodiosDeTodasTemporadas = utilizandoStreamsELambdas(temporadas);
                                break;
                            case 4:
                                // Não roda se não tiver a lista de todos os eps e todas as temporadas populadas
                                if (temporadas.isEmpty() || todosEpisodiosDeTodasTemporadas.isEmpty())
                                    System.out.println("\nNão é possível acessar essa opção ainda (acesse as opções anteriores primeiro).");
                                else
                                    apresentandoDezMaisAvaliados(todosEpisodiosDeTodasTemporadas);
                                break;
                            case 5:
                                if (temporadas.isEmpty())
                                    System.out.println("\nNão é possível acessar essa opção ainda (acesse as opções anteriores primeiro).");
                                else
                                    episodios = apresentandoEpisodiosETemporadasPorConstrutor(temporadas);
                                break;
                            case 6:
                                if (temporadas.isEmpty() || episodios.isEmpty())
                                    System.out.println("\nNão é possível acessar essa opção ainda (acesse as opções anteriores primeiro).");
                                else
                                    apresentarEpisodiosAPartirDeUmAno(episodios);
                                break;
                            case 7:
                                if (temporadas.isEmpty() || episodios.isEmpty())
                                    System.out.println("\nNão é possível acessar essa opção ainda (acesse as opções anteriores primeiro).");
                                else
                                    apresentarTemporadaPorEpisodio(episodios);
                                break;
                            case 8:
                                if (temporadas.isEmpty() || episodios.isEmpty())
                                    System.out.println("\nNão é possível acessar essa opção ainda (acesse as opções anteriores primeiro).");
                                else
                                    apresentarAvaliacoesPorTemporada(episodios);
                                break;
                            case 9:
                                if (temporadas.isEmpty() || episodios.isEmpty())
                                    System.out.println("\nNão é possível acessar essa opção ainda (acesse as opções anteriores primeiro).");
                                else
                                    apresentarEstatisticas(episodios);
                                break;
                            case 10:
                                System.out.println("\nApresentando todas as séries pesquisadas:");
                                listarSeriesBuscadas();
                                break;
                            case 0:
                                // Ao encerrar a consulta, pergunta novamente se deseja pesquisar outra série
                                System.out.println("\nEncerrando a consulta.");
                                break;
                            default:
                                System.out.println("\nOpção inválida.");
                                break;
                        }
                    } while (opcao != 0);
                }
            } else {
                // Retorna à pergunta inicial para pesquisar outra série ou encerrar
                listarSeriesBuscadas();
                System.out.println("\nDeseja pesquisar outra série?");
            }
        } while (opcao != 0);
    }

    public void verificaUnicidadeSerieNoBD(DadosSerie dados) {
        // Verifica se já existe uma série com o mesmo título
        Optional<Serie> serieExistente = serieRepository.findByTitulo(dados.titulo());

        if (serieExistente.isEmpty()) {
            // Se não existir, salva a nova série
            Serie serie = new Serie(dados);
            serieRepository.save(serie);
        }
    }

    public void verificaUnicidadeEpisodioNoBD(String tituloSerie, DadosEpisodios dadosEpisodio ) {
        Optional<Episodio> episodioExistente = episodioRepository.findByTituloAndSerie_Titulo(dadosEpisodio.titulo(), tituloSerie);
        Optional<Serie> serieEncontrada = serieRepository.findByTitulo(tituloSerie);

        if (episodioExistente.isEmpty()) {
            // Se não existir, salva a nova série
            Episodio ep = new Episodio(dadosEpisodio);
            ep.setSerie(serieEncontrada.get());
            episodioRepository.save(ep);
        }
    }

    //1
    public List<DadosTemporada> apresentandoTodosOsEpisodiosETemporadas(List<DadosTemporada> temporadas, String json, String nome, DadosSerie dados) {
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
    }

    //2
    public void apresentandoTodosOsTitulos(List<DadosTemporada> temporadas) {
        // Apresentar todos os títulos (forma convencional)
        System.out.println("\n\nApresentando todos os títulos de episódios:");
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println("| " + e.titulo() + " |")));
    }

    //3
    public List<DadosEpisodios> utilizandoStreamsELambdas(List<DadosTemporada> temporadas) {
        //Utilizando Streams e lambdas para trabalhar com as temporadas
        System.out.println("\n\nUtilizando streams e lambdas: ");
        List<DadosEpisodios> todosEpisodiosDeTodasTemporadas = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());
        todosEpisodiosDeTodasTemporadas.forEach(System.out::println);

        return todosEpisodiosDeTodasTemporadas;
    }

    //4
    public void apresentandoDezMaisAvaliados(List<DadosEpisodios> todosEpisodiosDeTodasTemporadas) {
        System.out.println("\nApresentando os 10 episódios mais bem avaliados:");
        todosEpisodiosDeTodasTemporadas.stream()
                //Retirando os N/A
                .filter(e -> !String.valueOf(e.avaliacao()).equalsIgnoreCase("N/A"))
                //Peek ajuda a dar uma olhadinha para ver se é o que estávamos esperando
                //.peek(e-> System.out.println("\nPrimeiro filtro (N/A): "+e))
                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                //.peek(e-> System.out.println("\nOrdenação: "+e))
                .limit(10)
                //.peek(e-> System.out.println("\nLimite: "+e))
                .map(e -> e.titulo().toUpperCase())
                //.peek(e-> System.out.println("\nMapeamento: "+e))
                .forEach(e -> System.out.println("* " + e));
    }

    //5
    public List<Episodio> apresentandoEpisodiosETemporadasPorConstrutor(List<DadosTemporada> temporadas) {
        System.out.println("\nApresentando episódios e temporadas por meio de um construtor de episódios da classe Episódio:");
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios()
                        .stream()
                        .map(e -> new Episodio(t.temporada(), e))
                ).collect(Collectors.toList());
        episodios.forEach(System.out::println);
        return episodios;
    }

    //6
    public void apresentarEpisodiosAPartirDeUmAno(List<Episodio> episodios) {

        System.out.println("\nA partir de que ano você quer ver estes episódios?");
        var ano = lerInt.nextInt();
        lerInt.nextLine();

        System.out.println("\nApresentando episódios a partir do ano de " + ano + ": ");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyy");
        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        boolean nenhumResultado = episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .peek(e -> System.out.println("| Temporada: " + e.getTemporada() +
                        " | Episódio: " + e.getNumeroEpisodio() +
                        " | Título: " + e.getTitulo() +
                        " | Avaliação: " + e.getAvaliacao() +
                        " | Data de Lançamento: " + e.getDataLancamento().format(dtf) +
                        " |"))
                .count() == 0;

        if (nenhumResultado) {
            System.out.println("Nenhum episódio encontrado a partir do ano " + ano + ".");
        }
    }

    //7
    public void apresentarTemporadaPorEpisodio(List<Episodio> episodios) {
        System.out.println("\nDigite o título de um episódio: ");
        var tituloEpisodio = lerString.nextLine();
        //Optional é utilizado quando os valores podem ou não serem retornados
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().contains(tituloEpisodio))
                .findFirst();

        //Procura se existe
        episodioBuscado.ifPresent(episodio -> System.out.println("\nEpisódio encontrado, ele pertence à temporada: " + episodio.getTemporada()));
        if (episodioBuscado.isEmpty()) {
            System.out.println("\nEpisódio não encontrado.");
        }
    }

    //8
    public void apresentarAvaliacoesPorTemporada(List<Episodio> episodios) {
        //Apresenta a média de avaliação por temporada
        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() != null && e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println("* " + avaliacaoPorTemporada);
    }

    //9
    public void apresentarEstatisticas(List<Episodio> episodios) {
        //Apresenta estatísticas
        System.out.println("\nApresenta Estatísticas do Double Summary Statistics: ");
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() != null && e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("* " + est);

        //Não é necessário somar todas as avaliações
        //imprimimos apenas o necessário
        System.out.println("\n\nApresentando as estatísticas que eu quero:"
                + "\n* Média: " + est.getAverage()
                + "\n* Menor Avaliação: " + est.getMin()
                + "\n* Melhor Avaliação: " + est.getMax()
                + "\n* Quantidade de episódios avaliados: " + est.getCount());
    }

    //10
    public void listarSeriesBuscadas() {
        List<Serie> series = serieRepository.findAll();

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

