# Avaliação : estoque_poo

Sistema CLI (Command Line Interface) para controlar estoque de produtos, categorias e movimentações de entrada/saída. Os dados são armazenados em memória utilizando coleções Java.

##  Funcionalidades (Algumas ainda estão em desenvolvimento)

# Gerenciamento de Categorias
- Cadastrar categorias de produtos
-  Buscar por ID ou nome
-  Listar todas as categorias
-  Atualizar informações
-  Remover categorias

###  Gerenciamento de Produtos
-  Cadastrar produtos simples
-  Cadastrar produtos perecíveis (com validade)
-  Buscar por ID, nome ou categoria
-  Listar todos os produtos
-  Alertas de estoque baixo
-  Alertas de produtos sem estoque
-  Alertas de produtos próximos do vencimento
-  Remover produtos

###  Movimentações de Estoque
-  Registrar entradas
-  Registrar saídas (com validação de estoque)
-  Consultar movimentações (todas, entradas ou saídas)
-  Histórico por produto
-  Últimas movimentações

###  Relatórios
-  Estatísticas gerais do estoque
-  Valor total do estoque
-  Produtos que precisam reposição
-  Alertas de validade



##  Como Compilar e Executar

### Pré-requisitos
- Java JDK 8 ou superior instalado
- Terminal/Prompt de comando

### Opção 1: Script Automático (Linux/Mac)

```bash
chmod +x compilar.sh
./compilar.sh
```

### Opção 2: Script Automático (Windows)

```cmd
compilar.bat
```

## 💡 Como Usar

### Menu Principal
```
┌─────────────────────────────────────────┐
│           MENU PRINCIPAL                │
├─────────────────────────────────────────┤   
│ 1 - Gerenciar Categorias                │
│ 2 - Gerenciar Produtos                  │
│ 3 - Movimentações de Estoque            │
│ 4 - Relatórios e Consultas              │
│ 0 - Sair                                │
└─────────────────────────────────────────┘
```

### Exemplos de Uso

**1. Cadastrar uma categoria:**
- Menu Principal → 1 → 1
- Digite nome e descrição

**2. Cadastrar um produto:**
- Menu Principal → 2 → 1 (ou 2 para perecível)
- Escolha uma categoria
- Preencha os dados do produto

**3. Registrar entrada de mercadorias:**
- Menu Principal → 3 → 1
- Digite o ID do produto
- Informe a quantidade

**4. Registrar saída (venda):**
- Menu Principal → 3 → 2
- Digite o ID do produto
- Informe a quantidade

**5. Ver relatórios:**
- Menu Principal → 4


