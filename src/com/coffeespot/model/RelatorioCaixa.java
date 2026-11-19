
package com.coffeespot.model;

import java.math.BigDecimal;

public class RelatorioCaixa {

    private final int quantidadeVendas;
    private final BigDecimal subtotalBruto;
    private final BigDecimal totalDescontos;
    private final BigDecimal faturamento;
    private final BigDecimal totalDinheiro;
    private final BigDecimal totalPix;
    private final BigDecimal totalCartao;
    private final BigDecimal ticketMedio;

    public RelatorioCaixa(
            int quantidadeVendas,
            BigDecimal subtotalBruto,
            BigDecimal totalDescontos,
            BigDecimal faturamento,
            BigDecimal totalDinheiro,
            BigDecimal totalPix,
            BigDecimal totalCartao,
            BigDecimal ticketMedio) {

        this.quantidadeVendas = quantidadeVendas;
        this.subtotalBruto = subtotalBruto;
        this.totalDescontos = totalDescontos;
        this.faturamento = faturamento;
        this.totalDinheiro = totalDinheiro;
        this.totalPix = totalPix;
        this.totalCartao = totalCartao;
        this.ticketMedio = ticketMedio;
    }

    public int getQuantidadeVendas() {
        return quantidadeVendas;
    }

    public BigDecimal getSubtotalBruto() {
        return subtotalBruto;
    }

    public BigDecimal getTotalDescontos() {
        return totalDescontos;
    }

    public BigDecimal getFaturamento() {
        return faturamento;
    }

    public BigDecimal getTotalDinheiro() {
        return totalDinheiro;
    }

    public BigDecimal getTotalPix() {
        return totalPix;
    }

    public BigDecimal getTotalCartao() {
        return totalCartao;
    }

    public BigDecimal getTicketMedio() {
        return ticketMedio;
    }
}
