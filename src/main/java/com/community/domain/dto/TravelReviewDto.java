package com.community.domain.dto;


import com.community.domain.TravelReview;
import com.community.domain.UserAccount;

import java.time.LocalDateTime;


public record TravelReviewDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static TravelReviewDto of(UserAccountDto userAccountDto, String title, String content) {
        return new TravelReviewDto(null, userAccountDto, title, content,
                null, null, null, null);
    }

    public static TravelReviewDto of(Long id, UserAccountDto userAccountDto, String title, String content,
                                     LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new TravelReviewDto(id, userAccountDto, title, content, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static TravelReviewDto from(TravelReview entity) {
        return new TravelReviewDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public TravelReview toEntity(UserAccount userAccount) {
        return TravelReview.of(
                userAccount,
                title,
                content
        );
    }
}
