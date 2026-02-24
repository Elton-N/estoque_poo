package com.estoque.service; // Gerenciamento com Coleção

import com.estoque.model.Categoria;
// Imports para trabalhar com coleções
import java.util.ArrayList;  // Lista dinâmica
import java.util.List;       // Interface List
import java.util.Optional;   // Container que pode ou não ter valor
import java.util.stream.Collectors;  // Utilitários para Streams

/**
 * Classe de SERVIÇO para gerenciar Categorias
 * Contém toda a LÓGICA DE NEGÓCIO relacionada a categorias
 * 
 * DEMONSTRA:
 * - Uso de COLEÇÕES (ArrayList)
 * - Uso de STREAMS API (filter, map, collect)
 * - Uso de OPTIONAL (evita null)
 * - Separação de responsabilidades (Service Layer)
 */
public class CategoriaService {
    
    // COLEÇÃO: Lista que armazena todas as categorias em memória
    // List<Categoria> = lista que só pode conter objetos do tipo Categoria
    // private = só esta classe pode acessar diretamente
    private List<Categoria> categorias;
    
    // Controla o próximo ID a ser gerado
    private int proximoId;

    /**
     * CONSTRUTOR
     * Inicializa a lista vazia e o contador de IDs
     */
    public CategoriaService() {
        // new ArrayList<>() = cria uma lista vazia
        this.categorias = new ArrayList<>();
        this.proximoId = 1;  // Começa em 1
    }

    /**
     * Cadastra uma nova categoria
     * 
     * @param nome Nome da categoria
     * @param descricao Descrição da categoria
     * @return A categoria criada
     */
    public Categoria cadastrar(String nome, String descricao) {
        // Gera ID único no formato CAT001, CAT002, etc.
        // String.format("%03d", numero) = formata número com 3 dígitos (001, 002...)
        String id = "CAT" + String.format("%03d", proximoId++);
        // proximoId++ = usa o valor atual E depois incrementa
        
        // Cria novo objeto Categoria
        Categoria categoria = new Categoria(id, nome, descricao);
        
        // Adiciona à lista
        // .add() = método do ArrayList que adiciona elemento ao final
        categorias.add(categoria);
        
        // Retorna a categoria criada
        return categoria;
    }

    /**
     * Busca categoria por ID
     * 
     * DEMONSTRA STREAMS API e OPTIONAL
     * 
     * @param id ID da categoria
     * @return Optional com a categoria (se encontrada) ou vazio (se não encontrada)
     */
    public Optional<Categoria> buscarPorId(String id) {
        // STREAMS API:
        // categorias.stream() = transforma a lista em stream (fluxo de dados)
        // .filter() = filtra elementos que atendem a condição
        // .findFirst() = retorna o primeiro elemento encontrado
        // 
        // Lambda (c -> ...):
        // c = representa cada categoria da lista
        // c.getId().equals(id) = verifica se o ID da categoria é igual ao procurado
        return categorias.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        // Retorna Optional<Categoria>:
        // - Se encontrou: Optional.of(categoria)
        // - Se não encontrou: Optional.empty()
    }

    /**
     * Busca categorias por nome (busca parcial)
     * 
     * DEMONSTRA STREAMS API com múltiplos resultados
     * 
     * @param nome Nome (ou parte do nome) para buscar
     * @return Lista de categorias que contém o nome buscado
     */
    public List<Categoria> buscarPorNome(String nome) {
        return categorias.stream()
                // .toLowerCase() = converte para minúsculas
                // .contains() = verifica se contém o texto
                // Busca case-insensitive (ignora maiúsculas/minúsculas)
                .filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase()))
                // .collect(Collectors.toList()) = transforma stream de volta em lista
                .collect(Collectors.toList());
    }

    /**
     * Lista todas as categorias
     * 
     * @return Nova lista com todas as categorias
     */
    public List<Categoria> listarTodas() {
        // new ArrayList<>(categorias) = cria CÓPIA da lista
        // Não retorna a lista original para proteger os dados
        // ENCAPSULAMENTO: não expõe a lista interna diretamente
        return new ArrayList<>(categorias);
    }

    /**
     * Atualiza dados de uma categoria
     * 
     * @param id ID da categoria a atualizar
     * @param nome Novo nome
     * @param descricao Nova descrição
     * @return true se atualizou, false se não encontrou
     */
    public boolean atualizar(String id, String nome, String descricao) {
        // Busca a categoria por ID
        Optional<Categoria> categoriaOpt = buscarPorId(id);
        
        // .isPresent() = verifica se o Optional contém valor
        if (categoriaOpt.isPresent()) {
            // .get() = extrai o valor do Optional
            Categoria categoria = categoriaOpt.get();
            
            // Atualiza os dados usando setters
            categoria.setNome(nome);
            categoria.setDescricao(descricao);
            
            return true;  // Sucesso
        }
        return false;  // Não encontrou
    }

    /**
     * Remove uma categoria
     * 
     * @param id ID da categoria a remover
     * @return true se removeu, false se não encontrou
     */
    public boolean remover(String id) {
        // .removeIf() = remove elementos que atendem a condição
        // Lambda: c -> c.getId().equals(id)
        // Remove se o ID for igual ao procurado
        // Retorna true se removeu algo, false se não encontrou
        return categorias.removeIf(c -> c.getId().equals(id));
    }

    /**
     * Retorna quantidade total de categorias cadastradas
     * 
     * @return Número de categorias
     */
    public int quantidadeTotal() {
        // .size() = retorna tamanho da lista
        return categorias.size();
    }
}