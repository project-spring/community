package com.community.repository.querydsl;

import com.community.domain.Article;
import com.community.domain.TravelReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface TravelReviewRepositoryCustom {
    /**
     * @deprecated 해시태그 도메인을 새로 만들었으므로 이 코드는 더 이상 사용할 필요 없다.
     * @see HashtagRepositoryCustom#findAllHashtagNames()
     */
    @Deprecated
    List<String> findAllDistinctHashtags();
    Page<TravelReview> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);

}
