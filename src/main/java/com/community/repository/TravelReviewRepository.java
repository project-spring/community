package com.community.repository;

import aj.org.objectweb.asm.commons.Remapper;

import com.community.domain.Article;
import com.community.domain.QTravelReview;
import com.community.domain.TravelReview;
import com.community.repository.querydsl.TravelReviewRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TravelReviewRepository extends
        JpaRepository<TravelReview, Long>,
        TravelReviewRepositoryCustom,
        QuerydslPredicateExecutor<TravelReview>,
        QuerydslBinderCustomizer<QTravelReview> {

    Page<TravelReview> findByTitleContaining(String title, Pageable pageable);
    Page<TravelReview> findByContentContaining(String content, Pageable pageable);
    Page<TravelReview> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<TravelReview> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);

    void deleteByIdAndUserAccount_UserId(long travelReviewId, String userId);

    @Override
    default void customize(QuerydslBindings bindings, QTravelReview root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.hashtags, root.createdAt, root.createdBy);
        bindings.bind(root.title).first(StringExpression::likeIgnoreCase);
        bindings.bind(root.hashtags.any().hashtagName).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }

}
