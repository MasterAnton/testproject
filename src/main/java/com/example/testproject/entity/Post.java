package com.example.testproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post extends AbstractEntity{
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name="description",length = 2000)
    private String description;

    @Column(name="coverindex")
    private Integer coverindex;

    @Column(name="publicated")
    private boolean publicated=false;

    @ManyToMany(mappedBy = "favorites")
    private List<User> follower;

//    @OneToMany(mappedBy="post",fetch = FetchType.EAGER)
//    private List<Comment> comments;
//
//    @OneToMany(mappedBy="post",fetch = FetchType.EAGER)
//    private List<Media> medias;

}
