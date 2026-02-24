package com.estoque.service; // Gerenciamento de Movimentações

import com.estoque.enums.TipoMovimentacao;
import com.estoque.model.Movimentacao;
import com.estoque.model.Produto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe de SERVIÇO para gerenciar Movimentações de estoque
 * Registra entradas e saídas, mantém histórico
 * 
 * DEMONSTRA:
 * - Integração entre classes (trabalha com Produto)
 * - Validações de negócio
 * - Filtros por data e tipo
 */
public class MovimentacaoService {
    
    // Lista que armazena todas as movimentações
    private List<Movimentacao> movimentacoes;
    
    // Controla próximo ID
    private int proximoId;

    /**
     * CONSTRUTOR
     * Inicializa lista vazia
     */
    public MovimentacaoService() {
        this.movimentacoes = new ArrayList<>();
        this.proximoId = 1;
    }

    /**
     * Registra uma ENTRADA de produtos no estoque
     * Aumenta a quantidade do produto
     * 
     * @param produto Produto que está entrando
     * @param quantidade Quantidade a adicionar
     * @param observacao Motivo da entrada (ex: "Compra fornecedor X")
     * @return Movimentacao criada
     */
    public Movimentacao registrarEntrada(Produto produto, int quantidade, String observacao) {
        // Gera ID único no formato MOV00001, MOV00002, etc.
        String id = "MOV" + String.format("%05d", proximoId++);
        
        // Cria objeto Movimentacao
        // TipoMovimentacao.ENTRADA = constante do enum
        Movimentacao mov = new Movimentacao(id, produto, TipoMovimentacao.ENTRADA, 
                                            quantidade, observacao);
        
        // ATUALIZA O ESTOQUE DO PRODUTO
        // produto.adicionarQuantidade() = método da interface Estocavel
        produto.adicionarQuantidade(quantidade);
        
        // Adiciona à lista de movimentações (histórico)
        movimentacoes.add(mov);
        
        // Retorna a movimentação criada
        return mov;
    }

    /**
     * Registra uma SAÍDA de produtos do estoque
     * Diminui a quantidade do produto
     * Pode lançar exceção se não houver estoque suficiente
     * 
     * @param produto Produto que está saindo
     * @param quantidade Quantidade a remover
     * @param observacao Motivo da saída (ex: "Venda", "Perda", etc.)
     * @return Movimentacao criada
     * @throws Exception Se não houver estoque suficiente
     */
    public Movimentacao registrarSaida(Produto produto, int quantidade, String observacao) throws Exception {
        // VALIDAÇÃO: tenta remover do estoque
        // Se não houver quantidade suficiente, lança Exception
        // throws Exception = este método PODE gerar erro
        produto.removerQuantidade(quantidade);
        
        // Se chegou aqui, a remoção foi bem-sucedida
        // Gera ID
        String id = "MOV" + String.format("%05d", proximoId++);
        
        // Cria movimentação de SAÍDA
        Movimentacao mov = new Movimentacao(id, produto, TipoMovimentacao.SAIDA, 
                                            quantidade, observacao);
        
        // Adiciona ao histórico
        movimentacoes.add(mov);
        
        // Retorna movimentação criada
        return mov;
    }

    /**
     * Busca movimentação por ID
     * 
     * @param id ID da movimentação
     * @return Optional com movimentação ou vazio
     */
    public Optional<Movimentacao> buscarPorId(String id) {
        return movimentacoes.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    /**
     * Lista todas as movimentações de um produto específico
     * Histórico completo do produto
     * 
     * @param produtoId ID do produto
     * @return Lista de movimentações do produto
     */
    public List<Movimentacao> listarPorProduto(String produtoId) {
        return movimentacoes.stream()
                // m.getProduto() = retorna objeto Produto
                // .getId() = pega ID do produto
                .filter(m -> m.getProduto().getId().equals(produtoId))
                .collect(Collectors.toList());
    }

    /**
     * Lista movimentações por tipo (ENTRADA ou SAIDA)
     * 
     * @param tipo TipoMovimentacao.ENTRADA ou TipoMovimentacao.SAIDA
     * @return Lista de movimentações do tipo especificado
     */
    public List<Movimentacao> listarPorTipo(TipoMovimentacao tipo) {
        return movimentacoes.stream()
                // m.getTipo() retorna o enum
                // == compara enums diretamente (são constantes)
                .filter(m -> m.getTipo() == tipo)
                .collect(Collectors.toList());
    }

    /**
     * Lista movimentações em um período de datas
     * 
     * @param dataInicio Data inicial do período
     * @param dataFim Data final do período
     * @return Lista de movimentações no período
     */
    public List<Movimentacao> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        // Converte LocalDate para LocalDateTime
        // .atStartOfDay() = 00:00:00 do dia
        LocalDateTime inicio = dataInicio.atStartOfDay();
        // .atTime(23, 59, 59) = 23:59:59 do dia
        LocalDateTime fim = dataFim.atTime(23, 59, 59);
        
        return movimentacoes.stream()
                // !isBefore = NÃO é antes (ou seja, é depois ou igual)
                // !isAfter = NÃO é depois (ou seja, é antes ou igual)
                // Resultado: está ENTRE as datas
                .filter(m -> !m.getDataHora().isBefore(inicio) && 
                            !m.getDataHora().isAfter(fim))
                .collect(Collectors.toList());
    }

    /**
     * Lista todas as movimentações
     * 
     * @return Todas as movimentações registradas
     */
    public List<Movimentacao> listarTodas() {
        return new ArrayList<>(movimentacoes);
    }

    /**
     * Lista as últimas N movimentações
     * Útil para ver movimentações recentes
     * 
     * @param quantidade Quantas movimentações retornar
     * @return Últimas movimentações
     */
    public List<Movimentacao> listarUltimas(int quantidade) {
        int total = movimentacoes.size();  // Total de movimentações
        
        // Math.max(0, ...) = garante que não seja negativo
        // Se pediu 10 mas só tem 5, retorna as 5
        int inicio = Math.max(0, total - quantidade);
        
        // .subList(inicio, fim) = sublista de uma posição até outra
        // Retorna os últimos N elementos
        return new ArrayList<>(movimentacoes.subList(inicio, total));
    }

    /**
     * Retorna quantidade total de movimentações
     * 
     * @return Número total de movimentações
     */
    public int quantidadeTotal() {
        return movimentacoes.size();
    }

    /**
     * Conta quantas movimentações são do tipo ENTRADA
     * 
     * @return Número de entradas
     */
    public int quantidadeEntradas() {
        return (int) movimentacoes.stream()
                .filter(m -> m.getTipo() == TipoMovimentacao.ENTRADA)
                .count();  // .count() = conta quantos elementos
    }

    /**
     * Conta quantas movimentações são do tipo SAIDA
     * 
     * @return Número de saídas
     */
    public int quantidadeSaidas() {
        return (int) movimentacoes.stream()
                .filter(m -> m.getTipo() == TipoMovimentacao.SAIDA)
                .count();
    }
}