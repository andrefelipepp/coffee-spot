
package com.coffeespot.service;

import com.coffeespot.model.Produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardapioService {

    private final List<Produto> produtos;

    public CardapioService() {
        produtos = new ArrayList<>();
        carregarCardapio();
    }

    private void carregarCardapio() {

        // Cafes
        adicionar(1, "Espresso", "Cafes", "6.00");
        adicionar(2, "Cappuccino", "Cafes", "12.00");
        adicionar(3, "Latte", "Cafes", "11.00");
        adicionar(4, "Mocha", "Cafes", "14.00");

        // Bebidas
        adicionar(5, "Chocolate quente", "Bebidas", "13.00");
        adicionar(6, "Cha gelado", "Bebidas", "8.00");
        adicionar(7, "Suco natural", "Bebidas", "10.00");

        // Salgados
        adicionar(8, "Pao de queijo", "Salgados", "7.00");
        adicionar(9, "Croissant", "Salgados", "12.00");
        adicionar(10, "Sanduiche natural", "Salgados", "15.00");

        // Sobremesas
        adicionar(11, "Brownie", "Sobremesas", "9.00");
        adicionar(12, "Cheesecake", "Sobremesas", "16.00");
        adicionar(13, "Cookie", "Sobremesas", "6.00");
    }

    private void adicionar(int codigo, String nome,
                           String categoria, String preco) {

        Produto produto = new Produto(
            codigo,
            nome,
            categoria,
            new BigDecimal(preco)
        );

        produtos.add(produto);
    }

    public List<Produto> listarProdutos() {
        return Collections.unmodifiableList(produtos);
    }

    public Produto buscarPorCodigo(int codigo) {

        for (Produto produto : produtos) {
            if (produto.getCodigo() == codigo) {
                return produto;
            }
        }

        return null;
    }
}
