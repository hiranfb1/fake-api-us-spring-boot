package com.javanauta.fake_api_us.business.services;

import com.javanauta.fake_api_us.apiv1.dto.ProductsDTO;
import com.javanauta.fake_api_us.business.converter.ProdutoConverter;
import com.javanauta.fake_api_us.infrastructure.configs.error.NotificacaoErro;
import com.javanauta.fake_api_us.infrastructure.entities.ProdutoEntity;
import com.javanauta.fake_api_us.infrastructure.exceptions.BusinessException;
import com.javanauta.fake_api_us.infrastructure.exceptions.ConflictException;
import com.javanauta.fake_api_us.infrastructure.exceptions.UnprocessableEntityException;
import com.javanauta.fake_api_us.infrastructure.message.producer.FakeApiProducer;
import com.javanauta.fake_api_us.infrastructure.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@NotificacaoErro
public class ProdutoService {
    private final ProdutoRepository repository;
    private final ProdutoConverter converter;
    private final FakeApiProducer producer;

    public List<ProductsDTO> buscaTodosProdutos() {
        try {
            return converter.toListDTO(repository.findAll());
        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar todos produtos " + e);
        }
    }

    public ProductsDTO buscaProdutoPorNome(String nome) {
        try {
            ProdutoEntity produto = repository.findByNome(nome);
            if (Objects.isNull(produto)) {
                throw new UnprocessableEntityException("Não foram encontrados produtos com o nome " + nome);
            }
            return converter.toDTO(produto);
        } catch (UnprocessableEntityException e) {
            throw new UnprocessableEntityException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessException(format("Erro ao buscar produto por nome %s", nome) + e);
        }
    }

    public Boolean existsPorNome(String nome) {
        try {
            return repository.existsByNome(nome);
        } catch (Exception e) {
            throw new BusinessException(format("Erro ao buscar produto por nome %s", nome) + e);
        }
    }

    public ProdutoEntity salvaProduto(ProdutoEntity entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new BusinessException("Erro ao salvar produtos " + e);
        }
    }

    public ProductsDTO salvaProdutoDTO(ProductsDTO dto) {
        try {
            Boolean retorno = existsPorNome(dto.getNome());
            if (retorno.equals(true)) {
                throw new ConflictException("Produto já existente no banco de dados " + dto.getNome());
            }
            ProdutoEntity entity = repository.save(converter.toEntity(dto));
            return converter.toDTO(entity);
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessException("Erro ao salvar produtos " + e);
        }
    }

    public void salvaProdutoConsumer(ProductsDTO dto) {
        try {
            Boolean retorno = existsPorNome(dto.getNome());
            if (retorno.equals(true)) {
                producer.enviaRespostaCadastroProdutos("Produto " + dto.getNome() + " já existente no banco de dados.");
                throw new ConflictException("Produto já existente no banco de dados " + dto.getNome());
            }
            repository.save(converter.toEntity(dto));
            producer.enviaRespostaCadastroProdutos("Produto " + dto.getNome() + " gravado com sucesso.");
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        } catch (Exception e) {
            producer.enviaRespostaCadastroProdutos("Erro ao gravar o produto " + dto.getNome());
            throw new BusinessException("Erro ao salvar produtos " + e);
        }
    }

    public ProductsDTO updateProduto(String id, ProductsDTO dto) {
        try {
            ProdutoEntity entity = repository.findById(id).orElseThrow(() -> new UnprocessableEntityException("Produto não encontrado"));
            salvaProduto(converter.toEntityUpdate(entity, dto, id));
            return converter.toDTO(repository.findByNome(entity.getNome()));
        } catch (UnprocessableEntityException e) {
            throw new UnprocessableEntityException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessException("Erro ao atualizar produto ", e);
        }
    }

    public void deletaProduto(String nome) {
        try {
            Boolean retorno = existsPorNome(nome);
            if (retorno.equals(false)) {
                throw new UnprocessableEntityException("Não foi possível deletar o produto, pois não existe produto com o nome " + nome);
            } else {
                repository.deleteByNome(nome);
            }
        } catch (UnprocessableEntityException e) {
            throw new UnprocessableEntityException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessException(format("Erro ao deletar produto por nome %s", nome), e);
        }
    }
}