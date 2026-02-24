package com.estoque.enums; // ENUM

/**
 * Enum que representa os tipos de movimentação possíveis no estoque
 * Um ENUM é um tipo especial que define um conjunto fixo de constantes
 * Neste caso: apenas ENTRADA ou SAIDA
 */
public enum TipoMovimentacao { // 
    // Constante que representa entrada de produtos no estoque
    ENTRADA("Entrada"),
    
    // Constante que representa saída de produtos do estoque (vendas)
    SAIDA("Saída");

    // Atributo privado que guarda a descrição legível do tipo
    // ENCAPSULAMENTO: atributo privado
    private final String descricao;

    // Construtor do enum - chamado automaticamente ao criar as constantes
    // Recebe a descrição e armazena no atributo
    TipoMovimentacao(String descricao) {
        this.descricao = descricao;
    }

    // Método getter para obter a descrição
    // Permite acessar o atributo privado de forma controlada
    public String getDescricao() {
        return descricao;
    }
}