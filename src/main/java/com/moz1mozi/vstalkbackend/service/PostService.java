package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.common.EntityFinder;
import com.moz1mozi.vstalkbackend.dto.post.PostSort;
import com.moz1mozi.vstalkbackend.dto.post.request.PostUpdateDto;
import com.moz1mozi.vstalkbackend.dto.vote.response.VoteOptionDto;
import com.moz1mozi.vstalkbackend.dto.post.request.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.post.response.PostDto;
import com.moz1mozi.vstalkbackend.entity.Category;
import com.moz1mozi.vstalkbackend.entity.Post;
import com.moz1mozi.vstalkbackend.entity.User;
import com.moz1mozi.vstalkbackend.entity.VoteOption;
import com.moz1mozi.vstalkbackend.repository.PostRepository;
import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final VoteOptionService voteOptionService;

    public Long createPost(PostCreateDto dto, String username) {

        User byUsername = userService.findByUsername(username);

        Category category = categoryService.getCategory(dto.getCategoryId());

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .videoId(dto.getVideoId())
                .isSecret(dto.isSecret())
                .author(byUsername)
                .category(category)
                .voteEnabled(dto.isVoteEnabled())
                .voteEndTime(dto.getVoteEndTime())
                .build();

        Post savedPost = postRepository.save(post);

        if (dto.isVoteEnabled() && dto.hasVoteOptions()) {
            voteOptionService.createVoteOptions(savedPost, dto.getVoteOptions());
        }

        return savedPost.getId();
    }

    public Post getPostId(Long postId) {
        return getOrThrow(postId);
    }

    public PostDto getPost(Long postId) {
        PostDto dto = getOrThrow(postId).toDto();
        List<VoteOptionDto> voteOption = voteOptionService.getVoteOption(postId).stream().map(VoteOption::toDto).toList();
        dto.setVoteOptionList(voteOption);
        return dto;
    }

    public List<PostDto> getPostList(String orderby) {
        LocalDateTime now = LocalDateTime.now();
        // range: "24h", "7d", "all" 등 기간 필터(옵션)
        PostSort sort = PostSort.from(orderby);
        return switch (sort) {
            case CREATED_ASC  -> postRepository.findByIsDeletedFalse(Sort.by("createdAt").ascending()).stream().map(Post::toDto).toList();
            case CREATED_DESC -> postRepository.findByIsDeletedFalse((Sort.by("createdAt").descending())).stream().map(Post::toDto).toList();
            case VOTES_DESC   -> postRepository.findTopVotedPosts().stream().map(Post::toDto).toList();
            case ENDING_SOON  -> postRepository.findVoteActivePosts(now).stream().map(Post::toDto).toList();
        };
    }

    // 특정 카테고리에 속해있는 게시물 리스트만 가져오기
    public List<PostDto> getPostListByCategory(String slug) {
        List<Post> categoryPosts = postRepository.findByCategorySlugAndIsDeletedFalse(slug, Sort.by(Sort.Direction.DESC, "createdAt"));
        return categoryPosts.stream().map(Post::toDto).toList();
    }

    // 검색 메서드
    public List<PostDto> searchPost(String keyword) {
        List<Post> searchPosts = postRepository.findByTitleContainingIgnoreCaseAndIsDeletedFalse(keyword, Sort.by(Sort.Direction.DESC, "createdAt"));
        return searchPosts.stream().map(Post::toDto).toList();
    }

    // 게시글 삭제
    public void deletePost(Long postId) {
        Post post = getOrThrow(postId);

        post.changeDeleted();
    }

    // 게시글 수정
    public void updatePost(Long postId, PostUpdateDto dto) {
        Post post = getOrThrow(postId);
        Category category = categoryService.getCategory(dto.getCategoryId());

        boolean voteEnabled = post.isVoteEnabled();
        if (!voteEnabled) {
            throw new IllegalStateException("종료된 투표입니다.");
        }
        post.updatePost(dto.getTitle(),
                        dto.getContent(),
                        dto.getVideoId(),
                        dto.isDeleted(),
                        dto.isSecret(),
                        category,
                        dto.getVoteEndTime());

        if (dto.hasVoteOptions()) {
            voteOptionService.createVoteOptions(post, dto.getVoteOptions());
        }
    }

    private Post getOrThrow(Long postId) {
        return EntityFinder.findOrThrow(postRepository, postId, "존재하지 않는 게시글입니다.");
    }

    @Scheduled(fixedDelay = 60 * 1000) // 1분마다 실행
    public void disableExpiredVotes() {
        log.info("스케줄링합니다.");
        LocalDateTime now = LocalDateTime.now();
        postRepository.disableVotesPastDeadline(now);
    }
}
