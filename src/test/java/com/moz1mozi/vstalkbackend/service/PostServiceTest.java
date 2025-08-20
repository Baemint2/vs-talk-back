package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.post.request.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.post.request.PostUpdateDto;
import com.moz1mozi.vstalkbackend.dto.post.response.PostDto;
import com.moz1mozi.vstalkbackend.dto.user.request.UserCreateDto;
import com.moz1mozi.vstalkbackend.dto.vote.request.VoteOptionCreateDto;
import com.moz1mozi.vstalkbackend.entity.Category;
import com.moz1mozi.vstalkbackend.entity.Post;
import com.moz1mozi.vstalkbackend.entity.User;
import com.moz1mozi.vstalkbackend.repository.CategoryRepository;
import com.moz1mozi.vstalkbackend.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.moz1mozi.vstalkbackend.dto.post.PostSort.CREATED_ASC;
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
    @Autowired
    private PostRepository postRepository;

    @DisplayName("특정 카테고리에 속해있는 게시물 리스트만 가져온다.")
    @Test
    void getPostListByCategory() {
        // given
        Category sports = Category.builder()
                .name("sports")
                .build();

        Category foods = Category.builder()
                .name("food")
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
            postService.createPost(post, username);
        }

        // when
        Slice<PostDto> postListByCategory = postService.getPostListByCategory(savedSports.getName(),  0, 20, "desc");

        // then
        System.out.println(postListByCategory);
        assertThat(postListByCategory).hasSize(25);
    }

    @DisplayName("특정 게시물을 삭제합니다.")
    @Test
    void deletePost() {
        // given
        Category foods = Category.builder()
                .name("food")
                .build();

        Category savedFood = categoryRepository.save(foods);

        UserCreateDto userCreateDto = UserCreateDto.builder()
                .username("testUser")
                .password(passwordEncoder.encode("password"))
                .nickname("testNickname")
                .build();

        User user = userService.createUser(userCreateDto);

        String username = user.getUsername();

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
                .categoryId(savedFood.getId())
                .voteEnabled(true)
                .voteOptions(List.of(firstOption, secondOption))
                .build();

        Long post1 = postService.createPost(post, username);

        // when // then
        postService.deletePost(post1);

        PostDto post2 = postService.getPost(post1);
        assertThat(post2).isNotNull();
        assertThat(post2.isDeleted()).isTrue();
    }

    @DisplayName("특정 게시물을 수정합니다.")
    @Test
    void updatePost() {
        // given
        Category sports = Category.builder()
                .name("sports")
                .build();

        Category foods = Category.builder()
                .name("food")
                .build();

        Category savedSports = categoryRepository.save(sports);

        Category savedFood = categoryRepository.save(foods);

        UserCreateDto userCreateDto = UserCreateDto.builder()
                .username("testUser")
                .password(passwordEncoder.encode("password"))
                .nickname("testNickname")
                .build();

        User user = userService.createUser(userCreateDto);

        String username = user.getUsername();

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
                .categoryId(savedFood.getId())
                .voteEnabled(true)
                .voteOptions(List.of(firstOption, secondOption))
                .build();

        Long post1 = postService.createPost(post, username);
        PostUpdateDto updateDto = PostUpdateDto.builder()
                .title("modified title")
                .content("modified content")
                .categoryId(savedSports.getId())
                .build();
        // when
        postService.updatePost(post1, updateDto);

        // then
        PostDto postDto = postService.getPost(post1);
        assertThat(postDto)
                .extracting(PostDto::getTitle, PostDto::getContent, PostDto::getCategoryName)
                .containsExactly("modified title", "modified content", "sports");
    }

    @DisplayName("스크롤링 테스트")
    @Test
    void sliceTest() {
        // given
        Category sports = Category.builder()
                .name("sports")
                .build();

        Category foods = Category.builder()
                .name("food")
                .build();

        Category savedSports = categoryRepository.save(sports);

        Category savedFood = categoryRepository.save(foods);

        UserCreateDto userCreateDto = UserCreateDto.builder()
                .username("testUser")
                .password(passwordEncoder.encode("password"))
                .nickname("testNickname")
                .build();

        User user = userService.createUser(userCreateDto);

        String username = user.getUsername();

        VoteOptionCreateDto firstOption = VoteOptionCreateDto.builder()
                .optionText("test option 1")
                .build();

        VoteOptionCreateDto secondOption = VoteOptionCreateDto.builder()
                .optionText("test option 2")
                .build();

        for (int i = 0; i < 21; i++) {
            PostCreateDto post = PostCreateDto.builder()
                    .title("test title" + i)
                    .content("test content")
                    .videoId("test videoId" + i)
                    .isSecret(false)
                    .username(username)
                    .categoryId(savedFood.getId())
                    .voteEnabled(true)
                    .voteOptions(List.of(firstOption, secondOption))
                    .build();
            postService.createPost(post, username);
        }

        // when
        Slice<PostDto> postList = postService.getPostList(CREATED_ASC.getCode(), 0, 20);
        // then
        System.out.println(postList.getSize());
        System.out.println(postList.hasNext());
        if (postList.hasNext()) {
           Slice<PostDto> secondList = postService.getPostList(CREATED_ASC.getCode(), 1, 20);
            System.out.println(secondList.getSize());
            System.out.println(secondList.hasNext());
            System.out.println(secondList.getContent());
        }
    }

}