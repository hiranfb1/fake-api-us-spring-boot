package com.javanauta.fake_api_us.infrastructure.repositories;

import com.javanauta.fake_api_us.infrastructure.entities.ProdutoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, String> {
    ProdutoEntity findByNome(String nome);

    Boolean existsByNome(String nome);

    @Transactional
    void deleteByNome(String nome);
}