package com.community.service;

import com.community.domain.FoodReview;
import com.community.domain.UserAccount;
import com.community.domain.dto.FoodReviewDto;
import com.community.repository.FoodReviewRepository;
import com.community.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FoodReviewService {

    private final FoodReviewRepository foodReviewRepository;
    private final UserAccountRepository userAccountRepository;


    // 특정 게시글을 ID로 조회하는 메서드
    @Transactional(readOnly = true)
    public FoodReviewDto getFoodReview(Long foodReviewId) {
        return foodReviewRepository.findById(foodReviewId)
            .map(FoodReviewDto::from)
            .orElseThrow(() -> new EntityNotFoundException("해당 리뷰를 찾을 수 없습니다 - reviewId: " + foodReviewId)); // 없을 경우 예외 발생
    }


    // 새로운 게시글 저장 메서드
    public void saveFoodReview(FoodReviewDto dto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());  // 작성자 정보 조회

        // DTO를 엔티티로 변환하여 저장
        FoodReview foodReview = dto.toEntity(userAccount);
        foodReviewRepository.save(foodReview);
    }

    // 게시글 수정 메서드
    public void updateFoodReview(Long foodReviewId, FoodReviewDto dto) {
        try {
            FoodReview foodReview = foodReviewRepository.getReferenceById(foodReviewId);  // 수정할 게시글 조회
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId()); // 작성자 정보 조회

            // 작성자 정보가 일치하는지 확인
            if (foodReview.getUserAccount().equals(userAccount)) {
                // 제목과 내용이 비어 있지 않으면 해당 값으로 수정
                if (dto.title() != null) { foodReview.setTitle(dto.title()); }
                if (dto.content() != null) { foodReview.setContent(dto.content()); }

                foodReviewRepository.flush(); // 변경 사항 반영
            }
        } catch (EntityNotFoundException e) {
            log.warn("리뷰 업데이트 실패. 리뷰를 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    // 게시글 삭제
    public void deleteFoodReview(Long foodReviewId, String userId) {
        FoodReview foodReview = foodReviewRepository.getReferenceById(foodReviewId); // 삭제할 게시글 조회

      //  foodReviewRepository.deleteByIdAndUserAccount_UserId(foodReviewId, userId); // 게시글 삭제 (작성자와 일치하는지 확인)
        foodReviewRepository.flush(); // 삭제 반영
    }

    // 총 게시글 수 조회
    public long getFoodReviewCount() {
        return foodReviewRepository.count();
    }
}
