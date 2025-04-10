package com.milan.service.impl;

import com.milan.dto.UserDto;
import com.milan.model.Role;
import com.milan.model.User;
import com.milan.repository.RoleRepository;
import com.milan.repository.UserRepository;
import com.milan.service.UserService;
import com.milan.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    private final ModelMapper mapper;

    private final RoleRepository roleRepo;

    private final Validation validation;

    @Override
    public Boolean registerUser(UserDto userDto) {

        //validate role before register
        validation.userValidation(userDto);
        User saveUser = mapper.map(userDto, User.class);

        setRole(userDto,saveUser);

        User saved = userRepo.save(saveUser);
        return true;
    }

    private void setRole(UserDto userDto,User saveUser) {
        List<Integer> roleId = userDto.getRoles().stream().map(r -> r.getId()).toList();
        List<Role> roles = roleRepo.findAllById(roleId);
        saveUser.setRoles(roles);
    }

}
