package com.community.domain.dto.response;

import com.community.domain.dto.TravelReviewDto;

import java.time.LocalDateTime;

public record TravelReviewResponse(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        String email,
        String nickname
) {
    public static TravelReviewResponse of(Long id, String title, String content, LocalDateTime createdAt, String email, String nickname) {
        return new TravelReviewResponse(id, title, content, createdAt, email, nickname);
    }

    public static TravelReviewResponse from(TravelReviewDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new TravelReviewResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }

}
