package com.milan.service;

import com.milan.dto.CategoryDto;
import com.milan.dto.CategoryResponse;
import com.milan.exception.ResourceNotFoundException;
import com.milan.model.Category;

import java.util.List;

public interface CategoryService {

    public Boolean saveCategory(CategoryDto categoryDto);

    public List<CategoryDto> getAllCategories();

    public List<CategoryResponse> getActiveCategories();

    CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException;

    Boolean deleteCategoryById(Integer id);

}
