package com.javanauta.fake_api_us.infrastructure.consumer;

import com.javanauta.fake_api_us.apiv1.dto.ProductsDTO;
import com.javanauta.fake_api_us.business.services.ProdutoService;
import com.javanauta.fake_api_us.infrastructure.exceptions.BusinessException;
import com.javanauta.fake_api_us.infrastructure.message.consumer.FakeApiConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FakeApiConsumerTest {
    @InjectMocks
    FakeApiConsumer consumer;

    @Mock
    ProdutoService service;

    @Test
    void deveReceberMensagemProdutoDTOComSucesso() {
        ProductsDTO produtoDTO = ProductsDTO.builder().entityId("12345").nome("Jaqueta vermelha").preco(new BigDecimal(250.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais e listras").build();
        doNothing().when(service).salvaProdutoConsumer(produtoDTO);
        consumer.recebeProdutosDTO(produtoDTO);

        verify(service).salvaProdutoConsumer(produtoDTO);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deveGerarExceptionCasoErroNoConsumer() {
        ProductsDTO produtoDTO = ProductsDTO.builder().entityId("12345").nome("Jaqueta vermelha").preco(new BigDecimal(250.00)).categoria("Roupas").descricao("Jaqueta vermelha com bolsos laterais e listras").build();
        doThrow(new RuntimeException("Erro ao consumir mensagem")).when(service).salvaProdutoConsumer(produtoDTO);
        BusinessException e = assertThrows(BusinessException.class, () -> consumer.recebeProdutosDTO(produtoDTO));
        assertThat(e.getMessage(), is("Erro ao consumir mensagem do Kafka "));

        verify(service).salvaProdutoConsumer(produtoDTO);
        verifyNoMoreInteractions(service);
    }
}