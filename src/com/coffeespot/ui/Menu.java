
package com.coffeespot.ui;

import java.util.Scanner;

public class Menu {

    private final Scanner scanner;

    public Menu() {
        scanner = new Scanner(System.in);
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
                    System.out.println(
                        "\nCardapio - Em desenvolvimento."
                    );
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

    private int lerOpcao() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada invalida!");
            System.out.print("Digite um numero: ");
            scanner.next();
        }

        return scanner.nextInt();
    }
}
