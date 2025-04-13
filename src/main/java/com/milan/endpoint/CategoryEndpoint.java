package com.milan.endpoint;

import com.milan.dto.CategoryDto;
import com.milan.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.milan.util.Constants.ROLE_ADMIN;
import static com.milan.util.Constants.ROLE_ADMIN_USER;

@Tag(name = "CATEGORY APIs", description = "API for all Category Operations APIs")
@RequestMapping(path = "/api/v1/category")
public interface CategoryEndpoint {

    @Operation(summary = "Save Category",description = "SAVE BY ADMIN ONLY")
    @PostMapping("/save-category")
    @PreAuthorize(ROLE_ADMIN)
    ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto);

    @Operation(summary = "Get all category",description = "GET BY ADMIN ONLY")
    @GetMapping("/")
    @PreAuthorize(ROLE_ADMIN)
    ResponseEntity<?> getAllCategoryWithDetails();

    @Operation(summary = "Get active category",description = "GET BY ADMIN AND USER BOTH")
    @GetMapping("/active")
    @PreAuthorize(ROLE_ADMIN_USER)
    ResponseEntity<?> getActiveCategory();

    @Operation(summary = "Get category with its details by id",description = "GET BY ADMIN ONLY")
    @GetMapping("/{id}")
    @PreAuthorize(ROLE_ADMIN)
    ResponseEntity<?> getCategoryDetailsById(@PathVariable("id") Integer id) throws ResourceNotFoundException;

    @Operation(summary = "Delete category by id",description = "DELETE BY ADMIN ONLY")
    @DeleteMapping("/{id}")
    @PreAuthorize(ROLE_ADMIN)
    ResponseEntity<?> deleteCategoryDetailsById(@PathVariable("id") Integer id);
}
