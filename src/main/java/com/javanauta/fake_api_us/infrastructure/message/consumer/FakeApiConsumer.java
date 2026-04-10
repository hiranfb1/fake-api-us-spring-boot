package com.javanauta.fake_api_us.infrastructure.message.consumer;

import com.javanauta.fake_api_us.apiv1.dto.ProductsDTO;
import com.javanauta.fake_api_us.business.services.ProdutoService;
import com.javanauta.fake_api_us.infrastructure.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FakeApiConsumer {
    private final ProdutoService produtoService;

    @KafkaListener(topics = "${topico.fake-api.consumer.nome}", groupId = "${topico.fake-api.consumer.group-id}")
    public void recebeProdutosDTO(ProductsDTO productsDTO) {
        try {
            produtoService.salvaProdutoConsumer(productsDTO);
        } catch (Exception exception) {
            throw new BusinessException("Erro ao consumir mensagem do Kafka ");
        }
    }
}