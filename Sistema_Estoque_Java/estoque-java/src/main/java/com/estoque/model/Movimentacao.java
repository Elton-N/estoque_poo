package com.estoque.model;  // CLASSE DE RELACIONAMENTO

import com.estoque.enums.TipoMovimentacao;  // Enum com ENTRADA/SAIDA
import java.time.LocalDateTime;  // Data E hora (não só data)
import java.time.format.DateTimeFormatter;  // Formatador

/**
 * Classe que representa uma Movimentação de estoque
 * Registra cada entrada ou saída de produtos
 * 
 * DEMONSTRA:
 * - Relacionamento entre classes (tem Produto, tem TipoMovimentacao)
 * - Uso de Enum
 * - Trabalho com data/hora
 */
public class Movimentacao {
    
    // Atributos da movimentação
    private String id;                      // ID único da movimentação
    private Produto produto;                // RELACIONAMENTO: qual produto foi movimentado
    private TipoMovimentacao tipo;          // ENUM: ENTRADA ou SAIDA
    private int quantidade;                 // Quantidade movimentada
    private LocalDateTime dataHora;         // Data E hora da movimentação
    private String observacao;              // Observação/motivo da movimentação
    
    // Constantes da classe
    private static final int DIAS_EMPRESTIMO = 14;  // Não usado, pode remover
    // Formatador para exibir data e hora no formato brasileiro
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * CONSTRUTOR
     * Cria uma nova movimentação
     * A data/hora é automaticamente definida como AGORA
     */
    public Movimentacao(String id, Produto produto, TipoMovimentacao tipo, 
                       int quantidade, String observacao) {
        this.id = id;
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataHora = LocalDateTime.now();  // .now() = pega data/hora ATUAL
        this.observacao = observacao;
    }

    // ========== GETTERS ==========
    // Não tem setters porque movimentação não deve ser alterada depois de criada
    // É um REGISTRO histórico
    
    public String getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getObservacao() {
        return observacao;
    }

    /**
     * Exibe todas as informações da movimentação
     */
    public void exibirInformacoes() {
        System.out.println("\n=== MOVIMENTAÇÃO ===");
        System.out.println("ID: " + id);
        // .getDescricao() = pega a descrição do enum ("Entrada" ou "Saída")
        System.out.println("Tipo: " + tipo.getDescricao());
        // .getNome() e .getId() = acessa métodos do objeto Produto
        System.out.println("Produto: " + produto.getNome() + " (ID: " + produto.getId() + ")");
        System.out.println("Quantidade: " + quantidade);
        // .format() = converte LocalDateTime para String formatada
        System.out.println("Data/Hora: " + dataHora.format(formatter));
        System.out.println("Observação: " + observacao);
    }

    /**
     * Converte movimentação em texto resumido
     * Usado em listagens
     */
    @Override
    public String toString() {
        // String.format com placeholders:
        // %s = String, %d = número inteiro
        return String.format("ID: %s | %s | %s | Qtd: %d | %s",
                id, tipo.getDescricao(), produto.getNome(), quantidade,
                dataHora.format(formatter));
    }
}