package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.comment.request.CommentCreateDto;
import com.moz1mozi.vstalkbackend.dto.comment.response.CommentDto;
import com.moz1mozi.vstalkbackend.entity.Comment;
import com.moz1mozi.vstalkbackend.entity.Post;
import com.moz1mozi.vstalkbackend.entity.User;
import com.moz1mozi.vstalkbackend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional(readOnly = true)
    public List<CommentDto> getComment(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);
        return comments.stream().map(Comment::toDto).toList();
    }

    private final UserService userService;

    public CommentDto saveComment(CommentCreateDto dto, String username) {
        User user = userService.findByUsername(username);
        Post post = postService.getPostId(dto.getPostId());

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .author(user)
                .post(post)
                .parent(dto.getParentId() != null ?
                    commentRepository.findById(dto.getParentId()).orElse(null) : null)
                .build();
        Comment savedComment = commentRepository.save(comment);

        post.incrementCommentCount();

        return savedComment.toDto();
    }

    public void removeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        comment.markAsDeleted();

//        commentRepository.deleteById(commentId);
    }

    public CommentDto updateCount(Long commnetId, String newContent, String username) {
        Comment comment = commentRepository.findById(commnetId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니디."));

        // 삭제된 댓글은 수정 불가
        if (comment.isDeleted()) {
            throw new IllegalArgumentException("삭제된 댓글은 수정할 수 없습니다.");
        }

        User user = userService.findByUsername(username);
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }

        comment.updateContent(newContent);
        return comment.toDto();

    }

}