
package com.coffeespot.ui;

import com.coffeespot.model.ItemPedido;
import com.coffeespot.model.Pedido;
import com.coffeespot.model.Produto;
import com.coffeespot.service.CardapioService;
import com.coffeespot.service.PedidoService;

import java.util.Scanner;

public class Menu {

    private final Scanner scanner;
    private final CardapioService cardapioService;
    private final PedidoService pedidoService;

    public Menu() {
        scanner = new Scanner(System.in);
        cardapioService = new CardapioService();
        pedidoService = new PedidoService(cardapioService);
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
                    System.out.println(
                        "\nConsultar vendas - Em desenvolvimento."
                    );
                    break;

                case 4:
                    System.out.println(
                        "\nFechar caixa - Em desenvolvimento."
                    );
                    break;

                case 0:
                    System.out.println(
                        "\nEncerrando Coffee Spot..."
                    );
                    break;

                default:
                    System.out.println(
                        "\nOpcao invalida. Tente novamente."
                    );
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

        System.out.println("\n==============================");
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
                System.out.println(
                    "A quantidade deve ser maior que zero."
                );
                continue;
            }

            try {
                pedidoService.adicionarProduto(
                    pedido, codigo, quantidade
                );

                System.out.println(
                    "\nProduto adicionado com sucesso!"
                );
            } catch (ArithmeticException e) {
                System.out.println(
                    "Quantidade excede o limite permitido."
                );
                continue;
            }

            String resposta;

            do {
                resposta = lerTexto(
                    "Adicionar outro produto? (S/N): "
                );

                if (!resposta.equalsIgnoreCase("S")
                        && !resposta.equalsIgnoreCase("N")) {
                    System.out.println(
                        "Resposta invalida. Digite S ou N."
                    );
                }

            } while (!resposta.equalsIgnoreCase("S")
                    && !resposta.equalsIgnoreCase("N"));

            if (resposta.equalsIgnoreCase("N")) {
                break;
            }
        }

        if (pedido.estaVazio()) {
            System.out.println("\nNenhum produto adicionado.");
            return;
        }

        exibirResumoPedido(pedido);

        System.out.println(
            "\nPedido montado. Pagamento sera implementado na proxima etapa."
        );
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

        System.out.println("--------------------------------------");

        System.out.printf(
            "SUBTOTAL: R$ %.2f%n",
            pedido.calcularSubtotal()
        );

        System.out.println("======================================");
    }

    private int lerInteiro(String mensagem) {

        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println(
                    "Entrada invalida. Digite um numero inteiro."
                );
            }
        }
    }

    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }
}
