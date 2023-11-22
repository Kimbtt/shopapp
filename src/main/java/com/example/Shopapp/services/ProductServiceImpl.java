package com.example.Shopapp.services;

import com.example.Shopapp.models.dtos.ProductDto;
import com.example.Shopapp.models.dtos.ProductImageDto;
import com.example.Shopapp.exceptions.DataNotFoundException;
import com.example.Shopapp.exceptions.InvalidParamException;
import com.example.Shopapp.models.entity.Category;
import com.example.Shopapp.models.entity.Product;
import com.example.Shopapp.models.entity.ProductImage;
import com.example.Shopapp.models.responses.ProductResponse;
import com.example.Shopapp.repositories.CategoryRepository;
import com.example.Shopapp.repositories.ProductImageRepository;
import com.example.Shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDto productDto) throws DataNotFoundException {
        // Kiểm tra category có không
        Category existingCategory = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + productDto.getCategoryId()));

        // Tạo 1 đối tượng product mới
        Product newProduct = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .thumbnail(productDto.getThumbnail())
                .description(productDto.getDescription())
                .category(existingCategory).build();

        // Lưu và trả về 1 product
        productRepository.save(newProduct);
        return newProduct;
    }

    @Override
    public Product getProductById(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException("Cannot find produc with id = " + id
                        ));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        // Lấy danh sách sản phẩm theo trang (page) và giới hạn (limit)
        return productRepository
                .findAll(pageRequest)
                .map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(Long id, ProductDto productDto) throws Exception {
        Product existingProduct = getProductById(id);
        if (existingProduct != null) {
            //Copy các thuộc tính từ DTO -> Product
            // Có thể sử dụng ModelMapper

            // Kiểm tra category có không
            Category existingCategory = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + productDto.getCategoryId()));


            existingProduct.setName(productDto.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDto.getPrice());
            existingProduct.setDescription(productDto.getDescription());
            existingProduct.setThumbnail(productDto.getThumbnail());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDto productImageDto) throws Exception {
        // Kiểu tra có sản phẩm k?
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + productId));

        // Nếu có thì add ảnh
        ProductImage newProductImage = ProductImage.builder().product(existingProduct).imageUrl(productImageDto.getImageUrl()).build();

        // Ko cho insert quá 5 ảnh/sản phẩm
        int size = productImageRepository.findByProductId(productId).size(); //  Size: lấy số lượng phần tử
        if (size >= 5) {
            throw new InvalidParamException("Number of images must be <= 5");
        }
        return productImageRepository.save(newProductImage);
    }
}
