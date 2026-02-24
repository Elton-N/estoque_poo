package com.estoque.model; // INTERFACE

/**
 * Interface que define o Comortamento para itens que podem ser estocados
 * POLIMORFISMO: qualquer classe que implementar esta interface
 * DEVE implementar todos estes métodos
 * 
 * Interface = contrato que garante que certas funcionalidades existem
 */
public interface Estocavel {
    
    // Método para adicionar quantidade ao estoque
    // void = não retorna nada, apenas executa a ação
    // int quantidade = recebe um número inteiro como parâmetro
    void adicionarQuantidade(int quantidade);
    
    // Método para remover quantidade do estoque
    // throws Exception = pode lançar um erro se não houver estoque suficiente
    void removerQuantidade(int quantidade) throws Exception;
    
    // Método que retorna a quantidade disponível no estoque
    // int = retorna um número inteiro
    int getQuantidadeDisponivel();
    
    // Método que verifica se tem algum produto em estoque
    // boolean = retorna verdadeiro ou falso
    boolean temEstoqueDisponivel();
    
    // Método que verifica se o produto precisa de reposição
    // Compara estoque atual com estoque mínimo
    boolean precisaReposicao();
}