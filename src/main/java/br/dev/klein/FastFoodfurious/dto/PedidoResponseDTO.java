package br.dev.klein.FastFoodfurious.dto;

import java.util.List;

public class PedidoResponseDTO {
    private Long id;
    private String status;
    private Double valorTotal;
    private List<String> nomesProdutos; // Simplificado: apenas os nomes
    private String dataCriacao; // Adicione este campo

// No construtor:
public PedidoResponseDTO(Long id, String status, Double valorTotal, List<String> nomesProdutos, String dataCriacao) {
    this.id = id;
    this.status = status;
    this.valorTotal = valorTotal;
    this.nomesProdutos = nomesProdutos;
    this.dataCriacao = dataCriacao;
}

    // Construtor, Getters e Setters
    public PedidoResponseDTO(Long id, String status, Double valorTotal, List<String> nomesProdutos) {
        this.id = id;
        this.status = status;
        this.valorTotal = valorTotal;
        this.nomesProdutos = nomesProdutos;
    }

    // Getters...
    public Long getId() { return id; }
    public String getStatus() { return status; }
    public Double getValorTotal() { return valorTotal; }
    public List<String> getNomesProdutos() { return nomesProdutos; }
}