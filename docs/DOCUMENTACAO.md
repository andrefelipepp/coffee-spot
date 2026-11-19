# Documentação Técnica — Coffee Spot

## 1. Objetivo

Desenvolver um sistema de frente de caixa para uma cafeteria, permitindo registrar pedidos, calcular valores, processar pagamentos simulados e consultar relatórios de vendas.

## 2. Arquitetura

O sistema utiliza uma organização modular em Java, separando a interação com o usuário, os dados e as regras de negócio.

### Camada de apresentação

A classe `Menu` apresenta as opções disponíveis, recebe entradas pelo terminal e exibe os resultados das operações.

### Camada de modelo

- `Produto`: representa um produto do cardápio.
- `ItemPedido`: associa um produto a uma quantidade.
- `Pedido`: armazena os itens selecionados.
- `FormaPagamento`: define os meios de pagamento.
- `Venda`: representa uma transação concluída.
- `RelatorioCaixa`: armazena os resultados do relatório financeiro.

### Camada de serviços

- `CardapioService`: fornece o catálogo de produtos e permite consultas.
- `PedidoService`: gerencia a criação dos pedidos e a inclusão de produtos.
- `CaixaService`: calcula descontos, finaliza vendas e gera relatórios.

### Camada de armazenamento

`VendaRepository` mantém as vendas em uma lista durante a execução do programa.

## 3. Requisitos funcionais

- RF01: O sistema deve exibir o cardápio.
- RF02: O sistema deve permitir selecionar produtos e quantidades.
- RF03: O sistema deve calcular o subtotal de cada pedido.
- RF04: O sistema deve aplicar descontos conforme o subtotal.
- RF05: O sistema deve permitir pagamentos simulados.
- RF06: O sistema deve calcular o troco em dinheiro.
- RF07: O sistema deve registrar vendas concluídas.
- RF08: O sistema deve permitir consultar o histórico de vendas.
- RF09: O sistema deve apresentar um relatório de caixa.
- RF10: O sistema deve permitir realizar vários pedidos na mesma execução.

## 4. Requisitos não funcionais

- RNF01: O sistema deve ser executado no terminal.
- RNF02: O código deve ser desenvolvido em Java.
- RNF03: Os valores monetários devem utilizar `BigDecimal`.
- RNF04: O sistema deve validar entradas numéricas.
- RNF05: O código deve estar organizado em classes e pacotes.
- RNF06: O sistema não deve depender de conexão com a internet.

## 5. Regras de negócio

- RN01: Cada produto possui um código identificador.
- RN02: A quantidade de produtos deve ser positiva.
- RN03: Produtos repetidos no mesmo pedido têm suas quantidades somadas.
- RN04: O desconto é calculado sobre o subtotal.
- RN05: Pedidos vazios não podem ser finalizados.
- RN06: O pagamento em dinheiro deve cobrir o total da venda.
- RN07: Pagamentos por Pix e cartão são simulados.
- RN08: Somente vendas concluídas entram nos relatórios.
- RN09: O troco não integra o faturamento.
- RN10: Os dados permanecem disponíveis apenas durante a execução do programa.

## 6. Limitações

O sistema não possui banco de dados, autenticação, integração bancária ou persistência permanente. O fechamento de caixa funciona como relatório de conferência, sem encerrar ou limpar a sessão.

## 7. Testes

O projeto inclui testes automatizados em Java para verificar produtos, quantidades, subtotais, descontos, pagamentos e relatórios.

A aprovação final depende da execução dos testes no ambiente de desenvolvimento.