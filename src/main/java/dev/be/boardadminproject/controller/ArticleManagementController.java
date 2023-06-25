package dev.be.boardadminproject.controller;

import dev.be.boardadminproject.dto.ArticleDto;
import dev.be.boardadminproject.service.ArticleManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/management/articles")
@Controller
@RequiredArgsConstructor
public class ArticleManagementController {

    private final ArticleManagementService articleManagementService;

    @GetMapping
    public String articles(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            HttpServletRequest request
    ) {
        model.addAttribute("request", request.getRequestURI());
        model.addAttribute("articles", articleManagementService.getArticles().stream().map(ArticleDto.Response::withoutContent).toList());

        return "management/articles";
    }

    @ResponseBody
    @GetMapping("/{articleId}")
    public ArticleDto.Response article(@PathVariable Long articleId) {
        return ArticleDto.Response.withContent(articleManagementService.getArticle(articleId));
    }

    @PostMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleManagementService.deleteArticle(articleId);

        return "redirect:/management/articles";
    }
}
