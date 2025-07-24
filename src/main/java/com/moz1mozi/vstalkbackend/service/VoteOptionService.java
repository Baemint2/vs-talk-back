package com.moz1mozi.vstalkbackend.service;

import com.moz1mozi.vstalkbackend.dto.vote.request.VoteOptionCreateDto;
import com.moz1mozi.vstalkbackend.entity.Post;
import com.moz1mozi.vstalkbackend.entity.VoteOption;
import com.moz1mozi.vstalkbackend.repository.VoteOptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VoteOptionService {

    private final VoteOptionRepository voteOptionRepository;

    public List<VoteOption> createVoteOptions(Post post, List<VoteOptionCreateDto> optionDtos) {
        List<VoteOption> voteOptions = new ArrayList<>();

        for (int i = 0; i < optionDtos.size(); i++) {
            VoteOptionCreateDto optionDto = optionDtos.get(i);

            VoteOption voteOption = VoteOption.builder()
                    .post(post)
                    .optionText(optionDto.getOptionText())
                    .displayOrder(i + 1)
                    .color(optionDto.getColor() != null ? optionDto.getColor() : generateDefaultColor(i))
                    .build();

            voteOptions.add(voteOption);
        }

        return voteOptionRepository.saveAll(voteOptions);
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

}
