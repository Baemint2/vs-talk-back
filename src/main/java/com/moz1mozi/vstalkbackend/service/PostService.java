package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.vote.response.VoteOptionDto;
import com.moz1mozi.vstalkbackend.dto.post.request.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.post.response.PostDto;
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

        Category category = categoryService.getCategory(dto.getCategoryId());

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

    public List<PostDto> getPostList(String orderCondition ) {
        List<Post> posts;
        if ( orderCondition != null && orderCondition.equals("asc")) {
            posts = postRepository.findAllByOrderByCreatedAtAsc();
        } else {
            posts = postRepository.findAllByOrderByCreatedAtDesc();
        }
        return posts.stream().map(Post::toDto).toList();
    }

    // 특정 카테고리에 속해있는 게시물 리스트만 가져오기
    public List<PostDto> getPostListByCategory(String slug) {
        List<Post> categoryPosts = postRepository.findByCategorySlugOrderByCreatedAtDesc(slug);
        return categoryPosts.stream().map(Post::toDto).toList();
    }

    // 검색 메서드
    public List<PostDto> searchPost(String keyword) {
        List<Post> searchPosts = postRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(keyword);
        return searchPosts.stream().map(Post::toDto).toList();
    }
}
