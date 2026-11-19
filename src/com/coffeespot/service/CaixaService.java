
package com.coffeespot.service;

import com.coffeespot.model.FormaPagamento;
import com.coffeespot.model.Pedido;
import com.coffeespot.model.RelatorioCaixa;
import com.coffeespot.model.Venda;
import com.coffeespot.repository.VendaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CaixaService {

    private final VendaRepository vendaRepository;

    public CaixaService(VendaRepository vendaRepository) {
        if (vendaRepository == null) {
            throw new IllegalArgumentException(
                "Repositorio obrigatorio."
            );
        }

        this.vendaRepository = vendaRepository;
    }

    public BigDecimal calcularDesconto(Pedido pedido) {

        if (pedido == null) {
            throw new IllegalArgumentException("Pedido invalido.");
        }

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

    public RelatorioCaixa gerarRelatorio() {

        BigDecimal subtotalBruto = BigDecimal.ZERO;
        BigDecimal totalDescontos = BigDecimal.ZERO;
        BigDecimal faturamento = BigDecimal.ZERO;

        BigDecimal totalDinheiro = BigDecimal.ZERO;
        BigDecimal totalPix = BigDecimal.ZERO;
        BigDecimal totalCartao = BigDecimal.ZERO;

        int quantidadeVendas = 0;

        for (Venda venda : vendaRepository.listarTodas()) {

            quantidadeVendas++;

            subtotalBruto = subtotalBruto.add(
                venda.getSubtotal()
            );

            totalDescontos = totalDescontos.add(
                venda.getDesconto()
            );

            faturamento = faturamento.add(
                venda.getTotal()
            );

            switch (venda.getFormaPagamento()) {
                case DINHEIRO:
                    totalDinheiro = totalDinheiro.add(
                        venda.getTotal()
                    );
                    break;

                case PIX:
                    totalPix = totalPix.add(
                        venda.getTotal()
                    );
                    break;

                case CARTAO:
                    totalCartao = totalCartao.add(
                        venda.getTotal()
                    );
                    break;
            }
        }

        BigDecimal ticketMedio = BigDecimal.ZERO;

        if (quantidadeVendas > 0) {
            ticketMedio = faturamento.divide(
                BigDecimal.valueOf(quantidadeVendas),
                2,
                RoundingMode.HALF_UP
            );
        }

        return new RelatorioCaixa(
            quantidadeVendas,
            subtotalBruto,
            totalDescontos,
            faturamento,
            totalDinheiro,
            totalPix,
            totalCartao,
            ticketMedio
        );
    }
}
