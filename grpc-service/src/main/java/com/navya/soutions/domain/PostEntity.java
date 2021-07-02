package com.navya.soutions.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "POST")
public class PostEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String externalIdentifier;

    private String title;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PostDetailEntity detail;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostCommentEntity> comments = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<PostTagEntity> tags = new LinkedHashSet<>();

    public void setDetail(PostDetailEntity detail) {
        if (detail == null) {
            if (this.detail != null) {
                this.detail.setPost(null);
            }
        } else {
            detail.setPost(this);
        }
        this.detail = detail;
    }

    public void addComment(PostCommentEntity comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(PostCommentEntity comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    public void addTag(PostTagEntity tag) {
        tags.add(tag);
        tag.getPosts().add(this);
    }

    public void removeTag(PostTagEntity tag) {
        tags.remove(tag);
        tag.getPosts().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof PostEntity)) return false;

        return id != null && id.equals(((PostEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
