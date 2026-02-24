package com.estoque.model; // Classe ABSTRATA que representa um Produto genérico - PARTE - 1

/**
 * 
 * ABSTRATA = não pode ser instanciada diretamente
 * Serve como BASE para outras classes (ProdutoSimples, ProdutoPerecivel)
 * 
 * DEMONSTRA:
 * - HERANÇA: outras classes vão herdar desta
 * - ABSTRAÇÃO: define estrutura comum
 * - POLIMORFISMO: implementa interface Estocavel
 * - ENCAPSULAMENTO: atributos protegidos
 */
public abstract class Produto implements Estocavel {
    
    // PROTECTED: Atributos protegidos
    // Podem ser acessados por esta classe E por classes filhas (que herdam)
    // Não podem ser acessados de fora da hierarquia de herança
    
    protected String id;                 // ID único do produto (ex: PRD0001)
    protected String nome;               // Nome do produto
    protected String descricao;          // Descrição detalhada
    protected double preco;              // Preço unitário
    protected int quantidadeEstoque;     // Quantidade atual em estoque
    protected int estoqueMinimo;         // Quantidade mínima para alerta
    protected Categoria categoria;       // Categoria à qual pertence

    // CONSTRUTOR: Inicializa um produto com todos os dados necessários
    // Será chamado pelas classes filhas usando "super(...)"
    public Produto(String id, String nome, String descricao, double preco, 
                   int quantidadeEstoque, int estoqueMinimo, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.estoqueMinimo = estoqueMinimo;
        this.categoria = categoria;
    }

    // ========== GETTERS E SETTERS ==========
    // Métodos de acesso controlado aos atributos
    // ENCAPSULAMENTO: controlam como os dados são lidos e modificados
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // ========== IMPLEMENTAÇÃO DA INTERFACE ESTOCAVEL ==========
    // @Override = indica que estamos implementando métodos da interface
    
    /**
     * Adiciona quantidade ao estoque
     * Implementação da interface Estocavel
     */
    @Override
    public void adicionarQuantidade(int quantidade) {
        // += significa: quantidadeEstoque = quantidadeEstoque + quantidade
        this.quantidadeEstoque += quantidade;
    }

    /**
     * Remove quantidade do estoque
     * Lança exceção se não houver estoque suficiente
     * Implementação da interface Estocavel
     */
    @Override
    public void removerQuantidade(int quantidade) throws Exception {
        // Validação: verifica se há estoque suficiente
        if (quantidade > quantidadeEstoque) {
            // throw = lançar/disparar um erro
            // Interrompe a execução e retorna mensagem de erro
            throw new Exception("Quantidade insuficiente em estoque! Disponível: " + quantidadeEstoque);
        }
        // Se passou na validação, remove a quantidade
        this.quantidadeEstoque -= quantidade;  // -= é o mesmo que: = quantidadeEstoque - quantidade
    }

    /**
     * Retorna a quantidade disponível
     * Implementação da interface Estocavel
     */
    @Override
    public int getQuantidadeDisponivel() {
        return quantidadeEstoque;
    }

    /**
     * Verifica se tem algum produto em estoque
     * Implementação da interface Estocavel
     */
    @Override
    public boolean temEstoqueDisponivel() {
        // Retorna true se quantidade > 0, false caso contrário
        return quantidadeEstoque > 0;
    }

    /**
     * Verifica se o produto precisa de reposição
     * Compara quantidade atual com estoque mínimo
     * Implementação da interface Estocavel
     */
    @Override
    public boolean precisaReposicao() {
        // <= significa "menor ou igual"
        // Retorna true se estoque atual está menor ou igual ao mínimo
        return quantidadeEstoque <= estoqueMinimo;
    }

    // ========== MÉTODO ABSTRATO ==========
    // abstract = não tem implementação aqui
    // OBRIGA as classes filhas a implementarem
    // Cada tipo de produto retornará seu próprio tipo
    public abstract String getTipo();

    /**
     * Calcula o valor total dos produtos em estoque
     * Multiplica preço unitário pela quantidade
     */
    public double getValorTotalEstoque() {
        return preco * quantidadeEstoque;
    }

    /**
     * Converte o produto em texto para exibição
     * Usado em listas e exibições rápidas
     */
    @Override
    public String toString() {
        // String.format = formata texto com placeholders (%s, %.2f, %d)
        // %s = String, %.2f = número decimal com 2 casas, %d = número inteiro
        return String.format("ID: %s | %s | R$ %.2f | Estoque: %d | Categoria: %s",
                id, nome, preco, quantidadeEstoque, categoria.getNome());
    }

    /**
     * Exibe informações completas do produto
     * Usada para visualização detalhada
     */
    public void exibirInformacoes() {
        System.out.println("\n=== PRODUTO ===");
        System.out.println("ID: " + id);
        System.out.println("Tipo: " + getTipo());  // Chama método abstrato (será implementado pelas filhas)
        System.out.println("Nome: " + nome);
        System.out.println("Descrição: " + descricao);
        // String.format("%.2f") = formata número com 2 casas decimais
        System.out.println("Preço: R$ " + String.format("%.2f", preco));
        System.out.println("Quantidade em Estoque: " + quantidadeEstoque);
        System.out.println("Estoque Mínimo: " + estoqueMinimo);
        System.out.println("Valor Total em Estoque: R$ " + String.format("%.2f", getValorTotalEstoque()));
        System.out.println("Categoria: " + categoria.getNome());
        // Operador ternário: condição ? valorSeVerdadeiro : valorSeFalso
        System.out.println("Status: " + (precisaReposicao() ? "⚠️ REPOSIÇÃO NECESSÁRIA" : "✓ OK"));
    }
}