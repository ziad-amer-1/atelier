package com.atelier.service;

import com.atelier.dto.ProductDTO;
import com.atelier.entity.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    String createNewProduct(ProductDTO product) throws IOException;
    String deleteProduct(Long id) throws IOException;
    List<Product> getAllProducts(HttpServletRequest request);
    String updateSingleProduct(Long id, Product product);
}
