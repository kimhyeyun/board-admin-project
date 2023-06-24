package dev.be.boardadminproject.controller;

import dev.be.boardadminproject.service.ArticleManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("articles", articleManagementService.getArticles());

        return "management/articles";
    }
}
