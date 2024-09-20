package com.community.service;

import com.community.domain.Hashtag;
import com.community.domain.TravelReview;
import com.community.domain.UserAccount;
import com.community.domain.constant.SearchType;
import com.community.domain.dto.TravelReviewDto;
import com.community.repository.HashtagRepository;
import com.community.repository.TravelReviewRepository;
import com.community.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TravelReviewService {

    private final HashtagService hashtagService;
    private final TravelReviewRepository travelReviewRepository;
    private final UserAccountRepository userAccountRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional(readOnly = true)
    public Page<TravelReviewDto> searchTravelReviews(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return travelReviewRepository.findAll(pageable).map(TravelReviewDto::from);
        }

        return switch (searchType) {
            case TITLE -> travelReviewRepository.findByTitleContaining(searchKeyword, pageable).map(TravelReviewDto::from);
            case CONTENT -> travelReviewRepository.findByContentContaining(searchKeyword, pageable).map(TravelReviewDto::from);
            case ID -> travelReviewRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(TravelReviewDto::from);
            case NICKNAME -> travelReviewRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(TravelReviewDto::from);
            case HASHTAG -> travelReviewRepository.findByHashtagNames(
                            Arrays.stream(searchKeyword.split(" ")).toList(),
                            pageable
                    )
                    .map(TravelReviewDto::from);
        };
    }

    @Transactional(readOnly = true)
    public TravelReviewDto getTravelReview(Long travelReviewId) {
        return travelReviewRepository.findById(travelReviewId)
                .map(TravelReviewDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - travelReviewId: " + travelReviewId));
    }


    //dto 객체를 엔티티로 변환하여 데이터베이스에 저장
    public void saveTravelReview(TravelReviewDto dto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
        Set<Hashtag> hashtags = renewHashtagsFromContent(dto.content());

        TravelReview travelReview = dto.toEntity(userAccount);
        travelReview.addHashtags(hashtags);
        travelReviewRepository.save(travelReview);
    }


    public void updateTravelReview(Long travelReviewId, TravelReviewDto dto) {
        try {
            TravelReview travelReview = travelReviewRepository.getReferenceById(travelReviewId);
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

            if (travelReview.getUserAccount().equals(userAccount)) {
                if (dto.title() != null) { travelReview.setTitle(dto.title()); }
                if (dto.content() != null) { travelReview.setContent(dto.content()); }

                Set<Long> hashtagIds = travelReview.getHashtags().stream()
                        .map(Hashtag::getId)
                        .collect(Collectors.toUnmodifiableSet());
                travelReview.clearHashtags();
                travelReviewRepository.flush();

                hashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);

                Set<Hashtag> hashtags = renewHashtagsFromContent(dto.content());
                travelReview.addHashtags(hashtags);
            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    public void deleteTravelReview(long travelReviewId, String userId) {
        TravelReview travelReview = travelReviewRepository.getReferenceById(travelReviewId);
        Set<Long> hashtagIds = travelReview.getHashtags().stream()
                .map(Hashtag::getId)
                .collect(Collectors.toUnmodifiableSet());

        travelReviewRepository.deleteByIdAndUserAccount_UserId(travelReviewId, userId);
        travelReviewRepository.flush();

        hashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);
    }

    public long getTravelReviewCount() {
        return travelReviewRepository.count();
    }

    @Transactional(readOnly = true)
    public Page<TravelReviewDto> searchArticlesViaHashtag(String hashtagName, Pageable pageable) {
        if (hashtagName == null || hashtagName.isBlank()) {
            return Page.empty(pageable);
        }

        return travelReviewRepository.findByHashtagNames(List.of(hashtagName), pageable)
                .map(TravelReviewDto::from);
    }

    public List<String> getHashtags() {
        return hashtagRepository.findAllHashtagNames(); // TODO: HashtagService 로 이동을 고려해보자.
    }

    private Set<Hashtag> renewHashtagsFromContent(String content) {
        Set<String> hashtagNamesInContent = hashtagService.parseHashtagNames(content);
        Set<Hashtag> hashtags = hashtagService.findHashtagsByNames(hashtagNamesInContent);
        Set<String> existingHashtagNames = hashtags.stream()
                .map(Hashtag::getHashtagName)
                .collect(Collectors.toUnmodifiableSet());

        hashtagNamesInContent.forEach(newHashtagName -> {
            if (!existingHashtagNames.contains(newHashtagName)) {
                hashtags.add(Hashtag.of(newHashtagName));
            }
        });

        return hashtags;
    }


}
