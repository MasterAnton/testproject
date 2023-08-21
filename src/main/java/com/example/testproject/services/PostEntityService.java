package com.example.testproject.services;

import com.example.testproject.dto.PostDTO;
import com.example.testproject.entity.User;

import java.util.List;

public interface PostEntityService extends EntityService<PostDTO> {
    List<PostDTO> findByAuthor(User author);
    List<PostDTO> findFavoriteByAuthor(User author);
}