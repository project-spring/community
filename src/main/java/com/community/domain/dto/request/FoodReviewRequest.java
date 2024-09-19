package com.community.domain.dto.request;


import com.community.domain.dto.FoodReviewDto;
import com.community.domain.dto.UserAccountDto;

// FoodReviewRequest 클라이언트로부터 전달받은 게시글 작성/수정 요청 데이터를 담는 클래스입니다.
// 사용자가 제목과 내용을 입력하면 이를 바탕으로 FoodReviewDto를 생성합니다.
public record FoodReviewRequest(

    String title,  // 게시글 제목
    String content // 게시글 내용
) {

// 정적 팩토리 메서드: new 키워드를 사용하는 대신 of() 메서드로 FoodReviewRequest 객체를 생성할 수 있습니다.
// 이렇게 하면 생성자 호출을 더 직관적으로 할 수 있습니다.
public static FoodReviewRequest of(String title, String content) {
    // 전달받은 제목과 내용으로 ArticleRequest 객체를 생성하여 반환합니다.
    return new FoodReviewRequest(title, content);
}

    // toDto 메서드는 UserAccountDto를 전달받아 FoodReviewDto 객체로 변환하는 역할을 합니다.
    
    public FoodReviewDto toDto(UserAccountDto userAccountDto) {
        return FoodReviewDto.of(
            userAccountDto, // 사용자 정보
            title,          // 게시글 제목
            content         // 게시글 내용
        );
    }





}
