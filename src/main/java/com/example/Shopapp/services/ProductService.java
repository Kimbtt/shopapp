package com.example.Shopapp.services;

import com.example.Shopapp.models.dtos.ProductDto;
import com.example.Shopapp.models.dtos.ProductImageDto;
import com.example.Shopapp.exceptions.DataNotFoundException;
import com.example.Shopapp.models.entity.Product;
import com.example.Shopapp.models.entity.ProductImage;
import com.example.Shopapp.models.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductService {
    Product createProduct(ProductDto productDto) throws DataNotFoundException;

    Product getProductById(Long id) throws Exception;

    Page<ProductResponse> getAllProducts(PageRequest pageRequest);

    Product updateProduct(Long id, ProductDto productDto) throws Exception;

    void deleteProduct(Long id);

    boolean existsByName(String name);

    ProductImage createProductImage(
            Long productId,
            ProductImageDto productImageDto
    ) throws Exception;
}
