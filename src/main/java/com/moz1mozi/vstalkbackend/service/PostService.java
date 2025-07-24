package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.VoteOptionDto;
import com.moz1mozi.vstalkbackend.dto.post.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.post.PostDto;
import com.moz1mozi.vstalkbackend.entity.Category;
import com.moz1mozi.vstalkbackend.entity.Post;
import com.moz1mozi.vstalkbackend.entity.User;
import com.moz1mozi.vstalkbackend.entity.VoteOption;
import com.moz1mozi.vstalkbackend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final VoteOptionService voteOptionService;

    public Long createPost(PostCreateDto dto) {

        User byUsername = userService.findByUsername(dto.getUsername());

        Category category = categoryService.getCategory(dto.getCategoryName());

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .videoId(dto.getVideoId())
                .isSecret(dto.isSecret())
                .author(byUsername)
                .category(category)
                .voteEnabled(dto.isVoteEnabled())
                .build();

        Post savedPost = postRepository.save(post);

        if (dto.isVoteEnabled() && dto.hasVoteOptions()) {
            voteOptionService.createVoteOptions(savedPost, dto.getVoteOptions());
        }


        return savedPost.getId();
    }

    public Post getPostId(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
    }

    public PostDto getPost(Long postId) {
        PostDto dto = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다.")).toDto();

        List<VoteOptionDto> voteOption = voteOptionService.getVoteOption(postId).stream().map(VoteOption::toDto).toList();
        dto.setVoteOptionList(voteOption);
        return dto;
    }

    public List<PostDto> getPostList() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(Post::toDto).toList();
    }
}
