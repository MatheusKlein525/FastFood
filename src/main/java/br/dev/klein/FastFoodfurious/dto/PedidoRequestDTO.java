package br.dev.klein.FastFoodfurious.dto;

import java.util.List;

public class PedidoRequestDTO {
    private List<Long> produtosIds;

    // Getter e Setter
    public List<Long> getProdutosIds() { return produtosIds; }
    public void setProdutosIds(List<Long> produtosIds) { this.produtosIds = produtosIds; }
}