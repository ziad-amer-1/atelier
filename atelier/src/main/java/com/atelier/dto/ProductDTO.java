package com.atelier.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public record ProductDTO(
    String name,
    Float price,
    Set<String> availableColors,
    Set<String> availableSizes,
    List<MultipartFile> productImages
) {
}
