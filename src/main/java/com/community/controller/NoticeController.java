package com.community.controller;

import com.community.domain.Notice;
import com.community.domain.dto.security.BoardPrincipal;
import com.community.service.NoticeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // 공지사항 목록 조회
    @GetMapping
    public String getNotices(Model model) {
        List<Notice> notices = noticeService.getAllNotices();
        model.addAttribute("notices", notices);
        return "notices/list";
    }

    // 공지사항 상세 조회
    @GetMapping("/{id}")
    public String getNotice(@PathVariable Long id, Model model) {
        Notice notice = noticeService.getNotice(id);
        model.addAttribute("notice", notice);
        return "notices/detail";
    }

    // 공지사항 작성 폼
    @GetMapping("/new")
    public String newNoticeForm(@AuthenticationPrincipal BoardPrincipal boardPrincipal, Model model) {
        model.addAttribute("notice", new Notice());
        model.addAttribute("username", boardPrincipal.nickname());
        return "notices/form";
    }

    // 공지사항 생성 처리
    @PostMapping
    public String createNotice(@RequestParam String title, @AuthenticationPrincipal BoardPrincipal boardPrincipal,
        @RequestParam String content,
        @RequestParam String writer) {
        Notice notice = Notice.of(title, content, writer);

        String username = boardPrincipal.nickname();

        noticeService.createNotice(notice);
        return "redirect:/notices";
    }

    // 공지사항 수정 폼
    @GetMapping("/{id}/edit")
    public String editNoticeForm(@PathVariable Long id, @AuthenticationPrincipal BoardPrincipal boardPrincipal, Model model) {
        Notice notice = noticeService.getNotice(id);
        model.addAttribute("notice", notice);
        model.addAttribute("username", boardPrincipal.nickname());
        return "notices/form";
    }

    // 공지사항 수정 처리
    @PostMapping("/{id}")
    public String updateNotice(@PathVariable Long id, @AuthenticationPrincipal BoardPrincipal boardPrincipal,
        @RequestParam String title,
        @RequestParam String content,
        @RequestParam String writer) {
        String username = boardPrincipal.nickname();

        Notice notice = noticeService.getNotice(id);
        if (notice != null) {
            notice.setTitle(title);
            notice.setContent(content);
            notice.setWriter(username);
            noticeService.updateNotice(notice);
        }
        return "redirect:/notices";
    }

    // 공지사항 삭제 처리
    @PostMapping("/{id}/delete")
    public String deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return "redirect:/notices";
    }
}
