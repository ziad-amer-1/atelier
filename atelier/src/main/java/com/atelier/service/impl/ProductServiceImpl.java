package com.atelier.service.impl;

import com.atelier.dto.ProductDTO;
import com.atelier.entity.Product;
import com.atelier.repository.ProductRepo;
import com.atelier.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final ResourceLoader resourceLoader;
    @Override
    public String createNewProduct(ProductDTO product) throws IOException {
        if (
            product.name() == null ||
            product.price() == null ||
            product.availableColors() == null ||
            product.availableSizes() == null ||
            product.productImages().size() == 0
        ) {
            throw new IllegalStateException("provide all the required info");
        }

        List<String> imagesPath = new ArrayList<>();

        for (MultipartFile productImage : product.productImages()) {
            File staticFolder = resourceLoader.getResource("classpath:static/").getFile();
            String filename = productImage.getOriginalFilename();
            Path targetFilePath = Path.of(staticFolder.getAbsolutePath() + "/" +  filename);
            log.info("target file path: " + targetFilePath);
            Files.copy(productImage.getInputStream(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);
            imagesPath.add("/images/" + filename);
        }

        productRepo.save(new Product(product.name(), product.price(), imagesPath, product.availableColors(), product.availableSizes()));
        return "Product added Successfully";
    }

    @Override
    public String deleteProduct(Long id) throws IOException {
        Product product = productRepo.findById(id).orElseThrow(() -> new IllegalStateException("Product not exist"));
        for (String _path : product.getImagesPath()) {
            String[] imageNameInList = _path.split("/images/");
            String imageName = imageNameInList[imageNameInList.length - 1];
            Path imagePath = resourceLoader.getResource("classpath:static/" + imageName).getFile().toPath();
            log.info("ImageName: " + Arrays.toString(_path.split("/images/")));
            log.info("ImagePath: " + imagePath);
            Files.delete(imagePath);
        }

        productRepo.delete(product);
        return "product deleted successfully";
    }

    @Override
    @Cacheable("products")
    public List<Product> getAllProducts(HttpServletRequest request) {
        String imageUriPrefix = request.getScheme() + "://" +
                request.getServerName() + ":" +
                request.getServerPort();

        List<Product> products = productRepo.findAll();
        return products.stream().peek(product -> product.setImagesPath(product.getImagesPath().stream().map(imagePath -> {
            imagePath = imageUriPrefix + imagePath;
            return imagePath;
        }).collect(Collectors.toList()))).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String updateSingleProduct(Long id, Product product) {
        Product targetProduct = productRepo.findById(id).orElseThrow(() -> new IllegalStateException("Product not exist"));
        if (product.getName() != null) {
            targetProduct.setName(product.getName());
        }
        if (product.getPrice() != null) {
            targetProduct.setPrice(product.getPrice());
        }
        if (product.getAvailableSizes() != null) {
            targetProduct.setAvailableSizes(product.getAvailableSizes());
        }
        if (product.getAvailableColors() != null) {
            targetProduct.setAvailableColors(product.getAvailableColors());
        }

        return "product updated Successfully";

    }
}
