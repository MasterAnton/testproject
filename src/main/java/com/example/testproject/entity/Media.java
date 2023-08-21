package com.example.testproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Media extends AbstractEntity{
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name="url")
    private String url;
    @Column(name="local_url")
    private String localUrl;

    @Column(name="thumb_grid_url")
    private String thumbGridUrl;
    @Column(name="local_thumb_grid_url")
    private String localThumbGridUrl;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;
}
