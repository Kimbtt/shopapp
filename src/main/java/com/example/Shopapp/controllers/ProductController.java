package com.example.Shopapp.controllers;

import com.example.Shopapp.exceptions.DataNotFoundException;
import com.example.Shopapp.models.dtos.ProductDto;
import com.example.Shopapp.models.dtos.ProductImageDto;
import com.example.Shopapp.models.entity.Product;
import com.example.Shopapp.models.entity.ProductImage;
import com.example.Shopapp.models.responses.ProductListResponse;
import com.example.Shopapp.models.responses.ProductResponse;
import com.example.Shopapp.services.ProductService;
//import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Hiển thị tất cả các products
    @GetMapping("") //http://localhost:8888/api/v1/products?page=1&limit=10
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit
    ) {
        if (page > 0) {
            page--;
        } else {
            page = 0;
        }
        //Tạo pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page, limit, Sort.by("createAt").descending()); // sắp xếp giảm dần
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);

        // Lấy ra tổng số trang
        int totalPage = productPage.getTotalPages();
        // Lấy danh sách product
        List<ProductResponse> products = productPage.getContent();

        return ResponseEntity.ok(ProductListResponse.builder()
                .products(products)
                .totalPages(totalPage)
                .build());
    }

    /**
     * Tạo 1 bản ghi product mới
     *
     * @param productDto
     * @param result
     * @return
     */
    @PostMapping("")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto productDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errMess = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errMess);
            }
            Product newProduct = productService.createProduct(productDto);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /*{
        "name": "ipad pro 2023",
        "price": 294.23,
        "thumbnail":"",
        "description": "this is description",
        "category_id":1
    }*/

    @PostMapping(value = "uploads/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadImages(@ModelAttribute("files") List<MultipartFile> files, @PathVariable("id") Long ProductId) {
        /*
         * Debug khi không upload file => files = null lỗi
         *   xử lý: nếu files == null thì truyền vào 1 cái ArrayList rỗng, còn k thì là chính nó
         * */
        try {
            // B1: Kiểm tra xem có product đó không
            Product existingProduct = productService.getProductById(ProductId);
            // nếu files == nukk thì files được gán bằng 1 arrayList mới
            // Nếu files có file thì gán bằng chính files đó
            files = files == null ? new ArrayList<>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            }
            // Taạo 1 list productImages rỗng có kiêểu là productImage
            List<ProductImage> productImages = new ArrayList<>();

            // lặp qua danh saách file
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    // Nếu size = 0 thì continue;
                    continue;
                }
                if (file.getSize() > 10 * 1024 * 1024) {
                    // Nếu kích thước file  > 10mb => trả về status
                    // throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large! Maximum size is
                    // 10MB");
                    // Or
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is " + "10MB");
                }
                // Lấy định dạng file
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    // Nếu contentType null hoặc không phải là định dạng file ảnh (contentType trả về định dạng image/jpg
                    // chẳng hạn) => trả về badRequest
                    return ResponseEntity.badRequest().body("File must be an image");
                }
                // Lưu file và cập nhật thumbnail trong DTO
                String fileName = storeFile(file);

                // Lưu đối tượng product vào trong DB -> lưu vào bảng product_img -> trong hàm có r
                ProductImage newProductImage = productService.createProductImage(existingProduct.getId(), ProductImageDto.builder().imageUrl(fileName).build());
                productImages.add(newProductImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /**
     * Hàm để lưu file
     *
     * @param MultipartFile MultipartFile
     * @return tên file là duy nhất
     * @throws IOException
     */
    private String storeFile(MultipartFile file) throws IOException {
        // Kiểm tra có phải file ảnh và có tên không
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }

        // Lấy file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        // Tạo đường dẫn đến thư mục chứa file
        Path uploadDir = Paths.get("uploads");
        // Kiểm tra thư mục uploads tồn tại chưa
        if (!Files.exists(uploadDir)) {
            // Nếu chưa tồn tại thì tạo mới
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    /**
     * IDで商品情報を習得
     *
     * @param productId
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") long productId) {
        try {
            Product existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public  ResponseEntity<?> updateProductById(
            @PathVariable("id") long productId,
            @RequestBody ProductDto productDto
    ){
        try {
            Product product = productService.updateProduct(productId, productDto);
            return ResponseEntity.ok(product);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable("id") long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok("Product delete with id: " + productId);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PostMapping("/generateFakeProducts")
//    private ResponseEntity<String> generateFakeProducts() {
//        Faker faker = new Faker();
//        for (int i = 0; i < 1000000; i++) {
//            String productName = faker.commerce().productName();
//            if (productService.existsByName(productName)) {
//                continue;
//            }
//            ProductDto productDto = ProductDto.builder()
//                    .name(productName)
//                    .price((float) faker.number().numberBetween(10, 10000000))
//                    .thumbnail("")
//                    .description(faker.lorem().sentence())
//                    .categoryId((long) faker.number().numberBetween(1, 3))
//                    .build();
//            try {
//                productService.createProduct(productDto);
//            } catch (DataNotFoundException e) {
//                return ResponseEntity.badRequest().body(e.getMessage());
//            }
//        }
//
//        return ResponseEntity.ok("Fake Products created successfully");
//    }

}


