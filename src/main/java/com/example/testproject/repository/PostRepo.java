package com.example.testproject.repository;

import com.example.testproject.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.testproject.entity.Post;

import java.util.List;

@Repository
public interface PostRepo extends EntityRepository<Post>{
    List<Post> findByDeletedFalseAndAuthorOrderByCreatedAtDesc(User author);


    List<Post> findByPublicatedTrueAndDeletedFalseAndAuthorOrderByCreatedAtDesc(User author);

    @Query(
            value = "SELECT * FROM post p WHERE (upper(p.description) like CONCAT('%',upper(?1),'%') or (p.description is null and ?1 = '')) and p.publicated = true and p.deleted = false and (p.id < ?2 or ?2 < 0) order by p.id desc limit 12",
            nativeQuery = true)
    List<Post> findPublicByString(String string,Long lastid);
    @Query(
            value = "SELECT CASE WHEN count(*)>0 THEN true ELSE false END FROM favorites f WHERE f.post_id = ?2 and f.user_id = ?1",
            nativeQuery = true)
    boolean isFavoriteOfUser(Long userid,Long postid);

}
