
package com.coffeespot.model;

import java.math.BigDecimal;

public class Produto {

    private final int codigo;
    private final String nome;
    private final String categoria;
    private final BigDecimal preco;

    public Produto(int codigo, String nome,
                   String categoria, BigDecimal preco) {

        if (codigo <= 0) {
            throw new IllegalArgumentException("Codigo invalido.");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome invalido.");
        }

        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("Categoria invalida.");
        }

        if (preco == null || preco.signum() < 0) {
            throw new IllegalArgumentException("Preco invalido.");
        }

        this.codigo = codigo;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getPreco() {
        return preco;
    }
}
