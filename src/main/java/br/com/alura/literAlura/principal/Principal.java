package br.com.alura.literAlura.principal;

import br.com.alura.literAlura.model.Autor;
import br.com.alura.literAlura.model.DadosLivro;
import br.com.alura.literAlura.model.DadosResposta;
import br.com.alura.literAlura.model.Livro;
import br.com.alura.literAlura.repository.AutorRepository;
import br.com.alura.literAlura.repository.LivroRepository;
import br.com.alura.literAlura.service.ConsumoApi;
import br.com.alura.literAlura.service.ConverteDados;
import java.util.*;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books/?search=";

    private LivroRepository repositorioLivro;
    private AutorRepository repositorioAutor;

    private List<Livro> livros = new ArrayList<>();
    private List<Autor> autores = new ArrayList<>();

    public Principal(LivroRepository repositorioLivro, AutorRepository repositorioAutor) {
        this.repositorioLivro = repositorioLivro;
        this.repositorioAutor = repositorioAutor;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    
                    -------------------------------------------
                    1 - Buscar livros pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em determinado ano
                    5 - Listar livros em um determinado idioma
                    6 - Listar top 10 livros por downloads
                    
                    0 - Sair
                    -------------------------------------------
                    """;

            System.out.print(menu);
            System.out.print("Escolha uma opção: ");

            try {
                opcao = leitura.nextInt();
                leitura.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Opção inválida. Digite um número.");
                leitura.nextLine();
                continue;
            }

            switch (opcao) {
                case 1 -> buscarLivroTitulo();
                case 2 -> listarLivros();
                case 3 -> listarAutores();
                case 4 -> listarAutoresVivosData();
                case 5 -> listarLivrosIdioma();
                case 6 -> listarTop10Livros();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void buscarLivroTitulo() {
        DadosLivro dados = getDadosLivro();
        
        if (dados == null) {
            System.out.println("Livro não encontrado na API.");
            return;
        }

        if (!checaLivroExiste(dados.titulo())) {
            Livro livro = new Livro(dados);
            
            Optional<Autor> autorBusca = repositorioAutor.findByNomeContainingIgnoreCase(dados.autores().get(0).nome());
            
            if (autorBusca.isPresent()) {
                livro.setAutor(autorBusca.get());
            } else {
                Autor novoAutor = new Autor(dados.autores().get(0));
                repositorioAutor.save(novoAutor);
                livro.setAutor(novoAutor);
            }

            repositorioLivro.save(livro);
            System.out.println("\n--- LIVRO ENCONTRADO E SALVO ---");
            System.out.println(livro);
        } else {
            System.out.println("\n[AVISO] Livro já existente no banco de dados.");
        }
    }

    private DadosLivro getDadosLivro() {
        System.out.println("\nDigite o nome do livro:");
        var nomeLivro = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeLivro.replace(" ", "%20"));
        DadosResposta dados = conversor.obterDados(json, DadosResposta.class);

        if (dados.resultados() != null && !dados.resultados().isEmpty()) {
            return dados.resultados().get(0);
        }
        return null;
    }

    private void listarLivros() {
        System.out.println("\n======= LIVROS NO BANCO =======");
        livros = repositorioLivro.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    private void listarAutores() {
        System.out.println("\n======= AUTORES NO BANCO =======");
        autores = repositorioAutor.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor cadastrado.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivosData() {
        System.out.println("\nDigite o ano para a busca:");
        try {
            Integer ano = leitura.nextInt();
            leitura.nextLine();
            List<Autor> autoresVivos = repositorioAutor.findByFalescimentoGreaterThan(ano);
            
            if (autoresVivos.isEmpty()) {
                System.out.println("Nenhum autor vivo encontrado no ano de " + ano);
            } else {
                System.out.println("\n--- Autores vivos em " + ano + " ---");
                autoresVivos.forEach(System.out::println);
            }
        } catch (InputMismatchException e) {
            System.out.println("Ano inválido.");
            leitura.nextLine();
        }
    }

    private void listarLivrosIdioma() {
        System.out.println("\nDigite o idioma (ex: en, pt, fr):");
        var idioma = leitura.nextLine();
        List<Livro> livrosIdioma = repositorioLivro.findByIdiomaContainingIgnoreCase(idioma);
        
        if (livrosIdioma.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma: " + idioma);
        } else {
            System.out.println("\n--- Livros em " + idioma + " ---");
            livrosIdioma.forEach(System.out::println);
        }
    }

    private void listarTop10Livros() {
        System.out.println("\n--- TOP 10 LIVROS MAIS BAIXADOS ---");
        List<Livro> topLivros = repositorioLivro.findTop10ByOrderByNumeroDownloadsDesc();
        topLivros.forEach(System.out::println);
    }

    private boolean checaLivroExiste(String nomeLivro) {
        return repositorioLivro.findByTituloContainingIgnoreCase(nomeLivro).isPresent();
    }
}