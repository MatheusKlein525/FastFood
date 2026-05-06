package br.dev.klein.FastFoodfurious.controller;

import br.dev.klein.FastFoodfurious.Pedido;
import br.dev.klein.FastFoodfurious.dto.PedidoRequestDTO;
import br.dev.klein.FastFoodfurious.dto.PedidoResponseDTO;
import br.dev.klein.FastFoodfurious.model.Produto;
import br.dev.klein.FastFoodfurious.repository.PedidoRepository;
import br.dev.klein.FastFoodfurious.repository.ProdutoRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fastfurious/pedido")
public class PedidoController {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    // --- LISTAR TODOS ---
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    // --- BUSCAR POR ID ---
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(pedido -> ResponseEntity.ok(pedido))
                .orElse(ResponseEntity.notFound().build());
    }

    // --- FATURAMENTO (Colocado antes do ID para evitar conflito) ---
    @GetMapping("/faturamento")
    public ResponseEntity<String> calcularFaturamento() {
        try {
            List<Pedido> pedidosEntregues = repository.findByStatusIgnoreCase("ENTREGUE");
            double total = pedidosEntregues.stream()
                    .mapToDouble(p -> p.getValorTotal() != null ? p.getValorTotal() : 0.0)
                    .sum();
            return ResponseEntity.ok("Faturamento Total: R$ " + total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao calcular: " + e.getMessage());
        }
    }

    // --- ADICIONAR (USANDO DTO) ---
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> adicionar(@RequestBody PedidoRequestDTO dto ) {
        Pedido pedido = new Pedido();
        List<Produto> produtosParaPedido = new ArrayList<>();
        double total = 0.0;

        if (dto.getProdutosIds() == null || dto.getProdutosIds().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        for (Long id : dto.getProdutosIds()) {
            Produto p = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto " + id + " não encontrado"));
            produtosParaPedido.add(p);
            total += p.getPreco();
        }

        pedido.setProdutos(produtosParaPedido);
        pedido.setValorTotal(total);
        pedido.setStatus("ABERTO");
        pedido.setDataCriacao(LocalDateTime.now());

        repository.save(pedido);

        // Converte para DTO de resposta para o cliente
        List<String> nomes = produtosParaPedido.stream()
                .map(Produto::getNome)
                .collect(Collectors.toList());
        
        PedidoResponseDTO response = new PedidoResponseDTO(
                pedido.getId(), 
                pedido.getStatus(), 
                total, 
                nomes
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // --- AVANÇAR STATUS ---
    @PutMapping("/status/{id}")
    public ResponseEntity<Pedido> avancarStatus(@PathVariable Long id) {
        return repository.findById(id).map(pedido -> {
            String statusAtual = pedido.getStatus().toUpperCase();
            
            if ("ABERTO".equals(statusAtual)) {
                pedido.setStatus("PRONTO");
            } else if ("PRONTO".equals(statusAtual)) {
                pedido.setStatus("ENTREGUE");
            } else {
                return ResponseEntity.badRequest().<Pedido>build();
            }
            
            return ResponseEntity.ok(repository.save(pedido));
        }).orElse(ResponseEntity.notFound().build());
    }

    // --- CANCELAR PEDIDO ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Pedido> cancelarPedido(@PathVariable Long id) {
        return repository.findById(id).map(pedido -> {
            pedido.setStatus("CANCELADO");
            repository.save(pedido);
            return ResponseEntity.ok(pedido);
        }).orElse(ResponseEntity.notFound().build());
    }
}