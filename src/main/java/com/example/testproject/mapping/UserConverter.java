package com.example.testproject.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.example.testproject.dto.MediaDTO;
import com.example.testproject.dto.UserDTO;
import com.example.testproject.entity.Media;
import com.example.testproject.entity.User;
import com.example.testproject.repository.EntityRepository;
import com.example.testproject.repository.UsersRepo;

import java.util.List;

@Component
public class UserConverter   implements Converter<User, UserDTO>{
//    @Autowired
//    private Converter<User, UserDTO> userConverter;

    @Autowired
    private EntityRepository<User> userRepo;


    @Autowired
    UsersRepo usersRepo;

    @Override
    public UserDTO map(User source) {
        UserDTO target = new UserDTO();
        target.setNickname(source.getNickname());
        target.setEmail(source.getEmail());
        if (source.getAva()!=null){
            MediaDTO m = new MediaDTO();
            m.setId(source.getAva().getId());
            m.setUrl(source.getAva().getUrl());
            m.setThumbGridUrl(source.getAva().getThumbGridUrl());
            m.setLocalUrl(source.getAva().getLocalUrl());
            m.setLocalThumbGridUrl(source.getAva().getLocalThumbGridUrl());

            target.setAva(
                    m
            );
        }
        target.setName(source.getName());
        target.setPhone(source.getPhone());
        target.setProfession(source.getProfession());
        target.setSiteurl(source.getSiteurl());
        target.setSitename(source.getSitename());
        target.setId(source.getId());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = (User) auth.getPrincipal();
        return target;
    }

    @Override
    public User map(UserDTO target) {
        User source = new User();
        source.setNickname(target.getNickname());
        source.setEmail(target.getEmail());

        Media m = new Media();
        m.setId(target.getAva().getId());
        m.setUrl(target.getAva().getUrl());
        m.setThumbGridUrl(target.getAva().getThumbGridUrl());
        m.setLocalUrl(target.getAva().getLocalUrl());
        m.setLocalThumbGridUrl(target.getAva().getLocalThumbGridUrl());

        source.setAva(
                m
        );
        source.setName(target.getName());
        source.setPhone(target.getPhone());
        source.setProfession(target.getProfession());
        source.setSiteurl(target.getSiteurl());
        source.setSitename(target.getSitename());
        source.setId(target.getId());
        return source;
    }
}
