package com.example.testproject.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.testproject.dto.MediaDTO;
import com.example.testproject.entity.Media;
@Component
public class MediaConverter implements Converter<Media, MediaDTO>{

    @Autowired
    UserConverter userConverter;

    @Override
    public MediaDTO map(Media source) {
        MediaDTO target = new MediaDTO();
        target.setId(source.getId());
        target.setAuthor(userConverter.map(source.getAuthor()));
        target.setUrl(source.getUrl());
        target.setThumbGridUrl(source.getThumbGridUrl());
        target.setLocalUrl(source.getLocalUrl());
        target.setLocalThumbGridUrl(source.getLocalThumbGridUrl());
        return target;
    }


    @Override
    public Media map(MediaDTO target) {
        Media source = new Media();
        source.setAuthor(userConverter.map(target.getAuthor()));
        source.setId(target.getId());
        source.setUrl(target.getUrl());
        source.setThumbGridUrl(target.getThumbGridUrl());
        source.setLocalUrl(target.getLocalUrl());
        source.setLocalThumbGridUrl(target.getLocalThumbGridUrl());
        return source;
    }
}
