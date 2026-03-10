package br.com.alura.literAlura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;
    private String nomeAutor;
    private String idioma;
    private Integer numeroDownloads;

    @ManyToOne
    @JoinColumn(name = "autor_id", referencedColumnName = "id")
    private Autor autor;

    public Livro(){}

    public Livro (DadosLivro dadosLivro){
        this.titulo = dadosLivro.titulo();
        this.nomeAutor = dadosLivro.autores().isEmpty()
                ? "Desconhecido"
                : dadosLivro.autores().get(0).nome();
        this.idioma = dadosLivro.idiomas().get(0);
        this.numeroDownloads = dadosLivro.nDownloads();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroDownloads() {
        return numeroDownloads;
    }

    public void setNumeroDownloads(Integer nDownloads) {
        this.numeroDownloads = nDownloads;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return  "\n---------- LIVRO ----------" +
                "\nTítulo: " + titulo +
                "\nAutor: " + (autor != null ? autor.getNome() : nomeAutor) +
                "\nIdioma: " + idioma +
                "\nDownloads: " + numeroDownloads +
                "\n---------------------------\n";
    }
}
