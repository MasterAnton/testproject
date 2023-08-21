package com.example.testproject.services;

import com.example.testproject.dto.UserDTO;

import java.util.List;

public interface UserEntityService extends EntityService<UserDTO>{
    List<UserDTO> findByString(String string);
}
