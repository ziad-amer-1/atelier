package com.atelier.dto;

import com.atelier.entity.UserOrder;

import java.util.List;

public record UserDTO(Long id, String username, String email, List<UserOrder> orders) {
}
