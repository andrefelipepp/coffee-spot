
package com.coffeespot;

import com.coffeespot.model.*;
import com.coffeespot.repository.VendaRepository;
import com.coffeespot.service.*;

import java.math.BigDecimal;

public class CoffeeSpotTest {

    private static int aprovados = 0;
    private static int reprovados = 0;

    public static void main(String[] args) {

        System.out.println("=== TESTES COFFEE SPOT ===");

        executar("Buscar produto existente",
                CoffeeSpotTest::testarProdutoExistente);

        executar("Buscar produto inexistente",
                CoffeeSpotTest::testarProdutoInexistente);

        executar("Rejeitar quantidade invalida",
                CoffeeSpotTest::testarQuantidadeInvalida);

        executar("Somar produtos repetidos",
                CoffeeSpotTest::testarProdutosRepetidos);

        executar("Calcular subtotal",
                CoffeeSpotTest::testarSubtotal);

        executar("Calcular descontos",
                CoffeeSpotTest::testarDescontos);

        executar("Rejeitar pagamento insuficiente",
                CoffeeSpotTest::testarPagamentoInsuficiente);

        executar("Registrar venda e calcular troco",
                CoffeeSpotTest::testarVendaDinheiro);

        executar("Gerar relatorio de caixa",
                CoffeeSpotTest::testarRelatorio);

        System.out.println("\n=== RESULTADO FINAL ===");
        System.out.println("Aprovados: " + aprovados);
        System.out.println("Reprovados: " + reprovados);

        if (reprovados > 0) {
            System.exit(1);
        }
    }

    private static void executar(String nome, Runnable teste) {
        try {
            teste.run();
            aprovados++;
            System.out.println("[OK] " + nome);
        } catch (Throwable erro) {
            reprovados++;
            System.out.println("[FALHOU] " + nome);
            System.out.println("  Motivo: " + erro.getMessage());
        }
    }

    private static void verificar(
            boolean condicao, String mensagem) {

        if (!condicao) {
            throw new AssertionError(mensagem);
        }
    }

    private static void verificarValor(
            String esperado, BigDecimal atual) {

        BigDecimal valorEsperado = new BigDecimal(esperado);

        verificar(
            valorEsperado.compareTo(atual) == 0,
            "Esperado R$ " + esperado + ", obtido R$ " + atual
        );
    }

    private static Pedido criarPedido(
            int codigo, int quantidade) {

        CardapioService cardapio = new CardapioService();
        PedidoService servico = new PedidoService(cardapio);

        Pedido pedido = servico.criarPedido();
        servico.adicionarProduto(pedido, codigo, quantidade);

        return pedido;
    }

    private static void testarProdutoExistente() {
        CardapioService cardapio = new CardapioService();

        Produto produto = cardapio.buscarPorCodigo(2);

        verificar(produto != null, "Produto nao encontrado.");
        verificar(
            produto.getNome().equals("Cappuccino"),
            "Nome incorreto."
        );
    }

    private static void testarProdutoInexistente() {
        CardapioService cardapio = new CardapioService();

        verificar(
            cardapio.buscarPorCodigo(999) == null,
            "Produto inexistente foi encontrado."
        );
    }

    private static void testarQuantidadeInvalida() {
        CardapioService cardapio = new CardapioService();
        Pedido pedido = new Pedido();

        boolean rejeitou = false;

        try {
            pedido.adicionarProduto(
                cardapio.buscarPorCodigo(1), 0
            );
        } catch (IllegalArgumentException e) {
            rejeitou = true;
        }

        verificar(rejeitou, "Quantidade zero foi aceita.");
    }

    private static void testarProdutosRepetidos() {
        CardapioService cardapio = new CardapioService();
        Pedido pedido = new Pedido();

        Produto espresso = cardapio.buscarPorCodigo(1);

        pedido.adicionarProduto(espresso, 2);
        pedido.adicionarProduto(espresso, 3);

        verificar(
            pedido.getItens().size() == 1,
            "O produto foi duplicado."
        );

        verificar(
            pedido.getItens().get(0).getQuantidade() == 5,
            "Quantidade incorreta."
        );
    }

    private static void testarSubtotal() {
        CardapioService cardapio = new CardapioService();
        Pedido pedido = new Pedido();

        pedido.adicionarProduto(
            cardapio.buscarPorCodigo(2), 2
        );

        pedido.adicionarProduto(
            cardapio.buscarPorCodigo(8), 1
        );

        verificarValor("31.00", pedido.calcularSubtotal());
    }

    private static void testarDescontos() {
        CaixaService caixa = new CaixaService(
            new VendaRepository()
        );

        // Espresso: R$ 6,00
        verificarValor(
            "0.00",
            caixa.calcularDesconto(criarPedido(1, 1))
        );

        // 5 espressos: R$ 30,00 - 5%
        verificarValor(
            "1.50",
            caixa.calcularDesconto(criarPedido(1, 5))
        );

        // 10 espressos: R$ 60,00 - 10%
        verificarValor(
            "6.00",
            caixa.calcularDesconto(criarPedido(1, 10))
        );

        // 20 espressos: R$ 120,00 - 15%
        verificarValor(
            "18.00",
            caixa.calcularDesconto(criarPedido(1, 20))
        );
    }

    private static void testarPagamentoInsuficiente() {
        VendaRepository repositorio = new VendaRepository();
        CaixaService caixa = new CaixaService(repositorio);

        Pedido pedido = criarPedido(2, 1);

        boolean rejeitou = false;

        try {
            caixa.finalizarVenda(
                pedido,
                FormaPagamento.DINHEIRO,
                new BigDecimal("5.00")
            );
        } catch (IllegalArgumentException e) {
            rejeitou = true;
        }

        verificar(rejeitou, "Pagamento insuficiente foi aceito.");

        verificar(
            repositorio.listarTodas().isEmpty(),
            "Venda invalida foi registrada."
        );
    }

    private static void testarVendaDinheiro() {
        VendaRepository repositorio = new VendaRepository();
        CaixaService caixa = new CaixaService(repositorio);

        Pedido pedido = criarPedido(2, 2);

        Venda venda = caixa.finalizarVenda(
            pedido,
            FormaPagamento.DINHEIRO,
            new BigDecimal("50.00")
        );

        verificarValor("24.00", venda.getTotal());
        verificarValor("26.00", venda.getTroco());

        verificar(
            repositorio.listarTodas().size() == 1,
            "Venda nao registrada."
        );
    }

    private static void testarRelatorio() {
        VendaRepository repositorio = new VendaRepository();
        CaixaService caixa = new CaixaService(repositorio);

        Pedido espresso = criarPedido(1, 1);
        Pedido cappuccino = criarPedido(2, 1);

        caixa.finalizarVenda(
            espresso,
            FormaPagamento.PIX,
            new BigDecimal("6.00")
        );

        caixa.finalizarVenda(
            cappuccino,
            FormaPagamento.CARTAO,
            new BigDecimal("12.00")
        );

        RelatorioCaixa relatorio = caixa.gerarRelatorio();

        verificar(
            relatorio.getQuantidadeVendas() == 2,
            "Quantidade de vendas incorreta."
        );

        verificarValor(
            "18.00", relatorio.getFaturamento()
        );

        verificarValor(
            "6.00", relatorio.getTotalPix()
        );

        verificarValor(
            "12.00", relatorio.getTotalCartao()
        );

        verificarValor(
            "9.00", relatorio.getTicketMedio()
        );
    }
}
