package com.moz1mozi.vstalkbackend.domain.post.service;

import com.moz1mozi.vstalkbackend.domain.post.dto.request.VoteOptionCreateDto;
import com.moz1mozi.vstalkbackend.domain.post.entity.Post;
import com.moz1mozi.vstalkbackend.domain.post.entity.VoteOption;
import com.moz1mozi.vstalkbackend.domain.post.repository.VoteOptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VoteOptionService {

    private final VoteOptionRepository voteOptionRepository;

    public List<VoteOption> createVoteOptions(Post post, List<VoteOptionCreateDto> optionDtos) {
        int existingCount = voteOptionRepository.countByPost(post);
        AtomicInteger orderCounter = new AtomicInteger(existingCount);

        List<VoteOption> newVoteOptions = optionDtos.stream()
                .filter(dto -> dto.getId() == null)
                .map(dto -> VoteOption.builder()
                        .post(post)
                        .optionText(dto.getOptionText())
                        .displayOrder(orderCounter.incrementAndGet())
                        .color(dto.getColor() != null ? dto.getColor() : generateDefaultColor(0))
                        .build())
                .toList();

        if (newVoteOptions.isEmpty()) {
            return Collections.emptyList();
        }

        return voteOptionRepository.saveAll(newVoteOptions);
    }

    private String generateDefaultColor(int index) {
        String[] defaultColors = {
                "#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4",
                "#FFEAA7", "#DDA0DD", "#98D8C8", "#F7DC6F"
        };
        return defaultColors[index % defaultColors.length];
    }

    public List<VoteOption> getVoteOption(Long postId) {
        return voteOptionRepository.findAllByPostId(postId);
    }

    public void removeVoteOption(List<Long> ids) {
        for (Long id : ids) {
            voteOptionRepository.deleteById(id);
        }
    }

}
