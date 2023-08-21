package com.example.testproject.repository;

import com.example.testproject.entity.Media;
import com.example.testproject.entity.Post;

import java.util.List;

public interface MediaRepo extends EntityRepository<Media>{
     List<Media> findByPost(Post post);
}
