package com.estoque.model; // HERANÇA ESPECIALIZADA

// Imports necessários para trabalhar com datas
import java.time.LocalDate;  // Classe para representar datas (dia/mês/ano)
import java.time.format.DateTimeFormatter;  // Formatador de datas
import java.time.temporal.ChronoUnit;  // Utilitário para calcular diferenças entre datas

/**
 * Classe que representa um Produto Perecível
 * Exemplos: leite, iogurte, queijo, frutas
 * 
 * DEMONSTRA HERANÇA ESPECIALIZADA:
 * - Herda tudo de Produto
 * - Adiciona atributos específicos: dataValidade e lote
 * - Adiciona métodos específicos: estaVencido(), diasParaVencer()
 * - Comportamento diferenciado de ProdutoSimples
 */
public class ProdutoPerecivel extends Produto {
    
    // Atributos ESPECÍFICOS de produtos perecíveis
    private LocalDate dataValidade;  // Data em que o produto vence
    private String lote;             // Número do lote de fabricação
    
    // Formatador de data para exibir no padrão brasileiro (dd/MM/yyyy)
    // static final = constante compartilhada por todos os objetos
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * CONSTRUTOR
     * Recebe dados herdados + dados específicos de produtos perecíveis
     */
    public ProdutoPerecivel(String id, String nome, String descricao, double preco,
                           int quantidadeEstoque, int estoqueMinimo, Categoria categoria,
                           LocalDate dataValidade, String lote) {
        
        // super() chama o construtor da classe PAI (Produto)
        // Inicializa todos os atributos herdados
        super(id, nome, descricao, preco, quantidadeEstoque, estoqueMinimo, categoria);
        
        // Inicializa atributos específicos desta classe
        this.dataValidade = dataValidade;
        this.lote = lote;
    }

    // ========== GETTERS E SETTERS ==========
    
    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    // ========== IMPLEMENTAÇÃO DE MÉTODO ABSTRATO ==========
    
    /**
     * Retorna o tipo do produto
     * Implementação obrigatória do método abstrato da classe pai
     */
    @Override
    public String getTipo() {
        return "Produto Perecível";
    }

    // ========== MÉTODOS ESPECÍFICOS DE PRODUTOS PERECÍVEIS ==========
    
    /**
     * Verifica se o produto está vencido
     * Compara a data de validade com a data atual
     * 
     * @return true se estiver vencido, false se ainda estiver válido
     */
    public boolean estaVencido() {
        // LocalDate.now() = pega a data de HOJE
        // isAfter() = verifica se uma data é DEPOIS de outra
        // Se HOJE está DEPOIS da validade = produto vencido
        return LocalDate.now().isAfter(dataValidade);
    }

    /**
     * Calcula quantos dias faltam para o produto vencer
     * Pode retornar número negativo se já estiver vencido
     * 
     * @return número de dias até vencer (negativo se já venceu)
     */
    public long diasParaVencer() {
        // ChronoUnit.DAYS.between() = calcula diferença em DIAS entre duas datas
        // between(dataInicio, dataFim)
        // Se resultado é positivo = ainda vai vencer
        // Se resultado é negativo = já venceu (há X dias atrás)
        return ChronoUnit.DAYS.between(LocalDate.now(), dataValidade);
    }

    /**
     * Verifica se o produto está próximo do vencimento
     * Define "próximo" como 7 dias ou menos
     * 
     * @return true se faltar 7 dias ou menos para vencer
     */
    public boolean proximoDoVencimento() {
        long dias = diasParaVencer();
        // Retorna true se:
        // - dias >= 0 (ainda não venceu) E
        // - dias <= 7 (faltam 7 dias ou menos)
        return dias >= 0 && dias <= 7;
    }

    // ========== SOBRESCRITA DE MÉTODOS ==========
    
    /**
     * SOBRESCRITA: Exibe informações completas do produto perecível
     * Chama o método da classe pai E adiciona informações específicas
     */
    @Override
    public void exibirInformacoes() {
        // Chama o método da classe pai para exibir informações básicas
        super.exibirInformacoes();
        
        // Adiciona informações específicas de produtos perecíveis
        // .format(data) = converte LocalDate para String no formato dd/MM/yyyy
        System.out.println("Data de Validade: " + dataValidade.format(formatter));
        System.out.println("Lote: " + lote);
        
        // Verifica status da validade e exibe alerta apropriado
        if (estaVencido()) {
            System.out.println("⚠️ PRODUTO VENCIDO!");
        } else if (proximoDoVencimento()) {
            System.out.println("⚠️ ATENÇÃO: Vence em " + diasParaVencer() + " dias!");
        } else {
            System.out.println("Validade OK (vence em " + diasParaVencer() + " dias)");
        }
    }

    /**
     * SOBRESCRITA: Converte produto perecível em texto
     * Adiciona status de validade ao texto base
     */
    @Override
    public String toString() {
        String status = "";  // Inicia vazio
        
        // Define status baseado na validade
        if (estaVencido()) {
            status = " [VENCIDO]";
        } else if (proximoDoVencimento()) {
            status = " [PRÓXIMO DO VENCIMENTO]";
        }
        
        // super.toString() = chama o toString() da classe pai
        // Adiciona o status de validade ao final
        return super.toString() + status;
    }
}