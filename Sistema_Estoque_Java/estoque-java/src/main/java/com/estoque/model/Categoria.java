package com.estoque.model; // CLASSE SIMPLES

/**
 * Classe que representa uma Categoria de produtos
 * Exemplo: Alimentos, Bebidas, Limpeza, etc.
 * 
 * DEMONSTRA: ENCAPSULAMENTO
 * - Atributos privados
 * - Acesso controlado por getters e setters
 */
public class Categoria {
    
    // ENCAPSULAMENTO: Atributos PRIVADOS
    // Só podem ser acessados dentro desta classe
    // "private" = ninguém de fora pode ver ou modificar diretamente
    
    private String id;          // Identificador único (ex: CAT001)
    private String nome;        // Nome da categoria (ex: "Alimentos")
    private String descricao;   // Descrição detalhada

    // CONSTRUTOR: Método especial que cria um objeto Categoria
    // É chamado quando fazemos: new Categoria(...)
    // Recebe os valores iniciais e armazena nos atributos
    public Categoria(String id, String nome, String descricao) {
        this.id = id;              // "this" se refere ao objeto atual
        this.nome = nome;
        this.descricao = descricao;
    }

    // GETTERS: Métodos que RETORNAM (get = pegar) o valor dos atributos
    // Permitem LER os atributos privados de forma controlada
    
    public String getId() {
        return id;  // Retorna o valor do atributo id
    }

    public String getNome() {
        return nome;  // Retorna o valor do atributo nome
    }

    public String getDescricao() {
        return descricao;  // Retorna o valor do atributo descricao
    }

    // SETTERS: Métodos que ALTERAM (set = definir) o valor dos atributos
    // Permitem MODIFICAR os atributos privados de forma controlada
    
    public void setId(String id) {
        this.id = id;  // Altera o valor do atributo id
    }

    public void setNome(String nome) {
        this.nome = nome;  // Altera o valor do atributo nome
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;  // Altera o valor do atributo descricao
    }

    // Método toString() - converte o objeto em texto
    // @Override = sobrescreve o método toString() herdado de Object
    // Usado para exibir o objeto de forma legível
    @Override
    public String toString() {
        // Concatena (junta) os atributos em uma String formatada
        return "ID: " + id + " | " + nome + " - " + descricao;
    }

    // Método personalizado para exibir informações completas
    // void = não retorna nada, apenas imprime na tela
    public void exibirInformacoes() {
        System.out.println("\n=== CATEGORIA ===");
        System.out.println("ID: " + id);
        System.out.println("Nome: " + nome);
        System.out.println("Descrição: " + descricao);
    }
}