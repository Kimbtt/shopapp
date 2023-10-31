package com.example.Shopapp.controller;

import com.example.Shopapp.dto.ProductDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    // Hiển thị tất cả các products
    @GetMapping("") //http://localhost:8888/api/v1/products?page=1&limit=10
    public ResponseEntity<?> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok(String.format("get all products, page: %d, limit: %d", page, limit));
    }

    /**
     * Tạo 1 bản ghi product mới
     *
     * @param productDto
     * @param result
     * @return
     */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDto productDto,
//            @RequestPart("file") MultipartFile file,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errMess = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errMess);
            }
            MultipartFile file = productDto.getFile();
            if (file != null) {
                // Kiểm tra kích thước file và định dạng file
                if (file.getSize() > 10 * 1024 * 1024) {
                    // Nếu kích thước file  > 10mb => trả về status
                    // throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large! Maximum size is
                    // 10MB");
                    // Or
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is " +
                            "10MB");
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

                // Lưu đối tượng product vào trong DB =>> LÀM SAU
            }

            return ResponseEntity.ok("Product created successfully");
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


    /**
     * Hàm để lưu file
     *
     * @param MultipartFile file
     * @return tên file là duy nhất
     * @throws IOException
     */
    private String storeFile(MultipartFile file) throws IOException {
        // Lấy file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        // Tạo đường dẫn đến thư mục chứa file
        Path uploadDir = Paths.get("uploads");
        // Kiểm tra thư mục uploads tồn tại chưa
        if (!Files.exists(uploadDir)){
            // Nếu chưa tồn tại thì tạo mới
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }


    /**
     * IDで商品情報を習得
     *
     * @param productId
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") String productId) {
        return ResponseEntity.ok("Product with id: " + productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable("id") String productId) {
        return ResponseEntity.ok("Product delete with id: " + productId);
    }
}


