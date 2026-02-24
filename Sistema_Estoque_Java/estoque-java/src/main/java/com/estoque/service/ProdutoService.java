package com.estoque.service;   // GERENCIAMNETO AVANÇADO

import com.estoque.model.Produto;
import com.estoque.model.ProdutoPerecivel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe de SERVIÇO para gerenciar Produtos
 * Mais complexa que CategoriaService pois lida com diferentes tipos de produtos
 * 
 * DEMONSTRA:
 * - POLIMORFISMO: List<Produto> armazena ProdutoSimples E ProdutoPerecivel
 * - STREAMS API avançados
 * - CASTING e instanceof
 * - Múltiplos filtros e buscas
 */
public class ProdutoService {
    
    // Lista POLIMÓRFICA: pode armazenar ProdutoSimples ou ProdutoPerecivel
    // Ambos são do tipo Produto (classe pai)
    private List<Produto> produtos;
    
    // Controla próximo ID
    private int proximoId;

    /**
     * CONSTRUTOR
     * Inicializa lista vazia
     */
    public ProdutoService() {
    	// Inicializa a lista polimórfica que armazenará objetos do tipo Produto (e suas subclasses)
        this.produtos = new ArrayList<>();
        // Define o valor inicial para o contador de IDs sequenciais
        // O primeiro produto cadastrado receberá o ID 1 (formatado como PRD0001, por exemplo)
        this.proximoId = 1;
    }

    /**
     * Cadastra um produto já criado
     * Diferente do CategoriaService, aqui recebemos o produto pronto
     * Isso permite cadastrar ProdutoSimples OU ProdutoPerecivel
     * 
     * DEMONSTRA POLIMORFISMO:
     * O parâmetro é do tipo Produto (classe pai)
     * Mas pode receber ProdutoSimples ou ProdutoPerecivel (classes filhas)
     * 
     * @param produto Produto a ser cadastrado (pode ser qualquer subclasse)
     */
    public void cadastrar(Produto produto) {
        produtos.add(produto);  // Adiciona à lista
        proximoId++;  // Incrementa contador
    }

    /**
     * Gera o próximo ID disponível
     * Usado pelas outras classes antes de criar um produto
     * 
     * @return String no formato PRD0001, PRD0002, etc.
     */
    public String gerarProximoId() {
        // %04d = formata com 4 dígitos (0001, 0002, ...)
        return "PRD" + String.format("%04d", proximoId);
    }

    /**
     * Busca produto por ID
     * 
     * @param id ID do produto
     * @return Optional com produto ou vazio
     */
    public Optional<Produto> buscarPorId(String id) {
        // STREAMS API:
        // Percorre todos os produtos e retorna o que tem o ID procurado
        return produtos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    /**
     * Busca produtos por nome (busca parcial)
     * Retorna todos que contém o texto no nome
     * 
     * @param nome Nome ou parte do nome
     * @return Lista de produtos encontrados
     */
    public List<Produto> buscarPorNome(String nome) {
        return produtos.stream()
                // Busca case-insensitive (ignora maiúsculas/minúsculas)
                .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Busca produtos de uma categoria específica
     * 
     * @param categoriaId ID da categoria
     * @return Lista de produtos da categoria
     */
    public List<Produto> buscarPorCategoria(String categoriaId) {
        return produtos.stream()
                // p.getCategoria() = retorna objeto Categoria
                // .getId() = pega o ID da categoria
                // Compara com o ID procurado
                .filter(p -> p.getCategoria().getId().equals(categoriaId))
                .collect(Collectors.toList());
    }

    /**
     * Lista todos os produtos
     * 
     * @return Nova lista com todos os produtos
     */
    public List<Produto> listarTodos() {
        // Retorna CÓPIA para proteger lista interna
        return new ArrayList<>(produtos);
    }

    /**
     * Lista produtos com estoque baixo
     * Aqueles que atingiram ou ficaram abaixo do estoque mínimo
     * 
     * DEMONSTRA: Uso de method reference (::)
     * 
     * @return Lista de produtos que precisam reposição
     */
    public List<Produto> listarComEstoqueBaixo() {
        return produtos.stream()
                // Produto::precisaReposicao é o mesmo que:
                // p -> p.precisaReposicao()
                // Method reference = forma abreviada de lambda
                .filter(Produto::precisaReposicao)
                .collect(Collectors.toList());
    }

    /**
     * Lista produtos SEM NENHUM item em estoque
     * Quantidade = 0
     * 
     * @return Lista de produtos esgotados
     */
    public List<Produto> listarSemEstoque() {
        return produtos.stream()
                // ! = NOT (negação)
                // !temEstoqueDisponivel() = NÃO tem estoque
                .filter(p -> !p.temEstoqueDisponivel())
                .collect(Collectors.toList());
    }

    /**
     * Lista produtos perecíveis próximos do vencimento ou vencidos
     * 
     * DEMONSTRA:
     * - instanceof = verifica tipo do objeto
     * - Casting = conversão de tipo
     * - Polimorfismo
     * 
     * @return Lista de produtos perecíveis com alerta de validade
     */
    public List<ProdutoPerecivel> listarPereciveisProximosVencimento() {
        return produtos.stream()
                // FILTER 1: instanceof ProdutoPerecivel
                // Verifica se o produto é do tipo ProdutoPerecivel
                // instanceof = operador que testa tipo de objeto
                .filter(p -> p instanceof ProdutoPerecivel)
                
                // MAP: Casting (conversão de tipo)
                // p é do tipo Produto (genérico)
                // Convertemos para ProdutoPerecivel (específico)
                // (ProdutoPerecivel) p = casting explícito
                .map(p -> (ProdutoPerecivel) p)
                
                // FILTER 2: verificar validade
                // Agora p é ProdutoPerecivel, então tem os métodos específicos
                // Filtra os que estão próximos de vencer OU já vencidos
                .filter(p -> p.proximoDoVencimento() || p.estaVencido())
                
                .collect(Collectors.toList());
    }

    /**
     * Remove um produto
     * 
     * @param id ID do produto
     * @return true se removeu, false se não encontrou
     */
    public boolean remover(String id) {
        return produtos.removeIf(p -> p.getId().equals(id));
    }

    /**
     * Retorna quantidade total de produtos cadastrados
     * 
     * @return Número total de produtos
     */
    public int quantidadeTotal() {
        return produtos.size();
    }

    /**
     * Calcula valor total de todos os produtos em estoque
     * Soma: preço × quantidade de cada produto
     * 
     * DEMONSTRA: Streams com operações matemáticas
     * 
     * @return Valor total em Reais
     */
    public double valorTotalEstoque() {
        return produtos.stream()
                // .mapToDouble() = transforma cada produto em um número double
                // Produto::getValorTotalEstoque = chama o método de cada produto
                // que retorna preço × quantidade
                .mapToDouble(Produto::getValorTotalEstoque)
                // .sum() = soma todos os valores
                .sum();
    }

    /**
     * Retorna quantidade total de ITENS em estoque
     * Soma a quantidade de todos os produtos
     * 
     * Exemplo:
     * - 10 unidades de Arroz
     * - 5 unidades de Feijão
     * Total: 15 itens
     * 
     * @return Número total de itens
     */
    public int quantidadeTotalItens() {
        return produtos.stream()
                // .mapToInt() = transforma em números inteiros
                // Produto::getQuantidadeEstoque = pega quantidade de cada um
                .mapToInt(Produto::getQuantidadeEstoque)
                // .sum() = soma tudo
                .sum();
    }
}