package com.milan.endpoint;

import com.milan.dto.CategoryDto;
import com.milan.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.milan.util.Constants.ROLE_ADMIN;
import static com.milan.util.Constants.ROLE_ADMIN_USER;

@RequestMapping(path = "/api/v1/category")
public interface CategoryEndpoint {

    @PostMapping("/save-category")
    @PreAuthorize(ROLE_ADMIN)
    ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto);

    @GetMapping("/")
    @PreAuthorize(ROLE_ADMIN)
    ResponseEntity<?> getAllCategoryWithDetails();

    @GetMapping("/active")
    @PreAuthorize(ROLE_ADMIN_USER)
    ResponseEntity<?> getActiveCategory();

    @GetMapping("/{id}")
    @PreAuthorize(ROLE_ADMIN)
    ResponseEntity<?> getCategoryDetailsById(@PathVariable("id") Integer id) throws ResourceNotFoundException;

    @DeleteMapping("/{id}")
    @PreAuthorize(ROLE_ADMIN)
    ResponseEntity<?> deleteCategoryDetailsById(@PathVariable("id") Integer id);
}
