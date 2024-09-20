package com.community.domain.dto.request;

import com.community.domain.dto.HashtagDto;
import com.community.domain.dto.TravelReviewDto;
import com.community.domain.dto.UserAccountDto;

import java.util.Set;

public record TravelReviewRequest(
        String title,
        String content
) {

    public static TravelReviewRequest of(String title, String content) {
        return new TravelReviewRequest(title, content);
    }

    public TravelReviewDto toDto(UserAccountDto userAccountDto) {
        return toDto(userAccountDto, null);
    }

    public TravelReviewDto toDto(UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos) {
        return TravelReviewDto.of(
                userAccountDto,
                title,
                content,
                hashtagDtos
        );
    }



}
