package com.javanauta.fake_api_us.business.converter;

import com.javanauta.fake_api_us.apiv1.dto.ProductsDTO;
import com.javanauta.fake_api_us.infrastructure.entities.ProdutoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ProdutoConverterTest {
    @InjectMocks
    ProdutoConverter converter;

    @Test
    void deveConverterParaProdutoEntityComSucesso() {
        ProductsDTO productsDTO = ProductsDTO.builder().nome("Jaqueta vermelha").preco(new BigDecimal(500.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais").build();
        ProdutoEntity produtoEntityEsperado = ProdutoEntity.builder().id("12345").nome("Jaqueta vermelha").preco(new BigDecimal(500.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais").build();
        ProdutoEntity produtoEntity = converter.toEntity(productsDTO);

        assertEquals(produtoEntityEsperado.getNome(), produtoEntity.getNome());
        assertEquals(produtoEntityEsperado.getPreco(), produtoEntity.getPreco());
        assertEquals(produtoEntityEsperado.getCategoria(), produtoEntity.getCategoria());
        assertEquals(produtoEntityEsperado.getDescricao(), produtoEntity.getDescricao());
        assertEquals(produtoEntityEsperado.getImagem(), produtoEntity.getImagem());
        assertNotNull(produtoEntity.getId());
        assertNotNull(produtoEntity.getDataInclusao());
    }

    @Test
    void deveConverterParaProdutoEntityUpdateComSucesso() {
        ProdutoEntity entity = ProdutoEntity.builder().id("12345").nome("Jaqueta vermelha").preco(new BigDecimal(500.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais").build();
        ProductsDTO productsDTO = ProductsDTO.builder().preco(new BigDecimal(250.00)).descricao("Jaqueta vermelha com bolsos laterais e listras").build();
        ProdutoEntity produtoEntityEsperado = ProdutoEntity.builder().id("12345").nome("Jaqueta vermelha").preco(new BigDecimal(250.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais e listras").build();
        String id = "12345";
        ProdutoEntity produtoEntity = converter.toEntityUpdate(entity, productsDTO, id);

        assertEquals(produtoEntityEsperado.getNome(), produtoEntity.getNome());
        assertEquals(produtoEntityEsperado.getPreco(), produtoEntity.getPreco());
        assertEquals(produtoEntityEsperado.getCategoria(), produtoEntity.getCategoria());
        assertEquals(produtoEntityEsperado.getDescricao(), produtoEntity.getDescricao());
        assertEquals(produtoEntityEsperado.getImagem(), produtoEntity.getImagem());
        assertEquals(produtoEntityEsperado.getId(), produtoEntity.getId());
        assertEquals(produtoEntityEsperado.getDataInclusao(), produtoEntity.getDataInclusao());
        assertNotNull(produtoEntity.getDataAtualizacao());
    }


    @Test
    void deveConverterParaListaProdutoDTOComSucesso() {
        List<ProductsDTO> listaProdutosDTO = new ArrayList<>();
        List<ProdutoEntity> listaProdutoEntity = new ArrayList<>();

        ProductsDTO produtoDTO = ProductsDTO.builder().entityId("12345").nome("Jaqueta vermelha").preco(new BigDecimal(250.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais e listras").build();
        listaProdutosDTO.add(produtoDTO);

        ProdutoEntity produtoEntityEsperado = ProdutoEntity.builder().id("12345").nome("Jaqueta vermelha").preco(new BigDecimal(250.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais e listras").build();
        listaProdutoEntity.add(produtoEntityEsperado);

        List<ProductsDTO> productoDTO = converter.toListDTO(listaProdutoEntity);
        assertEquals(listaProdutosDTO, productoDTO);
    }
}