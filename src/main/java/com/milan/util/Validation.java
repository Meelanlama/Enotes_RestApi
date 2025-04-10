package com.milan.util;

import com.milan.dto.CategoryDto;
import com.milan.dto.NotesDto;
import com.milan.dto.TodoDto;
import com.milan.dto.UserDto;
import com.milan.enums.TodoStatus;
import com.milan.exception.ExistDataException;
import com.milan.exception.ResourceNotFoundException;
import com.milan.exception.ValidationException;
import com.milan.model.Role;
import com.milan.repository.RoleRepository;
import com.milan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Validation {

    private final RoleRepository roleRepo;

    private final UserRepository userRepo;

    //Category Validation
    public void categoryValidation(CategoryDto categoryDto) {

        Map<String, Object> error = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(categoryDto)) {
            throw new IllegalArgumentException("category Object/JSON shouldn't be null or empty");
        } else {

            // validation name field
            if (ObjectUtils.isEmpty(categoryDto.getName()) || !StringUtils.hasText(categoryDto.getName())) {
                error.put("name", "name field is empty or null");
            } else {
                if (categoryDto.getName().length() < 3) {
                    error.put("name", "name length min 3");
                }
                if (categoryDto.getName().length() > 100) {
                    error.put("name", "name length max 100");
                }
            }

            // validation description
            if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
                error.put("description", "description field is empty or null");
            }

            // validation isActive
            if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
                error.put("isActive", "isActive field is empty or null");
            } else {
                if (categoryDto.getIsActive() != Boolean.TRUE.booleanValue()
                        && categoryDto.getIsActive() != Boolean.FALSE.booleanValue()) {
                    error.put("isActive", "invalid value isActive field ");
                }
            }
        }

        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
    }

    //Notes Validation
    public void notesValidation(NotesDto notesDto) {

        Map<String, Object> error = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(notesDto)) {
            throw new IllegalArgumentException("NotesDto object must not be null");
        } else {

            // Validate title
            if (ObjectUtils.isEmpty(notesDto.getTitle())) {
                error.put("title", "Title must not be empty");
            } else {
                String title = notesDto.getTitle().trim(); // Trim whitespace
                if (title.isEmpty()) {
                    error.put("title", "Title must not be blank");
                } else if (title.length() < 3) {
                    error.put("title", "Title must be at least 3 characters");
                } else if (title.length() > 100) {
                    error.put("title", "Title must be at most 100 characters");
                }
            }

            if (ObjectUtils.isEmpty(notesDto.getDescription())) {
                error.put("description", "Description must not be empty");
            } else {
                String description = notesDto.getDescription().trim();
                if (description.isEmpty()) {
                    error.put("description", "Description must not be blank");
                }
            }

            // Validate category (nested validation)
            if (ObjectUtils.isEmpty(notesDto.getCategory())) {
                error.put("category", "Category must not be null");
            }

            if (!error.isEmpty()) {
                throw new ValidationException(error);
            }
        }
    }

    //To do validation
    public void todoValidation(TodoDto todo) throws Exception {
        TodoDto.StatusDto reqStatus = todo.getStatus();
        Boolean statusFound = false;

        //if only the id is correct, then make it true
        for (TodoStatus st : TodoStatus.values()) {
            if (st.getId().equals(reqStatus.getId())) {
                statusFound = true;
            }
        }
        if (!statusFound) {
            throw new ResourceNotFoundException("Invalid status");
        }
    }

    //Role validation for user
    public void userValidation(UserDto userDto) {

        if(!StringUtils.hasText(userDto.getFirstName()) || !StringUtils.hasText(userDto.getLastName())) {
            throw new IllegalArgumentException("Please enter valid details");
        }

        if(!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(Constants.EMAIL_REGEX)) {
            throw new IllegalArgumentException("Please enter valid email");
        }else {
            //validate duplicate email
            boolean exist = userRepo.existsByEmail(userDto.getEmail());
            if(exist) {
                throw new ExistDataException("Email already exists");
            }
        }

        if(!StringUtils.hasText(userDto.getMobileNumber()) || !userDto.getMobileNumber().matches(Constants.MOBILE_REGEX)) {
            throw new IllegalArgumentException("Please enter valid mobile number");
        }

        if(CollectionUtils.isEmpty(userDto.getRoles())) {
            throw new IllegalArgumentException("Please enter valid role. Empty");
        }else {
            List<Integer> allRoleIds = roleRepo.findAll().stream().map(r -> r.getId()).collect(Collectors.toList());

            List<Integer> roleRequestId = userDto.getRoles().stream()
                    .map(r -> r.getId())
                    .filter(roleId -> allRoleIds.contains(roleId)).toList();

            if(CollectionUtils.isEmpty(roleRequestId)) {
                throw new IllegalArgumentException("Please enter valid role:" + roleRequestId);
            }
        }

    }

}

