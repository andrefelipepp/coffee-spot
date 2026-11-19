
package com.coffeespot.ui;

import com.coffeespot.model.FormaPagamento;
import com.coffeespot.model.ItemPedido;
import com.coffeespot.model.Pedido;
import com.coffeespot.model.Produto;
import com.coffeespot.model.RelatorioCaixa;
import com.coffeespot.model.Venda;
import com.coffeespot.repository.VendaRepository;
import com.coffeespot.service.CaixaService;
import com.coffeespot.service.CardapioService;
import com.coffeespot.service.PedidoService;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner;
    private final CardapioService cardapioService;
    private final PedidoService pedidoService;
    private final VendaRepository vendaRepository;
    private final CaixaService caixaService;

    public Menu() {
        scanner = new Scanner(System.in);
        cardapioService = new CardapioService();
        pedidoService = new PedidoService(cardapioService);
        vendaRepository = new VendaRepository();
        caixaService = new CaixaService(vendaRepository);
    }

    public void iniciar() {
        int opcao;

        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opcao: ");

            switch (opcao) {
                case 1:
                    novoPedido();
                    break;
                case 2:
                    exibirCardapio();
                    break;
                case 3:
                    consultarVendas();
                    break;
                case 4:
                    fecharCaixa();
                    break;
                case 0:
                    System.out.println("\nEncerrando Coffee Spot...");
                    break;
                default:
                    System.out.println("\nOpcao invalida.");
            }

        } while (opcao != 0);
    }

    private void exibirMenu() {
        System.out.println("\n================================");
        System.out.println("          COFFEE SPOT");
        System.out.println("     Sistema de Frente de Caixa");
        System.out.println("================================");
        System.out.println("1 - Novo pedido");
        System.out.println("2 - Consultar cardapio");
        System.out.println("3 - Consultar vendas");
        System.out.println("4 - Fechar caixa");
        System.out.println("0 - Sair");
        System.out.println("================================");
    }

    private void exibirCardapio() {
        System.out.println("\n========== CARDAPIO ==========");

        String categoriaAtual = "";

        for (Produto produto : cardapioService.listarProdutos()) {

            if (!produto.getCategoria().equals(categoriaAtual)) {
                categoriaAtual = produto.getCategoria();
                System.out.println("\n--- " + categoriaAtual + " ---");
            }

            System.out.printf(
                "%02d - %-22s R$ %6.2f%n",
                produto.getCodigo(),
                produto.getNome(),
                produto.getPreco()
            );
        }

        System.out.println("==============================");
    }

    private void novoPedido() {
        Pedido pedido = pedidoService.criarPedido();

        System.out.println("\n===== NOVO PEDIDO =====");

        while (true) {
            exibirCardapio();

            int codigo = lerInteiro(
                "\nCodigo do produto (0 para finalizar): "
            );

            if (codigo == 0) {
                break;
            }

            Produto produto = cardapioService.buscarPorCodigo(codigo);

            if (produto == null) {
                System.out.println("Produto nao encontrado!");
                continue;
            }

            int quantidade = lerInteiro("Quantidade: ");

            if (quantidade <= 0) {
                System.out.println("Quantidade invalida.");
                continue;
            }

            try {
                pedidoService.adicionarProduto(
                    pedido, codigo, quantidade
                );
                System.out.println("\nProduto adicionado!");
            } catch (ArithmeticException e) {
                System.out.println("Quantidade excede o limite.");
                continue;
            }

            if (!lerSimNao("Adicionar outro produto? (S/N): ")) {
                break;
            }
        }

        if (pedido.estaVazio()) {
            System.out.println("\nPedido vazio. Operacao cancelada.");
            return;
        }

        exibirResumoPedido(pedido);

        if (!lerSimNao("\nConfirmar pedido? (S/N): ")) {
            System.out.println("Pedido cancelado.");
            return;
        }

        processarPagamento(pedido);
    }

    private void exibirResumoPedido(Pedido pedido) {
        System.out.println("\n========== RESUMO DO PEDIDO ==========");

        for (ItemPedido item : pedido.getItens()) {
            System.out.printf(
                "%dx %-22s R$ %6.2f%n",
                item.getQuantidade(),
                item.getProduto().getNome(),
                item.calcularSubtotal()
            );
        }

        BigDecimal subtotal = pedido.calcularSubtotal();
        BigDecimal desconto = caixaService.calcularDesconto(pedido);
        BigDecimal total = caixaService.calcularTotal(pedido);

        System.out.println("--------------------------------------");
        System.out.printf("Subtotal: R$ %.2f%n", subtotal);
        System.out.printf("Desconto: R$ %.2f%n", desconto);
        System.out.printf("TOTAL:    R$ %.2f%n", total);
        System.out.println("======================================");
    }

    private void processarPagamento(Pedido pedido) {
        BigDecimal total = caixaService.calcularTotal(pedido);

        System.out.println("\n========== PAGAMENTO ==========");
        System.out.printf("Total a pagar: R$ %.2f%n", total);
        System.out.println("1 - Dinheiro");
        System.out.println("2 - Pix");
        System.out.println("3 - Cartao");
        System.out.println("0 - Cancelar");

        FormaPagamento formaPagamento;

        while (true) {
            int opcao = lerInteiro("Forma de pagamento: ");

            if (opcao == 0) {
                System.out.println("Pagamento cancelado.");
                return;
            }

            if (opcao == 1) {
                formaPagamento = FormaPagamento.DINHEIRO;
                break;
            } else if (opcao == 2) {
                formaPagamento = FormaPagamento.PIX;
                break;
            } else if (opcao == 3) {
                formaPagamento = FormaPagamento.CARTAO;
                break;
            }

            System.out.println("Forma de pagamento invalida.");
        }

        BigDecimal valorRecebido = total;

        if (formaPagamento == FormaPagamento.DINHEIRO) {
            while (true) {
                valorRecebido = lerDinheiro(
                    "Valor recebido (ex: 50.00): R$ "
                );

                if (valorRecebido.compareTo(total) >= 0) {
                    break;
                }

                System.out.println(
                    "Valor insuficiente. Total: R$ " + total
                );
            }
        }

        Venda venda = caixaService.finalizarVenda(
            pedido, formaPagamento, valorRecebido
        );

        exibirComprovante(venda);
    }

    private void exibirComprovante(Venda venda) {
        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        System.out.println("\n======================================");
        System.out.println("          COFFEE SPOT");
        System.out.println("        COMPROVANTE DE VENDA");
        System.out.println("======================================");
        System.out.println("Venda: #" + venda.getNumero());
        System.out.println("Data: " + venda.getDataHora().format(formato));

        for (ItemPedido item : venda.getItens()) {
            System.out.printf(
                "%dx %-22s R$ %6.2f%n",
                item.getQuantidade(),
                item.getProduto().getNome(),
                item.calcularSubtotal()
            );
        }

        System.out.println("--------------------------------------");
        System.out.printf("Subtotal: R$ %.2f%n", venda.getSubtotal());
        System.out.printf("Desconto: R$ %.2f%n", venda.getDesconto());
        System.out.printf("TOTAL:    R$ %.2f%n", venda.getTotal());
        System.out.println("Pagamento: " + venda.getFormaPagamento());

        if (venda.getFormaPagamento() == FormaPagamento.DINHEIRO) {
            System.out.printf(
                "Recebido: R$ %.2f%n", venda.getValorRecebido()
            );
            System.out.printf("Troco:    R$ %.2f%n", venda.getTroco());
        }

        System.out.println("======================================");
        System.out.println("       Obrigado pela preferencia!");
        System.out.println("======================================");
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido.");
            }
        }
    }

    private BigDecimal lerDinheiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim()
                    .replace(",", ".");

            try {
                BigDecimal valor = new BigDecimal(entrada);

                if (valor.signum() >= 0 && valor.scale() <= 2) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // Entrada invalida: mensagem exibida abaixo.
            }

            System.out.println(
                "Valor invalido. Use ate duas casas decimais."
            );
        }
    }

    private boolean lerSimNao(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = scanner.nextLine().trim();

            if (resposta.equalsIgnoreCase("S")) {
                return true;
            }

            if (resposta.equalsIgnoreCase("N")) {
                return false;
            }

            System.out.println("Digite S ou N.");
        }
    }

    private void consultarVendas() {

        System.out.println("\n========== HISTORICO DE VENDAS ==========");

        if (vendaRepository.listarTodas().isEmpty()) {
            System.out.println("Nenhuma venda registrada.");
            return;
        }

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Venda venda : vendaRepository.listarTodas()) {

            System.out.printf(
                "#%03d | %s | %-8s | R$ %.2f%n",
                venda.getNumero(),
                venda.getDataHora().format(formato),
                venda.getFormaPagamento(),
                venda.getTotal()
            );
        }

        System.out.println("=========================================");

        int numero = lerInteiro(
            "\nNumero da venda para ver comprovante (0 para voltar): "
        );

        if (numero == 0) {
            return;
        }

        Venda venda = vendaRepository.buscarPorNumero(numero);

        if (venda == null) {
            System.out.println("Venda nao encontrada.");
            return;
        }

        exibirComprovante(venda);
    }

    private void fecharCaixa() {
        RelatorioCaixa relatorio = caixaService.gerarRelatorio();

        System.out.println("\n========================================");
        System.out.println("          COFFEE SPOT");
        System.out.println("       FECHAMENTO DE CAIXA");
        System.out.println("========================================");
        System.out.printf("Vendas realizadas: %d%n", relatorio.getQuantidadeVendas());
        System.out.println("----------------------------------------");
        System.out.printf("Subtotal bruto:  R$ %.2f%n", relatorio.getSubtotalBruto());
        System.out.printf("Descontos:       R$ %.2f%n", relatorio.getTotalDescontos());
        System.out.printf("Faturamento:     R$ %.2f%n", relatorio.getFaturamento());
        System.out.printf("Ticket medio:    R$ %.2f%n", relatorio.getTicketMedio());
        System.out.println("----------------------------------------");
        System.out.println("       FORMAS DE PAGAMENTO");
        System.out.println("----------------------------------------");
        System.out.printf("Dinheiro:        R$ %.2f%n", relatorio.getTotalDinheiro());
        System.out.printf("Pix:             R$ %.2f%n", relatorio.getTotalPix());
        System.out.printf("Cartao:          R$ %.2f%n", relatorio.getTotalCartao());
        System.out.println("========================================");
        System.out.println("Relatorio gerado com sucesso.");
        System.out.println("As vendas permanecem registradas.");
    }

}
