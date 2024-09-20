package com.community.repository.querydsl;

import com.community.domain.*;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Collection;
import java.util.List;

public class TravelReviewRepositoryCustomImpl extends QuerydslRepositorySupport implements TravelReviewRepositoryCustom {
    public TravelReviewRepositoryCustomImpl() {
        super(TravelReview.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QTravelReview travelReview = QTravelReview.travelReview;

        return from(travelReview)
                .distinct()
                .select(travelReview.hashtags.any().hashtagName)
                .fetch();
    }

    @Override
    public Page<TravelReview> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable) {
        QHashtag hashtag = QHashtag.hashtag;
        QTravelReview travelReview = QTravelReview.travelReview;

        JPQLQuery<TravelReview> query = from(travelReview)
                .innerJoin(travelReview.hashtags, hashtag)
                .where(hashtag.hashtagName.in(hashtagNames));
        List<TravelReview> travelReviews = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(travelReviews, pageable, query.fetchCount());
    }

}
