package br.dev.klein.FastFoodfurious.repository;

import br.dev.klein.FastFoodfurious.Pedido;
import br.dev.klein.FastFoodfurious.repository.PedidoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // Busca os pedidos de acordo com o status (Aberto, Pronto, Entregue)
    List<Pedido> findByStatusIgnoreCase(String status);
}
