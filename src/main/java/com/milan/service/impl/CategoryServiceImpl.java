package com.milan.service.impl;

import com.milan.dto.CategoryDto;
import com.milan.dto.CategoryResponse;
import com.milan.model.Category;
import com.milan.repository.CategoryRepository;
import com.milan.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper mapper;

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {
        Category category = mapper.map(categoryDto, Category.class);

        // Set isDeleted to false for a new category
        category.setIsDeleted(false);
        category.setCreatedBy(1);
        category.setCreatedOn(new Date());

        //if saved true, otherwise false
        categoryRepository.save(category);
        return true;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();

        //converting entity to dto list
        return allCategories.stream().map(cat -> mapper.map(cat, CategoryDto.class)).toList();
    }

    @Override
    public List<CategoryResponse> getActiveCategories() {

        List<Category> activeCategories = categoryRepository.findByIsActiveTrue();

        //convert entity to response dto
        return activeCategories.stream().map(cat -> mapper.map(cat, CategoryResponse.class)).toList();
    }

}
