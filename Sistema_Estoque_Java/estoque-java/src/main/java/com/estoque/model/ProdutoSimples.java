package com.estoque.model; // HERANÇA DE PRODUTO

/**
 * Classe que representa um Produto Simples (padrão)
 * Exemplos: arroz, feijão, detergente
 * 
 * DEMONSTRA HERANÇA:
 * - extends Produto = herda TUDO de Produto
 * - Adiciona atributo específico: codigoBarras
 * - Implementa método abstrato getTipo()
 */
public class ProdutoSimples extends Produto {
    
    // Atributo ESPECÍFICO desta classe
    // Não existe na classe pai (Produto)
    private String codigoBarras;  // Código de barras do produto

    /**
     * CONSTRUTOR
     * Recebe TODOS os dados necessários (próprios + herdados)
     */
    public ProdutoSimples(String id, String nome, String descricao, double preco,
                         int quantidadeEstoque, int estoqueMinimo, Categoria categoria,
                         String codigoBarras) {
        
        // super() = chama o construtor da classe PAI (Produto)
        // Passa os parâmetros que a classe pai precisa
        // REUTILIZAÇÃO DE CÓDIGO: não precisamos reescrever a inicialização
        super(id, nome, descricao, preco, quantidadeEstoque, estoqueMinimo, categoria);
        
        // Inicializa o atributo específico desta classe
        this.codigoBarras = codigoBarras;
    }

    // Getter para código de barras
    public String getCodigoBarras() {
        return codigoBarras;
    }

    // Setter para código de barras
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    /**
     * IMPLEMENTAÇÃO DO MÉTODO ABSTRATO
     * A classe pai (Produto) definiu que este método DEVE existir
     * Aqui fornecemos a implementação específica
     * 
     * @Override = sobrescreve o método da classe pai
     */
    @Override
    public String getTipo() {
        return "Produto Simples";
    }

    /**
     * SOBRESCRITA do método exibirInformacoes()
     * Chama o método da classe pai E adiciona informação extra
     */
    @Override
    public void exibirInformacoes() {
        // super.exibirInformacoes() = chama o método da classe PAI
        // Exibe tudo que Produto já exibe
        super.exibirInformacoes();
        
        // Adiciona informação específica desta classe
        System.out.println("Código de Barras: " + codigoBarras);
    }
}