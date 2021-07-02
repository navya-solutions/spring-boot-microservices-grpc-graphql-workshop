package com.navya.soutions.service;

import com.navya.soutions.common.CustomUtils;
import com.navya.soutions.domain.PostCommentEntity;
import com.navya.soutions.domain.PostEntity;
import com.navya.soutions.domain.PostTagEntity;
import com.navya.soutions.exception.CustomException;
import com.navya.soutions.repository.PostRepository;
import io.grpc.Status;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class PostService {
    final private PostRepository postRepository;

    public PostEntity createPost(PostEntity post) {
        try {
            post.setExternalIdentifier(CustomUtils.createExternalIdentifier());
            return postRepository.save(post);
        } catch (Exception exception) {
            throw new CustomException(CustomUtils.getRootException(exception), Status.INTERNAL.getCode().value());
        }
    }

    public Set<PostEntity> getAllPost() {
        return StreamSupport
                .stream(postRepository.findAll().spliterator(), true)
                .collect(Collectors.toSet());
    }

    public Optional<PostEntity> getPost(String externalIdentifier) {
        return postRepository.findByExternalIdentifier(externalIdentifier);
    }

    public void deletePost(String externalIdentifier) {
        postRepository.deleteByExternalIdentifier(externalIdentifier);
    }


    public PostEntity addTag(String externalIdentifier, PostTagEntity tagEntity) {
        final PostEntity postEntity = postRepository.findByExternalIdentifier(externalIdentifier)
                .orElseThrow(() -> new CustomException(String.format("Post not found for provided identifier %s", externalIdentifier),
                        Status.NOT_FOUND.getCode().value()));
        postEntity.addTag(tagEntity);
        return postRepository.save(postEntity);
    }

    public PostEntity addComment(String externalIdentifier, PostCommentEntity postComment) {
        final PostEntity postEntity = postRepository.findByExternalIdentifier(externalIdentifier)
                .orElseThrow(() -> new CustomException(String.format("Post not found for provided identifier %s", externalIdentifier),
                        Status.NOT_FOUND.getCode().value()));
        postEntity.addComment(postComment);
        return postRepository.save(postEntity);

    }

}
