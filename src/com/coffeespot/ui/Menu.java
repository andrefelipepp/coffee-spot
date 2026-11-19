
package com.coffeespot.ui;

import com.coffeespot.model.Produto;
import com.coffeespot.service.CardapioService;

import java.util.Scanner;

public class Menu {

    private final Scanner scanner;
    private final CardapioService cardapioService;

    public Menu() {
        scanner = new Scanner(System.in);
        cardapioService = new CardapioService();
    }

    public void iniciar() {
        int opcao;

        do {
            exibirMenu();
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    System.out.println(
                        "\nNovo pedido - Em desenvolvimento."
                    );
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
        System.out.print("Escolha uma opcao: ");
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

    private int lerOpcao() {

        while (!scanner.hasNextInt()) {
            System.out.println("Entrada invalida!");
            System.out.print("Digite um numero: ");
            scanner.next();
        }

        return scanner.nextInt();
    }
}
