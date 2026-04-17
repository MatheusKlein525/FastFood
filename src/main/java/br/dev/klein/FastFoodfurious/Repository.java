package br.dev.klein.FastFoodfurious;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<Pedido, Long> {
}