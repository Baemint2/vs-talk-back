package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.post.request.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.post.response.PostDto;
import com.moz1mozi.vstalkbackend.dto.user.request.UserCreateDto;
import com.moz1mozi.vstalkbackend.dto.vote.request.VoteOptionCreateDto;
import com.moz1mozi.vstalkbackend.entity.Category;
import com.moz1mozi.vstalkbackend.entity.User;
import com.moz1mozi.vstalkbackend.repository.CategoryRepository;
import jakarta.transaction.Transactional;
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
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @DisplayName("특정 카테고리에 속해있는 게시물 리스트만 가져온다.")
    @Test
    void getPostListByCategory() {
        // given
        Category sports = Category.builder()
                .name("sports")
                .isDeleted(false)
                .build();

        Category foods = Category.builder()
                .name("food")
                .isDeleted(false)
                .build();

        Category savedSports = categoryRepository.save(sports);
        Category savedFood = categoryRepository.save(foods);

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

        VoteOptionCreateDto firstOption = VoteOptionCreateDto.builder()
                .optionText("test option 1")
                .build();

        VoteOptionCreateDto secondOption = VoteOptionCreateDto.builder()
                .optionText("test option 2")
                .build();

        for (int i = 1; i <= 50; i++) {
            PostCreateDto post = PostCreateDto.builder()
                    .title("test title" + i)
                    .content("test content")
                    .videoId("test videoId")
                    .isSecret(false)
                    .username(username)
                    .categoryId(i % 2 == 0 ? savedSports.getId() : savedFood.getId())
                    .voteEnabled(true)
                    .voteOptions(List.of(firstOption, secondOption))
                    .build();
            postService.createPost(post);
        }

        // when
        List<PostDto> postListByCategory = postService.getPostListByCategory(savedSports.getName());

        // then
        System.out.println(postListByCategory);
        assertThat(postListByCategory).hasSize(25);
    }
}