package com.javanauta.fake_api_us.apiv1.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DummyJsonResponseDTO {
    private List<ProductsDTO> products;
}