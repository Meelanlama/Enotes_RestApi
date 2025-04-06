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
import java.util.Optional;

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
        List<Category> allCategories = categoryRepository.findByIsDeletedFalse();

        //converting entity to dto list
        return allCategories.stream().map(cat -> mapper.map(cat, CategoryDto.class)).toList();
    }

    @Override
    public List<CategoryResponse> getActiveCategories() {

        List<Category> activeCategories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();

        //convert entity to response dto
        return activeCategories.stream().map(cat -> mapper.map(cat, CategoryResponse.class)).toList();
    }

    @Override
    public CategoryDto getCategoryById(Integer id) {
        Optional<Category> categoryById = categoryRepository.findByIdAndIsDeletedFalse(id);

        if(categoryById.isPresent()) {
            //get category if its present
            Category category = categoryById.get();
            //convert entity to dto
           return mapper.map(category, CategoryDto.class);
        }
        return null;
    }

    @Override
    public Boolean deleteCategoryById(Integer id) {
        Optional<Category> categoryById = categoryRepository.findById(id);

        if(categoryById.isPresent()) {
            Category category = categoryById.get();
            category.setIsDeleted(true);
            category.setIsActive(false);
            categoryRepository.save(category);
            return true;
        }
        return false;
    }

}
