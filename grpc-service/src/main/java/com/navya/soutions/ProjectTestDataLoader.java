package com.navya.soutions;

import com.navya.soutions.common.CustomUtils;
import com.navya.soutions.domain.PostCommentEntity;
import com.navya.soutions.domain.PostDetailEntity;
import com.navya.soutions.domain.PostEntity;
import com.navya.soutions.domain.PostTagEntity;
import com.navya.soutions.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProjectTestDataLoader {

    private final PostService postService;

    public void loadTestData() {
        loadPostData();

    }

    private void loadPostData() {
        int counter = 0;
        counter = postService.getAllPost().size();
        if (counter != 0)
            return;

        PostEntity post = new PostEntity();
        post.setTitle("High-Performance Java Persistence");
        post.setExternalIdentifier(CustomUtils.createExternalIdentifier());

        post.setDetail(createPostDetail("Yogesh Kumar"));

        post.addComment(createPostComment("JPA and Hibernate"));

        post.addTag(createTag("Java"));
        post.addTag(createTag("Hibernate"));
        postService.createPost(post);
    }

    private PostDetailEntity createPostDetail(String createdBy) {
        PostDetailEntity detail = new PostDetailEntity();
        detail.setCreatedBy(createdBy);
        detail.setCreatedOn(CustomUtils.getEpochCurrentTime());
        return detail;
    }

    private PostCommentEntity createPostComment(String review) {
        PostCommentEntity comment = new PostCommentEntity();
        comment.setReview(review);
        return comment;
    }

    private PostTagEntity createTag(String name) {
        PostTagEntity tag = new PostTagEntity();
        tag.setName(name);
        return tag;
    }
}