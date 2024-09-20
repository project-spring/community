package com.community.service;

import com.community.domain.TravelReview;
import com.community.domain.UserAccount;
import com.community.domain.dto.TravelReviewDto;
import com.community.repository.TravelReviewRepository;
import com.community.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TravelReviewService {

    private final TravelReviewRepository travelReviewRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public TravelReviewDto getTravelReview(Long travelReviewId) {
        return travelReviewRepository.findById(travelReviewId)
                .map(TravelReviewDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - travelReviewId: " + travelReviewId));
    }

    //dto 객체를 엔티티로 변환하여 데이터베이스에 저장
    public void saveTravelReview(TravelReviewDto dto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

        TravelReview travelReview = dto.toEntity(userAccount);
        travelReviewRepository.save(travelReview);
    }


    public void updateTravelReview(Long travelReviewId, TravelReviewDto dto) {
        try {
            TravelReview travelReview = travelReviewRepository.getReferenceById(travelReviewId);
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

            if (travelReview.getUserAccount().equals(userAccount)) {
                if (dto.title() != null) { travelReview.setTitle(dto.title()); }
                if (dto.content() != null) { travelReview.setContent(dto.content()); }

            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    public void deleteTravelReview(long travelReviewId, String userId) {
        TravelReview travelReview = travelReviewRepository.getReferenceById(travelReviewId);

        //travelReviewRepository.deleteByIdAndUserAccount_UserId(travelReviewId, userId);
        travelReviewRepository.flush();

    }

    public long getTravelReviewCount() {
        return travelReviewRepository.count();
    }



}
