# Coffee Spot ☕

Sistema de frente de caixa para cafeterias, desenvolvido em Java como projeto acadêmico da unidade curricular de Algoritmos e Programação, da Universidade Potiguar (UnP).

## Sobre o projeto

O Coffee Spot é uma aplicação de terminal que simula o processo de atendimento e vendas de uma cafeteria.

O sistema permite consultar um cardápio, criar pedidos com diferentes produtos, calcular valores e descontos, registrar pagamentos e acompanhar as vendas realizadas.

O projeto foi desenvolvido para aplicar conceitos de lógica de programação e organização de código, utilizando Java e orientação a objetos.

## Funcionalidades

- Consulta de cardápio organizado por categorias.
- Seleção de produtos por código.
- Inclusão de múltiplos produtos em um pedido.
- Controle de quantidades.
- Cálculo automático de subtotal.
- Aplicação de descontos progressivos.
- Pagamentos simulados por dinheiro, Pix e cartão.
- Cálculo de troco para pagamentos em dinheiro.
- Emissão de comprovante no terminal.
- Histórico de vendas realizadas.
- Consulta de comprovantes anteriores.
- Relatório de fechamento de caixa.
- Validação de entradas do usuário.

## Tecnologias utilizadas

- Java 21
- Java Collections
- BigDecimal
- Git e GitHub
- Visual Studio Code

## Regras de desconto

| Valor do pedido | Desconto |
|---|---|
| Abaixo de R$ 30,00 | 0% |
| De R$ 30,00 a R$ 59,99 | 5% |
| De R$ 60,00 a R$ 99,99 | 10% |
| A partir de R$ 100,00 | 15% |

## Como executar

É necessário ter o JDK 21 instalado.

Clone o repositório e acesse a pasta do projeto.

Compile os arquivos Java:

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
```

Execute o programa:

```bash
java -cp out com.coffeespot.Main
```

## Como executar os testes

Compile o código principal e os testes:

```bash
javac -encoding UTF-8 -d out $(find src tests -name "*.java")
```

Execute os testes:

```bash
java -cp out com.coffeespot.CoffeeSpotTest
```

## Organização do projeto

O código está dividido em pacotes:

- `model`: entidades e objetos de domínio.
- `service`: regras de negócio e cálculos.
- `repository`: armazenamento temporário das vendas.
- `ui`: interação com o usuário pelo terminal.

## Limitações

O Coffee Spot é um protótipo acadêmico. Os pagamentos são simulados e os dados são armazenados somente em memória, sendo perdidos quando o programa é encerrado.

O sistema não realiza transações bancárias nem utiliza banco de dados.

## Possíveis evoluções

- Persistência de dados em PostgreSQL.
- API REST com Spring Boot.
- Cadastro e gerenciamento de produtos.
- Controle de estoque.
- Autenticação e permissões de usuários.
- Dashboard de vendas.
- Versão web e mobile.

## Contexto acadêmico

Projeto desenvolvido para a avaliação A3 da unidade curricular de Algoritmos e Programação da Universidade Potiguar (UnP), em 2026.