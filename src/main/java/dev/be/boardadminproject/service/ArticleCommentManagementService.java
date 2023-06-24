package dev.be.boardadminproject.service;

import dev.be.boardadminproject.dto.ArticleCommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleCommentManagementService {

    public List<ArticleCommentDto.Dto> getArticleComments() {
        return List.of();
    }

    public ArticleCommentDto.Dto getArticleComment(Long articleCommentId) {
        return null;
    }

    public void deleteArticleComment(Long articleCommentId) {

    }
}
