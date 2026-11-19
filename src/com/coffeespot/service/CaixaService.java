
package com.coffeespot.service;

import com.coffeespot.model.FormaPagamento;
import com.coffeespot.model.Pedido;
import com.coffeespot.model.Venda;
import com.coffeespot.repository.VendaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CaixaService {

    private final VendaRepository vendaRepository;

    public CaixaService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    public BigDecimal calcularDesconto(Pedido pedido) {

        BigDecimal subtotal = pedido.calcularSubtotal();
        BigDecimal percentual;

        if (subtotal.compareTo(new BigDecimal("100.00")) >= 0) {
            percentual = new BigDecimal("0.15");
        } else if (subtotal.compareTo(new BigDecimal("60.00")) >= 0) {
            percentual = new BigDecimal("0.10");
        } else if (subtotal.compareTo(new BigDecimal("30.00")) >= 0) {
            percentual = new BigDecimal("0.05");
        } else {
            percentual = BigDecimal.ZERO;
        }

        return subtotal.multiply(percentual)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularTotal(Pedido pedido) {
        return pedido.calcularSubtotal()
                .subtract(calcularDesconto(pedido));
    }

    public Venda finalizarVenda(
            Pedido pedido,
            FormaPagamento formaPagamento,
            BigDecimal valorRecebido) {

        if (pedido == null || pedido.estaVazio()) {
            throw new IllegalArgumentException("Pedido vazio.");
        }

        BigDecimal desconto = calcularDesconto(pedido);

        Venda venda = new Venda(
            vendaRepository.proximoNumero(),
            pedido,
            desconto,
            formaPagamento,
            valorRecebido
        );

        vendaRepository.salvar(venda);
        return venda;
    }
}
