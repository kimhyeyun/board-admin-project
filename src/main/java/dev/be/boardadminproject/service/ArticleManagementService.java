package dev.be.boardadminproject.service;

import dev.be.boardadminproject.dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleManagementService {

    public List<ArticleDto.Dto> getArticles() {
        return List.of();
    }

    public ArticleDto.Dto getArticle(Long articleId) {
        return null;
    }

    public void deleteArticle(Long articleId) {

    }
}
