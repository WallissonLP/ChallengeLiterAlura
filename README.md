# Desafio LiterAlura

Projeto desenvolvido no curso de Java e Spring Framework (G9 - ONE) com objetivo de explorar integração com API externa, persistência JPA e interface de console.

---

## 🔍 Visão Geral

Aplicação de linha de comando escrita em Spring Boot que permite pesquisar livros através da API pública **GutenDex**. O primeiro resultado encontrado é armazenado em um banco de dados (PostgreSQL/MySQL) junto ao autor correspondente. O sistema oferece funcionalidades de consulta e listagem baseadas nas informações persistidas.

---

## 🚀 Funcionalidades

- Adicionar livro ao banco pesquisando por título.
- Listar todos os livros registrados.
- Listar todos os autores registrados.
- Filtrar autores vivos em um dado ano.
- Filtrar livros por idioma.
- Exibir top 10 livros por número de downloads.

---

## ⚙️ Requisitos e Uso

1. Banco de dados (PostgreSQL ou MySQL) com base `literalura` criada.
2. Ajustar `src/main/resources/application.properties` conforme credenciais e URL do banco.
3. Compilar e executar usando o wrapper Maven:
   ```bash
   ./mvnw clean package
   java -jar target/*.jar
   ```
4. Ao iniciar, o menu apresentará as opções; digite o número desejado e siga as instruções.

> **Dica:** Variáveis de ambiente (`DB_HOST`, `DB_USER`, `DB_PASSWORD`) também podem ser definidas fora do código (o `main` atual as seta manualmente).

---

## 🧱 Arquitetura e Componentes

O código está dividido em pacotes lógicos:

- `model`: entidades JPA (`Livro`, `Autor`) e records para desserialização de JSON da API (`DadosLivro`, `DadosAutor`, `DadosResposta`).
- `repository`: interfaces Spring Data definindo consultas customizadas.
- `service`: classes auxiliares para realizar requisições HTTP (`ConsumoApi`) e converter JSON (`ConverteDados`).
- `principal`: classe `Principal` que implementa a lógica do menu e coordena as operações.
- `LiterAluraApplication`: ponto de entrada Spring Boot e `CommandLineRunner` que inicia o menu.

---

## 🧠 Fluxo de Execução

1. O `main` define propriedades de conexão e inicia o contexto Spring.
2. `CommandLineRunner` invoca `Principal.exibeMenu()`.
3. Usuário escolhe ação; se for buscar livro, chama API e converte resposta em objetos.
4. Cria-se um `Livro` e associa-se a um `Autor` existente ou novo.
5. Dados são salvos em repositórios JPA (`LivroRepository`, `AutorRepository`).
6. Consultas subsequentes usam métodos dérivados do Spring Data para recuperar informações.

---

## 📁 Estrutura do Menu

```
1 - Buscar livros pelo título
2 - Listar livros registrados
3 - Listar autores registrados
4 - Listar autores vivos em determinado ano
5 - Listar livros em um determinado idioma
6 - Listar top 10 livros por Número de downloads
0 - Sair
```

---

## ⚠️ Observações & Limitações

- A aplicação não valida entradas do usuário; entradas inválidas podem causar exceções.
- Nenhum tratamento de erros de rede ou do banco é implementado.
- Relacionamento entre `Livro` e `Autor` usa `@ManyToOne`/`@OneToMany` com `CascadeType.ALL` e `FetchType.EAGER`.
- Parte do código contém blocos comentados que mostram alternativas de modelagem e consulta.

---

## 📚 Tecnologias

- Java 17 (provável, via wrapper) / Spring Boot
- Spring Data JPA
- Jackson para JSON
- HTTP Client (`java.net.http`)
- MySQL/PostgreSQL

---

## 🧑‍💻 Autor

Wallisson de Lima Parreira

---