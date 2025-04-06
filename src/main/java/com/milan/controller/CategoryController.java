package com.milan.controller;

import com.milan.dto.CategoryDto;
import com.milan.dto.CategoryResponse;
import com.milan.model.Category;
import com.milan.repository.CategoryRepository;
import com.milan.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    private final ModelMapper mapper;

    @PostMapping("/save-category")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) {

        Boolean b = categoryService.saveCategory(categoryDto);
        if (b) {
            return new ResponseEntity<>("Category Saved", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Category not saved", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCategoryWithDetails() {

        List<CategoryDto> allCategories = categoryService.getAllCategories();

        if (CollectionUtils.isEmpty(allCategories)) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveCategory() {

        List<CategoryResponse> activeCategories = categoryService.getActiveCategories();

        if (CollectionUtils.isEmpty(activeCategories)) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(activeCategories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryDetailsById(@PathVariable("id") Integer id) {
        CategoryDto categoryById = categoryService.getCategoryById(id);

        if(categoryById != null) {
            return new ResponseEntity<>(categoryById, HttpStatus.OK);
        }
        return new ResponseEntity<>("Category not found of id: " + id, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryDetailsById(@PathVariable("id") Integer id) {
        Boolean isDeleted =  categoryService.deleteCategoryById(id);

        if(isDeleted) {
            return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
