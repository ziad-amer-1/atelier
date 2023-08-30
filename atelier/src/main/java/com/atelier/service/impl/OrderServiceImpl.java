package com.atelier.service.impl;

import com.atelier.constants.AtelierConstants;
import com.atelier.dto.CreateOrderDTO;
import com.atelier.entity.AppUser;
import com.atelier.entity.ProductOrder;
import com.atelier.entity.UserOrder;
import com.atelier.repository.OrderRepo;
import com.atelier.repository.ProductOrderRepo;
import com.atelier.repository.ProductRepo;
import com.atelier.repository.UserRepo;
import com.atelier.service.OrderService;
import com.atelier.utils.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final ProductOrderRepo productOrderRepo;
    private final UserRepo userRepo;
    private final JwtTokenProvider tokenProvider;
    private final ProductRepo productRepo;

    @Override
    @Transactional
    @Modifying
    public String createOrder(CreateOrderDTO createOrderDTO) {

        log.info("token: " + createOrderDTO.token());

        Claims tokenClaims = tokenProvider.extractClaims(createOrderDTO.token().split(" ")[1]);
        AppUser user = userRepo.findById(Long.valueOf((String) tokenClaims.get("userId"))).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info("order to user: " + user.getUsername());
        UserOrder order = orderRepo.save(new UserOrder(user, LocalDate.now(), LocalTime.now(), "active"));

        List<ProductOrder> productOrders = new ArrayList<>();

        for (Map<String, Object> orderDTO : createOrderDTO.productOrderList()) {
            ProductOrder productOrder = new ProductOrder();
            productOrder.setOrder(order);
            Number intProductId = (int) orderDTO.get("productId");
            Number intQuantity = (int) orderDTO.get("quantity");
            Long productId = intProductId.longValue();
            int quantity = intQuantity.intValue();
            productOrder.setProduct(productRepo.findById(productId).orElseThrow(() -> new IllegalStateException("Product with id = " + productId + " not exist")));
            productOrder.setQuantity(quantity);
            productOrderRepo.save(productOrder);
            productOrders.add(productOrder);
        }
        order.setProductOrders(productOrders);
        List<UserOrder> userOrders  = user.getOrders();
        userOrders.add(order);
        user.setOrders(userOrders);

        return "Order created Successfully to " + user.getUsername();
    }

    @Override
    @Cacheable("orders")
    public List<UserOrder> getAllOrdersInSpecificDate(String localDate, String status) {
        if (status != null) {
            status = status.toLowerCase();
        }
        return orderRepo.getAllOrdersInSpecificDate(localDate, status);
    }

    @Override
    @Transactional
    public String changeOrderStatus(Long id, String status) {
        UserOrder order = orderRepo.findById(id).orElseThrow(() -> new IllegalStateException("Order with id = " + id + " not exist"));
        order.setStatus(status.toLowerCase());
        return "status for the order with the id = " + id + " updated to " + status;
    }
}
