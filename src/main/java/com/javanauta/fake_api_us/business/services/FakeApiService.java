package com.javanauta.fake_api_us.business.services;

import com.javanauta.fake_api_us.apiv1.dto.ProductsDTO;
import com.javanauta.fake_api_us.business.converter.ProdutoConverter;
import com.javanauta.fake_api_us.infrastructure.client.FakeApiClient;
import com.javanauta.fake_api_us.infrastructure.configs.error.NotificacaoErro;
import com.javanauta.fake_api_us.infrastructure.exceptions.BusinessException;
import com.javanauta.fake_api_us.infrastructure.exceptions.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FakeApiService {
    private final FakeApiClient client;
    private final ProdutoService produtoService;
    private final ProdutoConverter converter;

    @NotificacaoErro
    public List<ProductsDTO> buscaProdutos() {
        try {
            List<ProductsDTO> dto = client.buscaListaProdutos().getProducts();
            dto.forEach(produto -> {
                Boolean retorno = produtoService.existsPorNome(produto.getNome());
                if (retorno.equals(false)) {
                    produtoService.salvaProduto(converter.toEntity(produto));
                } else {
                    throw new ConflictException("Produto já existente no banco de dados " + produto.getNome());
                }
            });
            return produtoService.buscaTodosProdutos();
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar e gravar produtos no banco de dados");
        }
    }
}