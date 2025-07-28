package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.comment.request.CommentCreateDto;
import com.moz1mozi.vstalkbackend.dto.comment.response.CommentDto;
import com.moz1mozi.vstalkbackend.dto.post.request.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.user.request.UserCreateDto;
import com.moz1mozi.vstalkbackend.dto.vote.request.VoteOptionCreateDto;
import com.moz1mozi.vstalkbackend.entity.Category;
import com.moz1mozi.vstalkbackend.entity.User;
import com.moz1mozi.vstalkbackend.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    VoteOptionService voteOptionService;

    @DisplayName("댓글을 작성한다.")
    @Test
    void createComment() {
        // given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .username("testUser")
                .password(passwordEncoder.encode("password"))
                .nickname("testNickname")
                .build();

        UserCreateDto userCreateDto2 = UserCreateDto.builder()
                .username("testUser2")
                .password(passwordEncoder.encode("password"))
                .nickname("testNickname2")
                .build();

        User user = userService.createUser(userCreateDto);
        User user2 = userService.createUser(userCreateDto2);

        String username = user.getUsername();

        Category sports = Category.builder()
                .name("sports")
                .build();

        Category category = categoryRepository.save(sports);

        VoteOptionCreateDto firstOption = VoteOptionCreateDto.builder()
                .optionText("test option 1")
                .build();

        VoteOptionCreateDto secondOption = VoteOptionCreateDto.builder()
                .optionText("test option 2")
                .build();

        PostCreateDto post = PostCreateDto.builder()
                .title("test title")
                .content("test content")
                .videoId("test videoId")
                .isSecret(false)
                .username(username)
                .categoryId(category.getId())
                .voteEnabled(true)
                .voteOptions(List.of(firstOption, secondOption))
                .build();

        Long postId = postService.createPost(post);

        voteOptionService.createVoteOptions(postService.getPostId(postId), List.of(firstOption, secondOption));

        CommentCreateDto comment = CommentCreateDto.builder()
                .postId(postId)
                .content("test comment")
                .build();
        // when
        CommentDto commentDto = commentService.saveComment(comment, "testUser");

        // then
        assertThat(commentDto.getContent()).isEqualTo( comment.getContent() );
    }

    @DisplayName("댓글 목록을 조회한다.")
    @Test
    void getComments() {
        // given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .username("testUser")
                .password(passwordEncoder.encode("password"))
                .nickname("testNickname")
                .build();

        UserCreateDto userCreateDto2 = UserCreateDto.builder()
                .username("testUser2")
                .password(passwordEncoder.encode("password"))
                .nickname("testNickname2")
                .build();

        User user = userService.createUser(userCreateDto);
        User user2 = userService.createUser(userCreateDto2);

        String username = user.getUsername();

        Category sports = Category.builder()
                .name("sports")
                .build();

        Category category = categoryRepository.save(sports);

        VoteOptionCreateDto firstOption = VoteOptionCreateDto.builder()
                .optionText("test option 1")
                .build();

        VoteOptionCreateDto secondOption = VoteOptionCreateDto.builder()
                .optionText("test option 2")
                .build();

        PostCreateDto post = PostCreateDto.builder()
                .title("test title")
                .content("test content")
                .videoId("test videoId")
                .isSecret(false)
                .username(username)
                .categoryId(category.getId())
                .voteEnabled(true)
                .voteOptions(List.of(firstOption, secondOption))
                .build();

        Long postId = postService.createPost(post);

        List<Long> parentCommentIds = new ArrayList<>();

        // 먼저 부모 댓글들을 생성
        for (int i = 1; i <= 10; i++) {
            CommentCreateDto parentComment = CommentCreateDto.builder()
                    .postId(postId)  // 하드코딩된 9L 대신 실제 생성된 postId 사용
                    .content("부모 댓글 " + i)
                    .parentId(null) // 부모 댓글은 parentId가 null
                    .build();

            CommentDto savedComment = commentService.saveComment(parentComment, username);
            parentCommentIds.add(savedComment.getId());
        }

        // 그 다음 대댓글들을 생성
        for (int i = 1; i <= 20; i++) {
            // 무작위로 부모 댓글 선택 또는 순차적으로 선택
            Long randomParentId = parentCommentIds.get(i % parentCommentIds.size());

            CommentCreateDto replyComment = CommentCreateDto.builder()
                    .postId(postId)  // 하드코딩된 9L 대신 실제 생성된 postId 사용
                    .content("대댓글 " + i)
                    .parentId(randomParentId)
                    .build();

            commentService.saveComment(replyComment, user2.getUsername());
        }

        // when
        List<CommentDto> commentList = commentService.getComment(postId);

        // then
        assertThat(commentList).hasSize(30); // 부모 댓글 10개 + 대댓글 20개
    }

}