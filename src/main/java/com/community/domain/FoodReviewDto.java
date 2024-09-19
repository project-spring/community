package com.community.domain;

import com.community.domain.dto.UserAccountDto;
import java.time.LocalDateTime;

public record FoodReviewDto(
    Long id,                             // 게시글의 ID (Primary Key)
    UserAccountDto userAccountDto,        // 작성자 정보 (UserAccountDto 사용)
    String title,                        // 게시글 제목
    String content,                      // 게시글 내용
    // Set<HashtagDto> hashtagDtos,          // 게시글에 연결된 해시태그들
    LocalDateTime createdAt,             // 생성된 시간
    String createdBy,                    // 게시글 작성자 정보 (생성자)
    LocalDateTime modifiedAt,            // 마지막 수정 시간
    String modifiedBy                    // 마지막 수정자 정보

) {


    // 새로운 게시글을 생성할 때 사용할 정적 팩토리 메서드입니다.
    // 기본적으로 ID, 생성일, 수정일 등의 값이 없는 상태로 객체를 생성합니다.
    public static FoodReviewDto of(UserAccountDto userAccountDto, String title, String content) {
        // 새로 생성된 게시글이므로 ID, 생성일자, 수정일자는 null로 설정
        return new FoodReviewDto(null, userAccountDto, title, content,null, null, null, null);
    }

    // ID와 날짜 정보 등을 포함해 ArticleDto 객체를 생성하는 정적 팩토리 메서드입니다.
    // 게시글 수정 등의 경우, 기존 데이터를 포함한 DTO를 생성할 때 사용됩니다.
    public static FoodReviewDto of(Long id, UserAccountDto userAccountDto, String title, String content,  LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        // 모든 필드가 주어진 상태로 DTO를 생성
        return new FoodReviewDto(id, userAccountDto, title, content, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    // Article 엔티티를 ArticleDto로 변환하는 정적 메서드입니다.
    // 엔티티 객체를 받아와 DTO로 변환하는 로직을 제공합니다.
    public static FoodReviewDto from(FoodReview entity) {
        return new FoodReviewDto(
            entity.getId(),                                  // Article 엔티티의 ID
            UserAccountDto.from(entity.getUserAccount()),     // Article 엔티티에 연결된 UserAccount를 UserAccountDto로 변환
            entity.getTitle(),                               // Article 엔티티의 제목
            entity.getContent(),                             // Article 엔티티의 내용
          //  entity.getHashtags().stream()                    // 해시태그 엔티티를 HashtagDto로 변환
          //      .map(HashtagDto::from)                   // 각 Hashtag 엔티티를 HashtagDto로 변환
           //     .collect(Collectors.toUnmodifiableSet()),// 변경 불가능한 Set으로 변환
            entity.getCreatedAt(),                           // Article 엔티티의 생성 시간
            entity.getCreatedBy(),                           // Article 엔티티의 작성자
            entity.getModifiedAt(),                          // Article 엔티티의 마지막 수정 시간
            entity.getModifiedBy()                           // Article 엔티티의 마지막 수정자
        );
    }

    // DTO 데이터를 엔티티로 변환하는 메서드입니다.
    // 이 메서드는 DTO의 데이터를 바탕으로 Article 엔티티 객체를 생성합니다.
    public FoodReview toEntity(UserAccount userAccount) {
        return FoodReview.of(
            userAccount,     // UserAccount 정보를 전달하여 엔티티에 설정
            title,           // 제목을 엔티티에 설정
            content          // 내용을 엔티티에 설정
        );
    }
}
