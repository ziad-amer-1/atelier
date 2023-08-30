package com.atelier.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record CreateOrderDTO(
    String token,
    List<Map<String, Object>> productOrderList

) {
    public CreateOrderDTO withToken(String token) {
        return new CreateOrderDTO(token, productOrderList());
    }
}
