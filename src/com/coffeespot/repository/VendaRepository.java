
package com.coffeespot.repository;

import com.coffeespot.model.Venda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VendaRepository {

    private final List<Venda> vendas = new ArrayList<>();

    public void salvar(Venda venda) {
        if (venda == null) {
            throw new IllegalArgumentException("Venda invalida.");
        }

        vendas.add(venda);
    }

    public List<Venda> listarTodas() {
        return Collections.unmodifiableList(vendas);
    }

    public int proximoNumero() {
        return vendas.size() + 1;
    }
}
