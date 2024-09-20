package com.community.controller;

import com.community.domain.constant.FormStatus;
import com.community.domain.constant.SearchType;
import com.community.domain.dto.request.TravelReviewRequest;
import com.community.domain.dto.response.TravelReviewResponse;
import com.community.domain.dto.security.BoardPrincipal;
import com.community.service.TravelReviewService;
import com.community.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


import java.util.List;

import static com.community.domain.QTravelReview.travelReview;

@Controller
@RequestMapping("/travelReviews")
@RequiredArgsConstructor
public class TravelReviewController {
    private final TravelReviewService travelReviewService;
    private final PaginationService paginationService;


    //게시글 목록 조회
    @GetMapping
    public String travelReviews(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        Page<TravelReviewResponse> travelReviews = travelReviewService.searchTravelReviews(searchType, searchValue, pageable).map(TravelReviewResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), travelReviews.getTotalPages());


        map.addAttribute("articles", travelReviews);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchTypes", SearchType.values());
        map.addAttribute("searchTypeHashtag", SearchType.HASHTAG);

        return "travelReviews/index";
    }

    //특정 게시글 상세 조회
    @GetMapping("/{travelReviewId}")
    public String travelReview(@PathVariable Long travelReviewId, ModelMap map) {

        map.addAttribute("article", travelReview);
        map.addAttribute("totalCount", travelReviewService.getTravelReviewCount());
        map.addAttribute("searchTypeHashtag", SearchType.HASHTAG);

        return "travelReviews/detail";
    }

    @GetMapping("/search-hashtag")
    public String searchArticleHashtag(
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        Page<TravelReviewResponse> articles = travelReviewService.searchArticlesViaHashtag(searchValue, pageable).map(TravelReviewResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        List<String> hashtags = travelReviewService.getHashtags();

        map.addAttribute("articles", articles);
        map.addAttribute("hashtags", hashtags);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchType", SearchType.HASHTAG);

        return "travelReviews/search-hashtag";
    }

    @GetMapping("/form")
    public String TravelReviewForm(ModelMap map) {
        map.addAttribute("formStatus", FormStatus.CREATE);

        return "travelReviews/form";
    }

    @PostMapping("/form")
    public String postNewTravelReview(
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            TravelReviewRequest travelReviewRequest
    ) {
        travelReviewService.saveTravelReview(travelReviewRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/travelReviews";
    }

    @GetMapping("/{travelReviewId}/form")
    public String updateTravelReviewForm(@PathVariable Long travelReviewId, ModelMap map) {
        TravelReviewResponse travelReview = TravelReviewResponse.from(travelReviewService.getTravelReview(travelReviewId));

        map.addAttribute("article", travelReview);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return "travelReviews/form";
    }


    @PostMapping("/{travelReviewId}/form")
    public String updateTravelReview(
            @PathVariable Long travelReviewId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            TravelReviewRequest travelReviewRequest
    ) {
        travelReviewService.updateTravelReview(travelReviewId, travelReviewRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/travelReviews/" + travelReviewId;
    }

    @PostMapping("/{travelReviewId}/delete")
    public String deleteTravelReview(
            @PathVariable Long travelReviewId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ) {
        travelReviewService.deleteTravelReview(travelReviewId, boardPrincipal.getUsername());

        return "redirect:/travelReviews";
    }
}
