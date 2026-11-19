
package com.coffeespot.service;

import com.coffeespot.model.Pedido;
import com.coffeespot.model.Produto;

public class PedidoService {

    private final CardapioService cardapioService;

    public PedidoService(CardapioService cardapioService) {
        if (cardapioService == null) {
            throw new IllegalArgumentException(
                "Cardapio obrigatorio."
            );
        }

        this.cardapioService = cardapioService;
    }

    public Pedido criarPedido() {
        return new Pedido();
    }

    public boolean adicionarProduto(
            Pedido pedido, int codigo, int quantidade) {

        if (pedido == null || quantidade <= 0) {
            throw new IllegalArgumentException(
                "Pedido ou quantidade invalida."
            );
        }

        Produto produto = cardapioService.buscarPorCodigo(codigo);

        if (produto == null) {
            return false;
        }

        pedido.adicionarProduto(produto, quantidade);
        return true;
    }
}
