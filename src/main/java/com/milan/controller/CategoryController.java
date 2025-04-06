package com.milan.controller;

import com.milan.model.Category;
import com.milan.repository.CategoryRepository;
import com.milan.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/save-category")
    public ResponseEntity<?> saveCategory(@RequestBody Category category) {

        Boolean b = categoryService.saveCategory(category);
        if (b) {
            return new ResponseEntity<>(category, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Category not saved", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/category")
    public ResponseEntity<?> getAllCategory() {

        List<Category> allCategories = categoryService.getAllCategories();

        if (CollectionUtils.isEmpty(allCategories)) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }


}
