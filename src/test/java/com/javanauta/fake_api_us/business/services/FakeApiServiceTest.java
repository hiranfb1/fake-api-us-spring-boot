package com.javanauta.fake_api_us.business.services;

import com.javanauta.fake_api_us.apiv1.dto.DummyJsonResponseDTO;
import com.javanauta.fake_api_us.apiv1.dto.ProductsDTO;
import com.javanauta.fake_api_us.business.converter.ProdutoConverter;
import com.javanauta.fake_api_us.infrastructure.client.FakeApiClient;
import com.javanauta.fake_api_us.infrastructure.entities.ProdutoEntity;
import com.javanauta.fake_api_us.infrastructure.exceptions.BusinessException;
import com.javanauta.fake_api_us.infrastructure.exceptions.ConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FakeApiServiceTest {
    @InjectMocks
    FakeApiService service;

    @Mock
    FakeApiClient client;

    @Mock
    ProdutoService produtoService;

    @Mock
    ProdutoConverter converter;

    @Test
    void deveBuscarProdutoEGravarComSucesso() {
        List<ProductsDTO> listaProdutosDTO = new ArrayList<>();
        ProductsDTO produtoDTO = ProductsDTO.builder().entityId("12345").nome("Jaqueta vermelha").preco(new BigDecimal(250.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais e listras").build();
        listaProdutosDTO.add(produtoDTO);
        DummyJsonResponseDTO dummyResponse = DummyJsonResponseDTO.builder().products(listaProdutosDTO).build();
        ProdutoEntity produtoEntity = ProdutoEntity.builder().id("12345").nome("Jaqueta vermelha").preco(new BigDecimal(250.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais e listras").build();

        when(client.buscaListaProdutos()).thenReturn(dummyResponse);
        when(produtoService.existsPorNome(produtoDTO.getNome())).thenReturn(false);
        when(converter.toEntity(produtoDTO)).thenReturn(produtoEntity);
        when(produtoService.salvaProduto(produtoEntity)).thenReturn(produtoEntity);
        when(produtoService.buscaTodosProdutos()).thenReturn(listaProdutosDTO);

        List<ProductsDTO> listaProdutosDTORetorno = service.buscaProdutos();
        assertEquals(listaProdutosDTO, listaProdutosDTORetorno);

        verify(client).buscaListaProdutos();
        verify(produtoService).existsPorNome(produtoDTO.getNome());
        verify(converter).toEntity(produtoDTO);
        verify(produtoService).salvaProduto(produtoEntity);
        verify(produtoService).buscaTodosProdutos();
        verifyNoMoreInteractions(client, produtoService, converter);
    }

    @Test
    void deveBuscarProdutoENaoGravarCasoRetornoTrue() {
        List<ProductsDTO> listaProdutosDTO = new ArrayList<>();
        ProductsDTO produtoDTO = ProductsDTO.builder().entityId("12345").nome("Jaqueta vermelha").preco(new BigDecimal(250.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais e listras").build();
        listaProdutosDTO.add(produtoDTO);
        DummyJsonResponseDTO dummyResponse = DummyJsonResponseDTO.builder().products(listaProdutosDTO).build();
        ProdutoEntity produtoEntity = ProdutoEntity.builder().id("12345").nome("Jaqueta vermelha").preco(new BigDecimal(250.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais e listras").build();

        when(client.buscaListaProdutos()).thenReturn(dummyResponse);
        when(produtoService.existsPorNome(produtoDTO.getNome())).thenReturn(true);

        ConflictException e = assertThrows(ConflictException.class, () -> service.buscaProdutos());
        assertThat(e.getMessage(), is("Produto já existente no banco de dados Jaqueta vermelha"));

        verify(client).buscaListaProdutos();
        verify(produtoService).existsPorNome(produtoDTO.getNome());
        verifyNoMoreInteractions(client, produtoService);
        verifyNoInteractions(converter);
    }

    @Test
    void deveGerarExceptionCasoErroAoBuscarProdutosViaClient() {
        List<ProductsDTO> listaProdutosDTO = new ArrayList<>();
        ProductsDTO produtoDTO = ProductsDTO.builder().entityId("12345").nome("Jaqueta vermelha").preco(new BigDecimal(250.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais e listras").build();
        listaProdutosDTO.add(produtoDTO);
        DummyJsonResponseDTO dummyResponse = DummyJsonResponseDTO.builder().products(listaProdutosDTO).build();
        ProdutoEntity produtoEntity = ProdutoEntity.builder().id("12345").nome("Jaqueta vermelha").preco(new BigDecimal(250.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais e listras").build();

        when(client.buscaListaProdutos()).thenThrow(new RuntimeException(("Erro ao buscar lista de produtos")));
        BusinessException e = assertThrows(BusinessException.class, () -> service.buscaProdutos());
        assertThat(e.getMessage(), is("Erro ao buscar e gravar produtos no banco de dados"));

        verify(client).buscaListaProdutos();
        verifyNoMoreInteractions(client);
        verifyNoInteractions(converter, produtoService);
    }
}