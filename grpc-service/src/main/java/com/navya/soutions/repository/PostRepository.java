package com.navya.soutions.repository;

import com.navya.soutions.domain.PostEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<PostEntity, Long> {

    Optional<PostEntity> findByExternalIdentifier(String externalIdentifier);

    void deleteByExternalIdentifier(String externalIdentifier);
}