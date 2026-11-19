
package com.coffeespot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Venda {

    private final int numero;
    private final LocalDateTime dataHora;
    private final List<ItemPedido> itens;
    private final BigDecimal subtotal;
    private final BigDecimal desconto;
    private final BigDecimal total;
    private final FormaPagamento formaPagamento;
    private final BigDecimal valorRecebido;
    private final BigDecimal troco;

    public Venda(int numero, Pedido pedido,
                 BigDecimal desconto,
                 FormaPagamento formaPagamento,
                 BigDecimal valorRecebido) {

        if (numero <= 0 || pedido == null || pedido.estaVazio()
                || desconto == null || formaPagamento == null
                || valorRecebido == null) {
            throw new IllegalArgumentException("Dados invalidos da venda.");
        }

        this.numero = numero;
        this.dataHora = LocalDateTime.now();

        List<ItemPedido> copia = new ArrayList<>();

        for (ItemPedido item : pedido.getItens()) {
            copia.add(new ItemPedido(
                item.getProduto(),
                item.getQuantidade()
            ));
        }

        this.itens = Collections.unmodifiableList(copia);
        this.subtotal = pedido.calcularSubtotal();

        if (desconto.signum() < 0 ||
                desconto.compareTo(subtotal) > 0) {
            throw new IllegalArgumentException("Desconto invalido.");
        }

        this.desconto = desconto;
        this.total = subtotal.subtract(desconto);
        this.formaPagamento = formaPagamento;

        if (valorRecebido.compareTo(total) < 0) {
            throw new IllegalArgumentException(
                "Valor recebido insuficiente."
            );
        }

        if (formaPagamento != FormaPagamento.DINHEIRO
                && valorRecebido.compareTo(total) != 0) {
            throw new IllegalArgumentException(
                "Pagamento deve corresponder ao total."
            );
        }

        this.valorRecebido = valorRecebido;
        this.troco = valorRecebido.subtract(total);
    }

    public int getNumero() {
        return numero;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public BigDecimal getValorRecebido() {
        return valorRecebido;
    }

    public BigDecimal getTroco() {
        return troco;
    }
}
