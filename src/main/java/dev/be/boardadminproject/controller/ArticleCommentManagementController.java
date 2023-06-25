package dev.be.boardadminproject.controller;

import dev.be.boardadminproject.dto.ArticleCommentDto;
import dev.be.boardadminproject.service.ArticleCommentManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/management/article-comments")
@Controller
public class ArticleCommentManagementController {

    private final ArticleCommentManagementService articleCommentManagementService;


    @GetMapping
    public String articleComments(
            Model model,
            HttpServletRequest request
    ) {
        model.addAttribute("request", request.getRequestURI());
        model.addAttribute("comments",
                articleCommentManagementService.getArticleComments().stream().map(ArticleCommentDto.Response::of).toList());
        return "management/article-comments";
    }

    @ResponseBody
    @GetMapping("/{articleCommentId}")
    public ArticleCommentDto.Response articleComment(@PathVariable Long articleCommentId) {
        return ArticleCommentDto.Response.of(articleCommentManagementService.getArticleComment(articleCommentId));
    }

    @PostMapping("/{articleCommentId}")
    public String deleteArticleComment(@PathVariable Long articleCommentId) {
        articleCommentManagementService.deleteArticleComment(articleCommentId);

        return "redirect:/management/article-comments";
    }
}
