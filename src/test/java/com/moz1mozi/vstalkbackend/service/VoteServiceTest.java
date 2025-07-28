package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.post.request.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.user.request.UserCreateDto;
import com.moz1mozi.vstalkbackend.dto.vote.request.VoteCreateDto;
import com.moz1mozi.vstalkbackend.dto.vote.request.VoteOptionCreateDto;
import com.moz1mozi.vstalkbackend.dto.vote.response.VoteCountDto;
import com.moz1mozi.vstalkbackend.entity.Category;
import com.moz1mozi.vstalkbackend.entity.User;
import com.moz1mozi.vstalkbackend.entity.VoteOption;
import com.moz1mozi.vstalkbackend.repository.CategoryRepository;
import com.moz1mozi.vstalkbackend.repository.VoteRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class VoteServiceTest {

    @Autowired
    VoteService voteService;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    VoteOptionService voteOptionService;

    @Autowired
    PostService postService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        voteRepository.deleteAll();
    }

    @DisplayName("게시물의 투표 옵션 카운트를 구한다.")
    @Test
    void getVoteCount() {
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

        List<VoteOption> voteOptions = voteOptionService.createVoteOptions(postService.getPostId(postId), List.of(firstOption, secondOption));

        VoteCreateDto voteCreateDto = VoteCreateDto.builder()
                .postId(postId)
                .optionId(voteOptions.get(0).getId())
                .build();

        VoteCreateDto voteCreateDto2 = VoteCreateDto.builder()
                .postId(postId)
                .optionId(voteOptions.get(1).getId())
                .build();
        // when
        voteService.vote(voteCreateDto, username);
        voteService.vote(voteCreateDto2, user2.getUsername());

        // then
        List<VoteCountDto> voteCount = voteService.getVoteCount(postId);
        System.out.println(voteCount);

        assertThat(voteCount).hasSize(2);
        assertThat(voteCount.stream().mapToLong(VoteCountDto::getCount).sum()).isEqualTo(2);
    }

    @DisplayName("특정 유저의 투표 여부를 확인한다.")
    @Test
    void isVoted() {
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

        List<VoteOption> voteOptions = voteOptionService.createVoteOptions(postService.getPostId(postId), List.of(firstOption, secondOption));

        VoteCreateDto voteCreateDto = VoteCreateDto.builder()
                .postId(postId)
                .optionId(voteOptions.get(0).getId())
                .build();
        // when
        voteService.vote(voteCreateDto, username);

        // when
        boolean voted = voteService.isVoted(postId, username);
        boolean voted2 = voteService.isVoted(postId, user2.getUsername());
        // then
        assertThat(voted).isTrue();
        assertThat(voted2).isFalse();
    }

}