package com.example.testproject.services;

import com.example.testproject.entity.User;
import com.example.testproject.mapping.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.testproject.dto.UserDTO;
import com.example.testproject.repository.UsersRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractEntityService<User, UserDTO> implements UserEntityService{

    @Autowired
    UsersRepo usersRepo;
    @Autowired
    private UserConverter userConverter;

    public List<UserDTO> findByString(String string) {
        return usersRepo.findByString(string)
                .stream()
                .map(converter::map)
                .collect(Collectors.toList());
    }
}