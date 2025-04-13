package com.milan.controller;

import com.milan.dto.CategoryDto;
import com.milan.dto.CategoryResponse;
import com.milan.endpoint.CategoryEndpoint;
import com.milan.exception.ResourceNotFoundException;
import com.milan.model.Category;
import com.milan.repository.CategoryRepository;
import com.milan.service.CategoryService;
import com.milan.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController implements CategoryEndpoint {

    private final CategoryService categoryService;

    private final ModelMapper mapper;

    @Override
    public ResponseEntity<?> saveCategory(CategoryDto categoryDto) {

        Boolean b = categoryService.saveCategory(categoryDto);
        if (b) {
           return CommonUtil.createBuildResponseMessage("Category saved", HttpStatus.CREATED);
           // return new ResponseEntity<>("Category Saved", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Category not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        //return new ResponseEntity<>("Category not saved", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getAllCategoryWithDetails() {

        List<CategoryDto> allCategories = categoryService.getAllCategories();

        if (CollectionUtils.isEmpty(allCategories)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(allCategories,HttpStatus.OK);
       // return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getActiveCategory() {

        List<CategoryResponse> activeCategories = categoryService.getActiveCategories();

        if (CollectionUtils.isEmpty(activeCategories)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(activeCategories,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getCategoryDetailsById(Integer id) throws ResourceNotFoundException {

        CategoryDto categoryById = categoryService.getCategoryById(id);
        if(ObjectUtils.isEmpty(categoryById)) {
            return CommonUtil.createErrorResponseMessage("Please Try Again. Internal server error", HttpStatus.NOT_FOUND);
        }
        return CommonUtil.createBuildResponse(categoryById,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteCategoryDetailsById(Integer id) {
        Boolean isDeleted =  categoryService.deleteCategoryById(id);

        if(isDeleted) {
            return CommonUtil.createBuildResponseMessage("Category deleted successfully", HttpStatus.OK);
        }
        return CommonUtil.createErrorResponseMessage("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
