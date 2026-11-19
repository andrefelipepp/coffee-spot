
package com.coffeespot.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {

    private final List<ItemPedido> itens;

    public Pedido() {
        this.itens = new ArrayList<>();
    }

    public void adicionarProduto(Produto produto, int quantidade) {

        if (produto == null || quantidade <= 0) {
            throw new IllegalArgumentException(
                "Produto ou quantidade invalida."
            );
        }

        for (ItemPedido item : itens) {
            if (item.getProduto().getCodigo() == produto.getCodigo()) {
                item.adicionarQuantidade(quantidade);
                return;
            }
        }

        itens.add(new ItemPedido(produto, quantidade));
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }

    public BigDecimal calcularSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemPedido item : itens) {
            subtotal = subtotal.add(item.calcularSubtotal());
        }

        return subtotal;
    }
}
