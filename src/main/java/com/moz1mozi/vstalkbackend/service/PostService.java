package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.common.EntityFinder;
import com.moz1mozi.vstalkbackend.dto.post.PostSort;
import com.moz1mozi.vstalkbackend.dto.post.request.PostCreateDto;
import com.moz1mozi.vstalkbackend.dto.post.request.PostUpdateDto;
import com.moz1mozi.vstalkbackend.dto.post.response.PostDto;
import com.moz1mozi.vstalkbackend.dto.vote.response.VoteOptionDto;
import com.moz1mozi.vstalkbackend.entity.Category;
import com.moz1mozi.vstalkbackend.entity.Post;
import com.moz1mozi.vstalkbackend.entity.User;
import com.moz1mozi.vstalkbackend.entity.VoteOption;
import com.moz1mozi.vstalkbackend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final VoteOptionService voteOptionService;
    private final VoteService voteService;

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

    public Slice<PostDto> getPostList(String orderBy, int page, int size) {
        LocalDateTime now = LocalDateTime.now();
        PostSort sort = PostSort.from(orderBy);
        Slice<PostDto> posts = switch (sort) {
            case CREATED_ASC -> postRepository
                    .findByIsDeletedFalse(pageRequest(page, size, Sort.by("createdAt").ascending()
                            .and(Sort.by("id").ascending())))
                    .map(Post::toDto);
            case CREATED_DESC ->
                    postRepository.findByIsDeletedFalse(pageRequest(page, size, Sort.by("createdAt").descending()
                                    .and(Sort.by("id").descending())))
                            .map(Post::toDto);
            case ENDING_SOON -> postRepository
                    .findVoteActivePosts(LocalDateTime.now(), PageRequest.of(page, size))
                    .map(Post::toDto);

            case VOTES_DESC -> postRepository
                    .findTopVotedPosts(PageRequest.of(page, size))
                    .map(Post::toDto);
        };
        setVoteCountsForPosts(posts.getContent());
        return posts;
    }

    // 특정 카테고리에 속해있는 게시물 리스트만 가져오기
    public Slice<PostDto> getPostListByCategory(String slug, int page, int size, String orderBy) {
        PostSort sort = PostSort.from(orderBy);
        Slice<PostDto> posts = switch (sort) {
            case CREATED_ASC ->
                    postRepository.findPostRowsByCategorySlugTree(slug, pageRequest(page, size, Sort.by("created_at").ascending())).map(Post::toDto);
            case CREATED_DESC ->
                    postRepository.findPostRowsByCategorySlugTree(slug, pageRequest(page, size, Sort.by("created_at").descending())).map(Post::toDto);
            case ENDING_SOON ->
                    postRepository.findPostsBySlugTreeOrderByDeadline(slug, PageRequest.of(page, size)).map(Post::toDto);
            case VOTES_DESC ->
                    postRepository.findPostsBySlugTreeOrderByVoteCount(slug, PageRequest.of(page, size)).map(Post::toDto);
        };
        setVoteCountsForPosts(posts.getContent());
        return posts;
    }

    // 검색 메서드
    public Slice<PostDto> searchPost(String keyword, int page, int size) {
        return postRepository
                .findByTitleContainingIgnoreCaseAndIsDeletedFalse(
                        keyword,
                        pageRequest(page, size, Sort.by("createdAt").descending()
                                .and(Sort.by("id").descending())))
                .map(Post::toDto);
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

    private static PageRequest pageRequest(int page, int size, Sort sort) {
        return PageRequest.of(Math.max(0, page), Math.min(Math.max(1, size), 50), sort);
    }

    public void updatePostVoteEndTime(Long postId) {
        Post post = getOrThrow(postId);
        post.updateVoteEndTime();
    }


    // 투표 수를 일괄로 설정하는 헬퍼 메서드
    private void setVoteCountsForPosts(List<PostDto> posts) {
        if (posts.isEmpty()) return;

        List<Long> postIds = posts.stream()
                .map(PostDto::getId)
                .toList();

        // 한 번의 쿼리로 모든 포스트의 투표 수 조회
        Map<Long, Long> voteCountMap = voteService.getVoteCountsForPosts(postIds);

        // 각 PostDto에 투표 수 설정
        posts.forEach(post ->
                post.setVoteCount(voteCountMap.getOrDefault(post.getId(), 0L))
        );
    }

}
