package com.community.domain.dto.response;

import com.community.domain.dto.FoodReviewDto;
import java.time.LocalDateTime;

// FoodReviewResponse 클래스는 게시글 조회 시 클라이언트에게 전달할 데이터를 담는 클래스입니다.
// FoodReviewDto에서 필요한 정보만 추출하여 클라이언트가 필요한 응답 형식으로 변환합니다.
public record FoodReviewResponse(

    Long id,             // 게시글 ID
    String title,        // 게시글 제목
    String content,      // 게시글 내용
    LocalDateTime createdAt, // 게시글 작성 시간
    String email,        // 작성자의 이메일
    String nickname      // 작성자의 닉네임
) {

    // 정적 팩토리 메서드: FoodReviewResponse 객체를 생성할 때 사용합니다.
    // 전달받은 id, title, content, createdAt, email, nickname을 바탕으로 객체를 생성합니다.
    public static FoodReviewResponse of(Long id, String title, String content,
        LocalDateTime createdAt, String email, String nickname) {
        return new FoodReviewResponse(id, title, content, createdAt, email, nickname);
    }

    // FoodReviewDto 객체를 받아서 FoodReviewResponse 객체로 변환하는 메서드입니다.
    // Dto에서 클라이언트가 필요로 하는 정보만 추출하여 응답용 객체를 생성합니다.
    public static FoodReviewResponse from(FoodReviewDto dto) {
        // 닉네임이 없을 경우, 유저 아이디를 대신 사용합니다.
        String nickname = dto.userAccountDto().nickname(); // 유저의 닉네임을 가져옵니다.
        if (nickname == null || nickname.isBlank()) {
            // 닉네임이 비어있거나 null인 경우, 유저 아이디를 닉네임 대신 사용합니다.
            nickname = dto.userAccountDto().userId();
        }
        // new로 새 객체를 만들어 반환하는 이유는, FoodReviewResponse는 record입니다.
        // 그러므로 기존 데이터를 수정할 수 없고, 새로운 데이터를 조합해서 응답용 객체를 만들어야 합니다.
        //따라서 new FoodReviewResponse(...)를 사용해서 변환된 데이터를 기반으로 새로운 응답 객체를 만들어 반환하는 것
        return new FoodReviewResponse(
            dto.id(),
            dto.title(),
            dto.content(),
            dto.createdAt(),
            dto.userAccountDto().email(),
            nickname
        );
    }
}