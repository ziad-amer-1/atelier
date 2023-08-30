package com.atelier.service;

import com.atelier.dto.CreateOrderDTO;
import com.atelier.entity.UserOrder;

import java.util.List;

public interface OrderService {

    String createOrder(CreateOrderDTO createOrderDTO);

    List<UserOrder> getAllOrdersInSpecificDate(String localDate, String status);

    String changeOrderStatus(Long id, String status);

}
