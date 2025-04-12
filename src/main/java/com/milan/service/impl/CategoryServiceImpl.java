package com.milan.service.impl;

import com.milan.dto.CategoryDto;
import com.milan.dto.CategoryResponse;
import com.milan.exception.ExistDataException;
import com.milan.exception.ResourceNotFoundException;
import com.milan.exception.ValidationException;
import com.milan.model.Category;
import com.milan.repository.CategoryRepository;
import com.milan.service.CategoryService;
import com.milan.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper mapper;

    private final Validation validation;

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {

        //Validation check
        validation.categoryValidation(categoryDto);

        //check category exists or not
        Boolean exists = categoryRepository.existsCategoryByName(categoryDto.getName().trim());

        if (exists) {
            //throw error
            throw new ExistDataException("Category already exists");
        }

        //convert dto to entity
        Category category = mapper.map(categoryDto, Category.class);

            //if id is empty save category, otherwise update it
            if(ObjectUtils.isEmpty(category.getId())){
                // Set isDeleted to false for a new category
                category.setIsDeleted(false);
                //category.setCreatedBy(1);
                //category.setCreatedOn(new Date());
                categoryRepository.save(category);
            }else {
                updateCategory(category);
            }

        Category saveCategory = categoryRepository.save(category);
        if (ObjectUtils.isEmpty(saveCategory)) {
            return false;
        }
        return true;
    }

    private void updateCategory(Category category) {
        Optional<Category> findById = categoryRepository.findById(category.getId());
        if (findById.isPresent()) {
            Category existCategory = findById.get();
            category.setCreatedBy(existCategory.getCreatedBy());
            category.setCreatedOn(existCategory.getCreatedOn());
            category.setIsDeleted(existCategory.getIsDeleted());

//            category.setUpdatedBy(1);
//            category.setUpdatedOn(new Date());
        }
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
    public CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException {

        Category categoryById = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id = " + id));

        // convert entity to dto
        return mapper.map(categoryById, CategoryDto.class);
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
