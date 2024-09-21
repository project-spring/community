package com.community.controller;

import com.community.domain.Review;
import com.community.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 리뷰 목록 조회
    @GetMapping
    public String getReviews(Model model) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        return "reviews/list";
    }

    // 리뷰 상세 조회
    @GetMapping("/{id}")
    public String getReview(@PathVariable Long id, Model model) {
        Review review = reviewService.getReview(id);
        model.addAttribute("review", review);
        return "reviews/detail";
    }

    // 리뷰 작성 폼
    @GetMapping("/new")
    public String newReviewForm(Model model) {
        model.addAttribute("review", new Review());
        return "reviews/form";
    }

    // 리뷰 생성 처리
    @PostMapping
    public String createReview(@RequestParam String title,
                               @RequestParam String content,
                               @RequestParam String writer) {
        Review review = Review.of(title, content, writer);
        reviewService.createReview(review);
        return "redirect:/reviews";
    }

    // 리뷰 수정 폼
    @GetMapping("/{id}/edit")
    public String editReviewForm(@PathVariable Long id, Model model) {
        Review review = reviewService.getReview(id);
        model.addAttribute("review", review);
        return "reviews/form";
    }

    // 리뷰 수정 처리
    @PostMapping("/{id}")
    public String updateReview(@PathVariable Long id,
                               @RequestParam String title,
                               @RequestParam String content,
                               @RequestParam String writer) {
        Review review = reviewService.getReview(id);
        if (review != null) {
            review.setTitle(title);
            review.setContent(content);
            review.setWriter(writer);
            reviewService.updateReview(review);
        }
        return "redirect:/reviews";
    }

    // 리뷰 삭제 처리
    @PostMapping("/{id}/delete")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return "redirect:/reviews";
    }
}
