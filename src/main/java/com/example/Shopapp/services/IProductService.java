package com.example.Shopapp.services;

import com.example.Shopapp.dtos.ProductDto;
import com.example.Shopapp.dtos.ProductImageDto;
import com.example.Shopapp.exceptions.DataNotFoundException;
import com.example.Shopapp.models.Product;
import com.example.Shopapp.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public interface IProductService {
    public Product createProduct(ProductDto productDto) throws DataNotFoundException;

    Product getProductById(Long id) throws Exception;

    Page<Product> getAllProducts(PageRequest pageRequest);

    Product updateProduct(Long id, ProductDto productDto) throws Exception;

    void deleteProduct(Long id);

    boolean existsByName(String name);

    ProductImage createProductImage(
            Long productId,
            ProductImageDto productImageDto
    ) throws Exception;
}
