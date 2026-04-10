package com.javanauta.fake_api_us.infrastructure.client;

import com.javanauta.fake_api_us.apiv1.dto.DummyJsonResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "fake-api", url = "${fake-api.url:#{null}}")
public interface FakeApiClient {
    @GetMapping("/products")
    DummyJsonResponseDTO buscaListaProdutos();
}