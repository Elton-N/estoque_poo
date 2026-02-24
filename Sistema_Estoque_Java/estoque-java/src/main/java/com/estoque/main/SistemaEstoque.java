package com.estoque.main; // INICIO E ESTRUTURA

// Imports necessários
import com.estoque.enums.TipoMovimentacao;
import com.estoque.model.*;
import com.estoque.service.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Classe PRINCIPAL do Sistema de Controle de Estoque
 * Contém o menu CLI (Command Line Interface) completo
 * 
 * DEMONSTRA:
 * - Integração de TODAS as classes
 * - Interface com usuário (CLI)
 * - Uso dos Services
 * - Fluxo completo do sistema
 * 
 * Esta é a classe que tem o método main() = ponto de entrada do programa
 */
public class SistemaEstoque {
    
    // ATRIBUTOS: Services que gerenciam os dados
    // Cada service é responsável por uma entidade
    private CategoriaService categoriaService;
    private ProdutoService produtoService;
    private MovimentacaoService movimentacaoService;
    
    // Scanner: objeto para ler dados digitados pelo usuário
    private Scanner scanner;

    /**
     * CONSTRUTOR
     * Inicializa todos os services e o scanner
     * Carrega dados iniciais para demonstração
     */
    public SistemaEstoque() {
        // Inicializa os services
        // new = cria novo objeto
        this.categoriaService = new CategoriaService();
        this.produtoService = new ProdutoService();
        this.movimentacaoService = new MovimentacaoService();
        
        // Scanner(System.in) = lê dados do teclado
        // System.in = entrada padrão (teclado)
        this.scanner = new Scanner(System.in);
        
        // Carrega alguns dados de exemplo
        carregarDadosIniciais();
    }

    /**
     * MÉTODO MAIN - PONTO DE ENTRADA DO PROGRAMA
     * É o primeiro método executado quando roda o programa
     * 
     * static = pode ser chamado sem criar objeto
     * void = não retorna nada
     * String[] args = argumentos da linha de comando (não usamos)
     */
    public static void main(String[] args) {
        // Cria uma instância do sistema
        SistemaEstoque sistema = new SistemaEstoque();
        
        // Inicia o sistema (mostra menu e loop principal)
        sistema.iniciar();
    }

    /**
     * Carrega dados iniciais para demonstração
     * Cadastra categorias e produtos de exemplo
     */
    private void carregarDadosIniciais() {
        // ========== CADASTRAR CATEGORIAS ==========
        // .cadastrar() retorna o objeto criado
        Categoria cat1 = categoriaService.cadastrar("Alimentos", "Produtos alimentícios em geral");
        Categoria cat2 = categoriaService.cadastrar("Bebidas", "Bebidas diversas");
        Categoria cat3 = categoriaService.cadastrar("Limpeza", "Produtos de limpeza");
        Categoria cat4 = categoriaService.cadastrar("Higiene", "Produtos de higiene pessoal");

        // ========== CADASTRAR PRODUTOS SIMPLES ==========
        // Cria objetos ProdutoSimples
        ProdutoSimples p1 = new ProdutoSimples(
            produtoService.gerarProximoId(),  // Gera ID automático
            "Arroz Branco 5kg",               // Nome
            "Arroz branco tipo 1",            // Descrição
            25.90,                            // Preço
            50,                               // Quantidade inicial
            10,                               // Estoque mínimo
            cat1,                             // Categoria (objeto)
            "7891234567890"                   // Código de barras
        );
        produtoService.cadastrar(p1);  // Adiciona à lista

        ProdutoSimples p2 = new ProdutoSimples(
            produtoService.gerarProximoId(),
            "Feijão Preto 1kg",
            "Feijão preto tipo 1",
            8.50,
            30,
            10,
            cat1,
            "7891234567891"
        );
        produtoService.cadastrar(p2);

        ProdutoSimples p3 = new ProdutoSimples(
            produtoService.gerarProximoId(),
            "Detergente Líquido",
            "Detergente neutro 500ml",
            2.50,
            100,
            20,
            cat3,
            "7891234567892"
        );
        produtoService.cadastrar(p3);

        // ========== CADASTRAR PRODUTOS PERECÍVEIS ==========
        // LocalDate.now().plusDays(X) = data de hoje + X dias
        ProdutoPerecivel p4 = new ProdutoPerecivel(
            produtoService.gerarProximoId(),
            "Leite Integral 1L",
            "Leite integral UHT",
            4.50,
            60,
            15,
            cat2,
            LocalDate.now().plusDays(30),  // Vence em 30 dias
            "LOTE001"
        );
        produtoService.cadastrar(p4);

        ProdutoPerecivel p5 = new ProdutoPerecivel(
            produtoService.gerarProximoId(),
            "Iogurte Natural",
            "Iogurte natural 170g",
            3.20,
            40,
            10,
            cat2,
            LocalDate.now().plusDays(5),   // Vence em 5 dias (ALERTA!)
            "LOTE002"
        );
        produtoService.cadastrar(p5);

        // ========== REGISTRAR MOVIMENTAÇÕES INICIAIS ==========
        // Registra entradas
        movimentacaoService.registrarEntrada(p1, 50, "Compra inicial");
        movimentacaoService.registrarEntrada(p2, 30, "Compra inicial");
        
        // Registra saídas
        // try-catch = tratamento de erro
        try {
            movimentacaoService.registrarSaida(p1, 10, "Venda");
            movimentacaoService.registrarSaida(p3, 5, "Venda");
        } catch (Exception e) {
            // Se der erro, ignora (não vai dar na carga inicial)
            // catch = captura o erro
            // Exception e = objeto que contém informações do erro
        }
    }

    /**
     * INICIA O SISTEMA
     * Mostra cabeçalho e entra no loop principal do menu
     */
    public void iniciar() {
        // Exibe cabeçalho do sistema
        // ╔═══╗ = caracteres especiais para desenhar bordas
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE CONTROLE DE ESTOQUE          ║");
        System.out.println("╚═══════════════════════════════════════════╝");

        // Variável para armazenar a opção escolhida
        int opcao;
        
        // LOOP PRINCIPAL DO SISTEMA
        // do-while = executa pelo menos uma vez
        // Continua enquanto opcao != 0
        do {
            exibirMenuPrincipal();     // Mostra menu
            opcao = lerOpcao();         // Lê opção digitada
            processarOpcao(opcao);      // Executa ação correspondente
        } while (opcao != 0);  // Se digitar 0, sai do loop

        // Fecha o scanner para liberar recursos
        scanner.close();
        
        // Mensagem de despedida
        System.out.println("\nSistema encerrado. Até logo!");
    }

    /**
     * Exibe o menu principal
     * Mostra as opções disponíveis
     */
    private void exibirMenuPrincipal() {
        System.out.println("\n========================================");
        System.out.println("│           MENU PRINCIPAL                │");
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("│ 1 - Gerenciar Categorias                │");
        System.out.println("│ 2 - Gerenciar Produtos                  │");
        System.out.println("│ 3 - Movimentações de Estoque            │");
        System.out.println("│ 4 - Relatórios e Consultas              │");
        System.out.println("│ 0 - Sair                                │");
        System.out.println("└─────────────────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Processa a opção escolhida no menu principal
     * Chama o método correspondente à opção
     * 
     * @param opcao Número da opção escolhida
     */
    private void processarOpcao(int opcao) {
        // switch = estrutura de decisão múltipla
        // Executa código diferente para cada valor de opcao
        switch (opcao) {
            case 1: 
                menuCategorias();      // Chama menu de categorias
                break;                 // break = sai do switch
            case 2: 
                menuProdutos();        // Chama menu de produtos
                break;
            case 3: 
                menuMovimentacoes();   // Chama menu de movimentações
                break;
            case 4: 
                menuRelatorios();      // Chama menu de relatórios
                break;
            case 0: 
                break;                 // Opção 0 = sair (não faz nada)
            default:                   // default = qualquer outro valor
                System.out.println("⚠ Opção inválida!");
        }
    }
    
    
// ==================== MENU DE CATEGORIAS ====================
    
    /**
     * Exibe e gerencia o menu de categorias
     * Loop secundário - só sai quando usuário digita 0
     */
    private void menuCategorias() {
        int opcao;
        
        // Loop do submenu
        do {
            // Mostra opções do menu de categorias
            System.out.println("\n┌─────────────────────────────────────────┐");
            System.out.println("│        GERENCIAR CATEGORIAS             │");
            System.out.println("├─────────────────────────────────────────┤");
            System.out.println("│ 1 - Cadastrar Categoria                 │");
            System.out.println("│ 2 - Buscar Categoria                    │");
            System.out.println("│ 3 - Listar Todas as Categorias          │");
            System.out.println("│ 4 - Atualizar Categoria                 │");
            System.out.println("│ 5 - Remover Categoria                   │");
            System.out.println("│ 0 - Voltar                              │");
            System.out.println("└─────────────────────────────────────────┘");
            System.out.print("Escolha uma opção: ");
            
            opcao = lerOpcao();  // Lê opção

            // Processa opção escolhida
            switch (opcao) {
                case 1: cadastrarCategoria(); break;
                case 2: buscarCategoria(); break;
                case 3: listarTodasCategorias(); break;
                case 4: atualizarCategoria(); break;
                case 5: removerCategoria(); break;
                case 0: break;  // Volta ao menu principal
                default: System.out.println("⚠ Opção inválida!");
            }
        } while (opcao != 0);  // Continua até digitar 0
    }

    /**
     * CADASTRAR CATEGORIA
     * Lê dados do usuário e cadastra nova categoria
     */
    private void cadastrarCategoria() {
        System.out.println("\n=== CADASTRAR CATEGORIA ===");
        
        // Lê nome da categoria
        System.out.print("Nome: ");
        String nome = scanner.nextLine();  // .nextLine() = lê linha inteira
        
        // Lê descrição
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        // Chama service para cadastrar
        Categoria categoria = categoriaService.cadastrar(nome, descricao);
        
        // Exibe mensagem de sucesso com ID gerado
        System.out.println("\n✓ Categoria cadastrada com sucesso! ID: " + categoria.getId());
    }

    /**
     * BUSCAR CATEGORIA
     * Permite buscar por ID ou por nome
     */
    private void buscarCategoria() {
        System.out.println("\n=== BUSCAR CATEGORIA ===");
        System.out.println("1 - Por ID");
        System.out.println("2 - Por Nome");
        System.out.print("Escolha: ");
        int opcao = lerOpcao();

        if (opcao == 1) {
            // BUSCA POR ID
            System.out.print("ID: ");
            String id = scanner.nextLine();
            
            // .buscarPorId() retorna Optional<Categoria>
            Optional<Categoria> cat = categoriaService.buscarPorId(id);
            
            // Verifica se encontrou
            if (cat.isPresent()) {
                // .get() extrai o valor do Optional
                cat.get().exibirInformacoes();
            } else {
                System.out.println("⚠ Categoria não encontrada!");
            }
            
        } else if (opcao == 2) {
            // BUSCA POR NOME
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            
            // Retorna List<Categoria>
            List<Categoria> cats = categoriaService.buscarPorNome(nome);
            
            // Exibe lista de resultados
            exibirListaCategorias(cats);
        }
    }

    /**
     * LISTAR TODAS AS CATEGORIAS
     */
    private void listarTodasCategorias() {
        System.out.println("\n=== TODAS AS CATEGORIAS ===");
        // Chama método auxiliar para exibir lista
        exibirListaCategorias(categoriaService.listarTodas());
    }

    /**
     * ATUALIZAR CATEGORIA
     * Permite modificar nome e descrição
     */
    private void atualizarCategoria() {
        System.out.println("\n=== ATUALIZAR CATEGORIA ===");
        System.out.print("ID da categoria: ");
        String id = scanner.nextLine();

        // Verifica se categoria existe
        Optional<Categoria> catOpt = categoriaService.buscarPorId(id);
        if (!catOpt.isPresent()) {
            // ! = NOT (negação)
            // !isPresent() = NÃO está presente = não encontrou
            System.out.println("⚠ Categoria não encontrada!");
            return;  // return = sai do método
        }

        // Lê novos dados
        System.out.print("Novo Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Nova Descrição: ");
        String descricao = scanner.nextLine();

        // Atualiza usando service
        if (categoriaService.atualizar(id, nome, descricao)) {
            System.out.println("\n✓ Categoria atualizada com sucesso!");
        }
    }

    /**
     * REMOVER CATEGORIA
     */
    private void removerCategoria() {
        System.out.println("\n=== REMOVER CATEGORIA ===");
        System.out.print("ID da categoria: ");
        String id = scanner.nextLine();

        // Tenta remover
        if (categoriaService.remover(id)) {
            System.out.println("\n✓ Categoria removida com sucesso!");
        } else {
            System.out.println("⚠ Categoria não encontrada!");
        }
    }
    
// ==================== MENU DE PRODUTOS ====================
    
    /**
     * Exibe e gerencia o menu de produtos
     * Menu mais complexo: produtos simples E perecíveis
     */
    private void menuProdutos() {
        int opcao;
        
        do {
            // Mostra opções disponíveis
            System.out.println("\n┌─────────────────────────────────────────┐");
            System.out.println("│         GERENCIAR PRODUTOS              │");
            System.out.println("├─────────────────────────────────────────┤");
            System.out.println("│ 1 - Cadastrar Produto Simples           │");
            System.out.println("│ 2 - Cadastrar Produto Perecível         │");
            System.out.println("│ 3 - Buscar Produto                      │");
            System.out.println("│ 4 - Listar Todos os Produtos            │");
            System.out.println("│ 5 - Listar Produtos com Estoque Baixo   │");
            System.out.println("│ 6 - Listar Produtos Sem Estoque         │");
            System.out.println("│ 7 - Alertas de Validade                 │");
            System.out.println("│ 8 - Remover Produto                     │");
            System.out.println("│ 0 - Voltar                              │");
            System.out.println("└─────────────────────────────────────────┘");
            System.out.print("Escolha uma opção: ");
            opcao = lerOpcao();

            // Processa cada opção
            switch (opcao) {
                case 1: cadastrarProdutoSimples(); break;
                case 2: cadastrarProdutoPerecivel(); break;
                case 3: buscarProduto(); break;
                case 4: listarTodosProdutos(); break;
                case 5: listarProdutosEstoqueBaixo(); break;
                case 6: listarProdutosSemEstoque(); break;
                case 7: listarAlertasValidade(); break;
                case 8: removerProduto(); break;
                case 0: break;
                default: System.out.println("⚠ Opção inválida!");
            }
        } while (opcao != 0);
    }

    /**
     * CADASTRAR PRODUTO SIMPLES
     * Pede todos os dados necessários e cria ProdutoSimples
     */
    private void cadastrarProdutoSimples() {
        System.out.println("\n=== CADASTRAR PRODUTO SIMPLES ===");
        
        // PRIMEIRO: escolher a categoria
        // Lista categorias para o usuário ver as opções
        listarTodasCategorias();
        
        System.out.print("\nID da Categoria: ");
        String catId = scanner.nextLine();
        
        // Busca categoria escolhida
        Optional<Categoria> catOpt = categoriaService.buscarPorId(catId);
        if (!catOpt.isPresent()) {
            System.out.println("⚠ Categoria não encontrada!");
            return;  // Sai do método
        }

        // Lê dados do produto
        System.out.print("Nome do Produto: ");
        String nome = scanner.nextLine();
        
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        
        System.out.print("Preço: R$ ");
        // Double.parseDouble() = converte String para double
        double preco = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Quantidade em Estoque: ");
        // Integer.parseInt() = converte String para int
        int quantidade = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Estoque Mínimo: ");
        int estoqueMin = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Código de Barras: ");
        String codigoBarras = scanner.nextLine();

        // Cria objeto ProdutoSimples
        // DEMONSTRA: Criação de objeto com todos os parâmetros
        ProdutoSimples produto = new ProdutoSimples(
            produtoService.gerarProximoId(),  // ID automático
            nome, 
            descricao, 
            preco,
            quantidade, 
            estoqueMin, 
            catOpt.get(),  // Extrai Categoria do Optional
            codigoBarras
        );
        
        // Cadastra no service
        produtoService.cadastrar(produto);
        
        System.out.println("\n✓ Produto cadastrado com sucesso! ID: " + produto.getId());
    }

    /**
     * CADASTRAR PRODUTO PERECÍVEL
     * Similar ao produto simples, mas pede data de validade e lote
     */
    private void cadastrarProdutoPerecivel() {
        System.out.println("\n=== CADASTRAR PRODUTO PERECÍVEL ===");
        
        // Escolher categoria
        listarTodasCategorias();
        System.out.print("\nID da Categoria: ");
        String catId = scanner.nextLine();
        
        Optional<Categoria> catOpt = categoriaService.buscarPorId(catId);
        if (!catOpt.isPresent()) {
            System.out.println("⚠ Categoria não encontrada!");
            return;
        }

        // Lê dados básicos
        System.out.print("Nome do Produto: ");
        String nome = scanner.nextLine();
        
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        
        System.out.print("Preço: R$ ");
        double preco = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Quantidade em Estoque: ");
        int quantidade = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Estoque Mínimo: ");
        int estoqueMin = Integer.parseInt(scanner.nextLine());
        
        // ESPECÍFICO DE PERECÍVEL: Data de validade
        System.out.print("Data de Validade (dd/MM/yyyy): ");
        String dataStr = scanner.nextLine();
        
        // Converte String para LocalDate
        // DateTimeFormatter.ofPattern() = define formato esperado
        // LocalDate.parse() = converte String para data
        LocalDate dataValidade = LocalDate.parse(dataStr, 
            DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        // ESPECÍFICO DE PERECÍVEL: Lote
        System.out.print("Lote: ");
        String lote = scanner.nextLine();

        // Cria objeto ProdutoPerecivel
        ProdutoPerecivel produto = new ProdutoPerecivel(
            produtoService.gerarProximoId(),
            nome, 
            descricao, 
            preco,
            quantidade, 
            estoqueMin, 
            catOpt.get(),
            dataValidade,  // Parâmetro adicional
            lote           // Parâmetro adicional
        );
        
        produtoService.cadastrar(produto);
        
        System.out.println("\n✓ Produto perecível cadastrado com sucesso! ID: " + produto.getId());
    }

    /**
     * BUSCAR PRODUTO
     * Permite buscar por ID, nome ou categoria
     */
    private void buscarProduto() {
        System.out.println("\n=== BUSCAR PRODUTO ===");
        System.out.println("1 - Por ID");
        System.out.println("2 - Por Nome");
        System.out.println("3 - Por Categoria");
        System.out.print("Escolha: ");
        int opcao = lerOpcao();

        switch (opcao) {
            case 1:
                // BUSCA POR ID
                System.out.print("ID: ");
                String id = scanner.nextLine();
                
                Optional<Produto> prod = produtoService.buscarPorId(id);
                if (prod.isPresent()) {
                    // .exibirInformacoes() funciona para ambos os tipos
                    // POLIMORFISMO: método funciona tanto para ProdutoSimples
                    // quanto para ProdutoPerecivel
                    prod.get().exibirInformacoes();
                } else {
                    System.out.println("⚠ Produto não encontrado!");
                }
                break;
                
            case 2:
                // BUSCA POR NOME (parcial)
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                
                List<Produto> prods = produtoService.buscarPorNome(nome);
                exibirListaProdutos(prods);
                break;
                
            case 3:
                // BUSCA POR CATEGORIA
                listarTodasCategorias();  // Mostra categorias disponíveis
                System.out.print("\nID da Categoria: ");
                String catId = scanner.nextLine();
                
                List<Produto> prodsCat = produtoService.buscarPorCategoria(catId);
                exibirListaProdutos(prodsCat);
                break;
        }
    }

    /**
     * LISTAR TODOS OS PRODUTOS
     */
    private void listarTodosProdutos() {
        System.out.println("\n=== TODOS OS PRODUTOS ===");
        exibirListaProdutos(produtoService.listarTodos());
    }

    /**
     * LISTAR PRODUTOS COM ESTOQUE BAIXO
     * Produtos que atingiram ou ficaram abaixo do estoque mínimo
     */
    private void listarProdutosEstoqueBaixo() {
        System.out.println("\n=== PRODUTOS COM ESTOQUE BAIXO ===");
        exibirListaProdutos(produtoService.listarComEstoqueBaixo());
    }

    /**
     * LISTAR PRODUTOS SEM ESTOQUE
     * Produtos com quantidade = 0
     */
    private void listarProdutosSemEstoque() {
        System.out.println("\n=== PRODUTOS SEM ESTOQUE ===");
        exibirListaProdutos(produtoService.listarSemEstoque());
    }

    /**
     * ALERTAS DE VALIDADE
     * Lista produtos perecíveis próximos do vencimento ou vencidos
     * 
     * DEMONSTRA: Trabalho com tipo específico (ProdutoPerecivel)
     */
    private void listarAlertasValidade() {
        System.out.println("\n=== ALERTAS DE VALIDADE ===");
        
        // Lista apenas produtos perecíveis com problema
        List<ProdutoPerecivel> produtos = produtoService.listarPereciveisProximosVencimento();
        
        // Verifica se encontrou algo
        if (produtos.isEmpty()) {
            System.out.println("⚠ Nenhum produto próximo do vencimento.");
            return;
        }
        
        // Exibe quantidade de alertas
        System.out.println("\n" + produtos.size() + " produto(s) com alerta:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // forEach = percorre cada elemento da lista
        // p -> representa cada ProdutoPerecivel
        for (ProdutoPerecivel p : produtos) {
            System.out.println(p);  // Chama toString()
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * REMOVER PRODUTO
     */
    private void removerProduto() {
        System.out.println("\n=== REMOVER PRODUTO ===");
        System.out.print("ID do produto: ");
        String id = scanner.nextLine();

        if (produtoService.remover(id)) {
            System.out.println("\n✓ Produto removido com sucesso!");
        } else {
            System.out.println("⚠ Produto não encontrado!");
        }
    }
    
// ==================== MENU DE MOVIMENTAÇÕES ====================
    
    /**
     * Exibe e gerencia o menu de movimentações
     * Registra entradas e saídas, consulta histórico
     */
    private void menuMovimentacoes() {
        int opcao;
        
        do {
            System.out.println("\n┌─────────────────────────────────────────┐");
            System.out.println("│      MOVIMENTAÇÕES DE ESTOQUE           │");
            System.out.println("├─────────────────────────────────────────┤");
            System.out.println("│ 1 - Registrar Entrada                   │");
            System.out.println("│ 2 - Registrar Saída                     │");
            System.out.println("│ 3 - Consultar Movimentações             │");
            System.out.println("│ 4 - Histórico de Produto                │");
            System.out.println("│ 5 - Últimas Movimentações               │");
            System.out.println("│ 0 - Voltar                              │");
            System.out.println("└─────────────────────────────────────────┘");
            System.out.print("Escolha uma opção: ");
            opcao = lerOpcao();

            switch (opcao) {
                case 1: registrarEntrada(); break;
                case 2: registrarSaida(); break;
                case 3: consultarMovimentacoes(); break;
                case 4: historicoProdu(); break;
                case 5: listarUltimasMovimentacoes(); break;
                case 0: break;
                default: System.out.println("⚠ Opção inválida!");
            }
        } while (opcao != 0);
    }

    /**
     * REGISTRAR ENTRADA
     * Adiciona produtos ao estoque
     */
    private void registrarEntrada() {
        System.out.println("\n=== REGISTRAR ENTRADA ===");
        System.out.print("ID do Produto: ");
        String id = scanner.nextLine();

        // Busca produto
        Optional<Produto> prodOpt = produtoService.buscarPorId(id);
        if (!prodOpt.isPresent()) {
            System.out.println("⚠ Produto não encontrado!");
            return;
        }

        Produto produto = prodOpt.get();
        
        // Mostra informações atuais
        System.out.println("Produto: " + produto.getNome());
        System.out.println("Estoque atual: " + produto.getQuantidadeEstoque());
        
        // Lê quantidade a adicionar
        System.out.print("\nQuantidade a adicionar: ");
        int quantidade = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Observação: ");
        String obs = scanner.nextLine();

        // Registra entrada no service
        // O service cuida de atualizar o estoque E criar a movimentação
        Movimentacao mov = movimentacaoService.registrarEntrada(produto, quantidade, obs);
        
        // Confirmação
        System.out.println("\n✓ Entrada registrada com sucesso! ID: " + mov.getId());
        System.out.println("Novo estoque: " + produto.getQuantidadeEstoque());
    }

    /**
     * REGISTRAR SAÍDA
     * Remove produtos do estoque (venda, perda, etc.)
     * 
     * DEMONSTRA: Tratamento de exceção
     */
    private void registrarSaida() {
        System.out.println("\n=== REGISTRAR SAÍDA ===");
        System.out.print("ID do Produto: ");
        String id = scanner.nextLine();

        // Busca produto
        Optional<Produto> prodOpt = produtoService.buscarPorId(id);
        if (!prodOpt.isPresent()) {
            System.out.println("⚠ Produto não encontrado!");
            return;
        }

        Produto produto = prodOpt.get();
        
        // Mostra informações atuais
        System.out.println("Produto: " + produto.getNome());
        System.out.println("Estoque disponível: " + produto.getQuantidadeEstoque());
        
        // Lê quantidade a retirar
        System.out.print("\nQuantidade a retirar: ");
        int quantidade = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Observação: ");
        String obs = scanner.nextLine();

        // TRY-CATCH: Tratamento de erro
        // try = tenta executar o código
        try {
            // Tenta registrar saída
            // Pode lançar Exception se não houver estoque suficiente
            Movimentacao mov = movimentacaoService.registrarSaida(produto, quantidade, obs);
            
            // Se chegou aqui, deu certo
            System.out.println("\n✓ Saída registrada com sucesso! ID: " + mov.getId());
            System.out.println("Novo estoque: " + produto.getQuantidadeEstoque());
            
        } catch (Exception e) {
            // catch = captura o erro se ocorrer
            // e.getMessage() = pega a mensagem de erro
            System.out.println("\n⚠ Erro: " + e.getMessage());
        }
    }

    /**
     * CONSULTAR MOVIMENTAÇÕES
     * Permite filtrar por tipo (todas, entradas ou saídas)
     */
    private void consultarMovimentacoes() {
        System.out.println("\n=== CONSULTAR MOVIMENTAÇÕES ===");
        System.out.println("1 - Todas");
        System.out.println("2 - Apenas Entradas");
        System.out.println("3 - Apenas Saídas");
        System.out.print("Escolha: ");
        int opcao = lerOpcao();

        List<Movimentacao> movs;
        
        // Define qual lista buscar baseado na escolha
        switch (opcao) {
            case 1:
                movs = movimentacaoService.listarTodas();
                break;
            case 2:
                // TipoMovimentacao.ENTRADA = constante do enum
                movs = movimentacaoService.listarPorTipo(TipoMovimentacao.ENTRADA);
                break;
            case 3:
                movs = movimentacaoService.listarPorTipo(TipoMovimentacao.SAIDA);
                break;
            default:
                return;  // Opção inválida, sai do método
        }
        
        // Exibe lista
        exibirListaMovimentacoes(movs);
    }

    /**
     * HISTÓRICO DE PRODUTO
     * Mostra todas as movimentações de um produto específico
     */
    private void historicoProdu() {
        System.out.println("\n=== HISTÓRICO DE PRODUTO ===");
        System.out.print("ID do Produto: ");
        String id = scanner.nextLine();

        // Busca movimentações do produto
        List<Movimentacao> movs = movimentacaoService.listarPorProduto(id);
        exibirListaMovimentacoes(movs);
    }

    /**
     * ÚLTIMAS MOVIMENTAÇÕES
     * Mostra as N movimentações mais recentes
     */
    private void listarUltimasMovimentacoes() {
        System.out.println("\n=== ÚLTIMAS MOVIMENTAÇÕES ===");
        System.out.print("Quantidade: ");
        int qtd = Integer.parseInt(scanner.nextLine());

        List<Movimentacao> movs = movimentacaoService.listarUltimas(qtd);
        exibirListaMovimentacoes(movs);
    }
    
// ==================== MENU DE RELATÓRIOS ====================
    
    /**
     * MENU DE RELATÓRIOS
     * Exibe estatísticas gerais do sistema
     * 
     * Não é um submenu com loop, apenas exibe informações e volta
     */
    private void menuRelatorios() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│       RELATÓRIOS E CONSULTAS            │");
        System.out.println("└─────────────────────────────────────────┘");
        
        // Título da seção
        System.out.println("\n📊 ESTATÍSTICAS GERAIS:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // ========== ESTATÍSTICAS DE PRODUTOS ==========
        System.out.println("\n📦 Produtos:");
        
        // Quantidade total de produtos cadastrados
        System.out.println("   • Total de produtos: " + produtoService.quantidadeTotal());
        
        // Quantidade total de ITENS (soma de todos os estoques)
        System.out.println("   • Total de itens em estoque: " + 
                          produtoService.quantidadeTotalItens());
        
        // Valor total do estoque (soma de preço × quantidade de cada produto)
        // String.format("%.2f") = formata número com 2 casas decimais
        System.out.println("   • Valor total do estoque: R$ " + 
                          String.format("%.2f", produtoService.valorTotalEstoque()));
        
        // Quantidade de produtos que precisam reposição
        // .listarComEstoqueBaixo() retorna lista
        // .size() retorna tamanho da lista
        System.out.println("   • Produtos com estoque baixo: " + 
                          produtoService.listarComEstoqueBaixo().size());
        
        // Quantidade de produtos sem nenhum item
        System.out.println("   • Produtos sem estoque: " + 
                          produtoService.listarSemEstoque().size());
        
        // ========== ESTATÍSTICAS DE CATEGORIAS ==========
        System.out.println("\n📂 Categorias:");
        System.out.println("   • Total de categorias: " + categoriaService.quantidadeTotal());
        
        // ========== ESTATÍSTICAS DE MOVIMENTAÇÕES ==========
        System.out.println("\n📋 Movimentações:");
        
        // Total de movimentações registradas
        System.out.println("   • Total de movimentações: " + 
                          movimentacaoService.quantidadeTotal());
        
        // Quantidade de entradas
        System.out.println("   • Entradas: " + 
                          movimentacaoService.quantidadeEntradas());
        
        // Quantidade de saídas
        System.out.println("   • Saídas: " + 
                          movimentacaoService.quantidadeSaidas());
        
        // ========== ALERTAS ==========
        System.out.println("\n⚠️  Alertas:");
        
        // Conta produtos perecíveis com problema de validade
        int alertasValidade = produtoService.listarPereciveisProximosVencimento().size();
        System.out.println("   • Produtos próximos do vencimento: " + alertasValidade);
        
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    // ==================== MÉTODOS AUXILIARES ====================
    // Métodos helper usados por várias partes do sistema
    
    /**
     * Exibe uma lista de categorias formatada
     * 
     * MÉTODO AUXILIAR: Usado por vários métodos para exibir listas
     * Evita repetição de código
     * 
     * @param categorias Lista de categorias a exibir
     */
    private void exibirListaCategorias(List<Categoria> categorias) {
        // Verifica se lista está vazia
        // .isEmpty() = retorna true se lista não tem elementos
        if (categorias.isEmpty()) {
            System.out.println("⚠ Nenhuma categoria encontrada.");
            return;  // Sai do método
        }
        
        // Exibe cabeçalho com quantidade
        // .size() = quantidade de elementos na lista
        System.out.println("\n" + categorias.size() + " categoria(s) encontrada(s):");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // FOR-EACH: Percorre cada elemento da lista
        // Categoria c = variável que representa cada categoria
        // : categorias = percorre a lista categorias
        for (Categoria c : categorias) {
            // System.out.println(c) chama automaticamente c.toString()
            System.out.println(c);
        }
        
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Exibe uma lista de produtos formatada
     * 
     * POLIMORFISMO:
     * A lista pode conter ProdutoSimples E ProdutoPerecivel
     * Ambos são tratados uniformemente como Produto
     * 
     * @param produtos Lista de produtos a exibir
     */
    private void exibirListaProdutos(List<Produto> produtos) {
        if (produtos.isEmpty()) {
            System.out.println("⚠ Nenhum produto encontrado.");
            return;
        }
        
        System.out.println("\n" + produtos.size() + " produto(s) encontrado(s):");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // FOR-EACH percorre produtos
        // p pode ser ProdutoSimples OU ProdutoPerecivel
        // POLIMORFISMO: .toString() funciona para ambos
        for (Produto p : produtos) {
            System.out.println(p);  // Chama toString()
        }
        
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Exibe uma lista de movimentações formatada
     * 
     * @param movimentacoes Lista de movimentações a exibir
     */
    private void exibirListaMovimentacoes(List<Movimentacao> movimentacoes) {
        if (movimentacoes.isEmpty()) {
            System.out.println("⚠ Nenhuma movimentação encontrada.");
            return;
        }
        
        System.out.println("\n" + movimentacoes.size() + " movimentação(ões) encontrada(s):");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        for (Movimentacao m : movimentacoes) {
            System.out.println(m);  // Chama toString()
        }
        
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Lê uma opção numérica digitada pelo usuário
     * 
     * TRATAMENTO DE ERRO:
     * Se usuário digitar algo que não é número, retorna -1
     * 
     * @return Número digitado ou -1 se inválido
     */
    private int lerOpcao() {
        // try-catch = tratamento de erro
        try {
            // scanner.nextLine() = lê linha inteira como String
            // Integer.parseInt() = converte String para int
            // Se String não for número, lança NumberFormatException
            return Integer.parseInt(scanner.nextLine());
            
        } catch (NumberFormatException e) {
            // catch = captura o erro
            // Se der erro (usuário digitou letra), retorna -1
            // -1 será tratado como opção inválida no switch
            return -1;
        }
    }
}  // FIM DA CLASSE SistemaEstoque