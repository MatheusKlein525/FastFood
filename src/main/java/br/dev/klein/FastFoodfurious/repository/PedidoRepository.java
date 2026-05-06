package br.dev.klein.FastFoodfurious.repository;

import br.dev.klein.FastFoodfurious.Pedido;
import br.dev.klein.FastFoodfurious.repository.PedidoRepository;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByStatusIgnoreCase(String status);
    // Busca todos os pedidos que já foram entregues
List<Pedido> findByStatus(String status);
List<Pedido> findByDataCriacaoBetween(LocalDateTime inicio, LocalDateTime fim);
}
// Busca todos os pedidos que já foram entregues
