package br.dev.klein.FastFoodfurious.repository;

import br.dev.klein.FastFoodfurious.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    // O Spring gera automaticamente a busca no banco baseado no nome do método!
    // O "IgnoreCase" ajuda a ignorar se o cliente digitou Lanche, LANCHE ou lanche.
    List<Produto> findByCategoriaIgnoreCase(String categoria);
}