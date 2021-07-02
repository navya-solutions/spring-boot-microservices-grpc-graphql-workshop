package com.navya.soutions.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "TAG")
public class PostTagEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<PostEntity> posts = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PostTagEntity))
            return false;
        PostTagEntity tag = (PostTagEntity) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
