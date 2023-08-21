package com.example.testproject.repository;

import com.example.testproject.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepo extends EntityRepository<User> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Long deleteByEmail(String email);






    @Override
    default Optional<User> findByIdentifier(String identifier) {
        return findByEmail(identifier);
    }

    @Query(
            value = "SELECT * FROM usr u WHERE upper(u.profession) like CONCAT('%',upper(?1),'%') or upper(u.phone) like CONCAT('%',upper(?1),'%') or upper(u.nickname) like CONCAT('%',upper(?1),'%') or upper(u.name) like CONCAT('%',upper(?1),'%') or upper(u.email) like CONCAT('%',upper(?1),'%') order by u.nickname",
            nativeQuery = true)
    List<User> findByString(String string);


    @Modifying
    @Transactional
    @Query(
            value = "update usr set nickname = ?1 where id = ?2 and (select count(*) from usr u where u.nickname = ?1 and u.id != ?2) = 0",
            nativeQuery = true)
    Integer updateNickname(String nickname,Long id);




    @Override
    default Long deleteByIdentifier(String identifier) {
        return deleteByEmail(identifier);
    }

    @Override
    default boolean existsByIdentifier(String identifier) {
        return existsByEmail(identifier);
    }
}
