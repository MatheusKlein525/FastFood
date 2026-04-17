package br.dev.klein.FastFoodfurious.controller;

import br.dev.klein.FastFoodfurious.Pedido;
import br.dev.klein.FastFoodfurious.controller.PedidoController;
import br.dev.klein.FastFoodfurious.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fastfurious/pedido")
public class PedidoController {

    @Autowired
    private PedidoRepository repository;

    // GET /pedido : Exibe lista geral de pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    // GET /pedido/{id} : Exibe pedido com id específico
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        Optional<Pedido> pedido = repository.findById(id);
        
        if (pedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(pedido.get());
    }

    // GET /pedido/status/{status} : Exibe pedidos com status específico
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pedido>> buscarPorStatus(@PathVariable String status) {
        List<Pedido> pedidos = repository.findByStatusIgnoreCase(status);
        
        if (pedidos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(pedidos);
    }

    // POST /pedido : Adiciona pedido (Recebe o carrinho e fica ABERTO)
    @PostMapping
    public ResponseEntity<Pedido> adicionar(@RequestBody Pedido pedido) {
        pedido.setStatus("ABERTO"); 
        Pedido novoPedido = repository.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

    // PUT /pedido/{id} : Altera pedido (Refatorado conforme o print)
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
        // Verifica se o pedido existe
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        pedido.setId(id);
        pedido = repository.save(pedido);
        return ResponseEntity.ok(pedido);
    }

    // DELETE /pedido/{id} : CANCELA pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Pedido> cancelarPedido(@PathVariable Long id) {
        Optional<Pedido> pedidoOpt = repository.findById(id);
        
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pedido pedido = pedidoOpt.get();
        pedido.setStatus("CANCELADO");
        pedido = repository.save(pedido);
        
        return ResponseEntity.ok(pedido);
    }

    // PUT /pedido/status/{id} : Altera o status do Pedido (Fluxo da Cozinha/Entrega)
    @PutMapping("/status/{id}")
    public ResponseEntity<Pedido> avancarStatus(@PathVariable Long id) {
        Optional<Pedido> pedidoOpt = repository.findById(id);
        
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pedido pedido = pedidoOpt.get();
        String statusAtual = pedido.getStatus().toUpperCase();
        
        if ("ABERTO".equals(statusAtual)) {
            pedido.setStatus("PRONTO");
        } else if ("PRONTO".equals(statusAtual)) {
            pedido.setStatus("ENTREGUE");
        } else {
            return ResponseEntity.badRequest().body(pedido); 
        }
        
        pedido = repository.save(pedido);
        return ResponseEntity.ok(pedido);
    }
}