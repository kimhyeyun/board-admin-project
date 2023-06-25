package dev.be.boardadminproject.service;

import dev.be.boardadminproject.dto.ArticleCommentDto;
import dev.be.boardadminproject.dto.properties.ProjectProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleCommentManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties properties;


    public List<ArticleCommentDto.Dto> getArticleComments() {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articleComments")
                .queryParam("size", 10000)
                .build()
                .toUri();

        ArticleCommentDto.ClientResponse response = restTemplate.getForObject(uri, ArticleCommentDto.ClientResponse.class);

        return Optional.ofNullable(response).orElseGet(ArticleCommentDto.ClientResponse::empty).articleComments();
    }

    public ArticleCommentDto.Dto getArticleComment(Long articleCommentId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articleComments/" + articleCommentId)
                .queryParam("projection", "withMember")
                .build()
                .toUri();

        ArticleCommentDto.Dto response = restTemplate.getForObject(uri, ArticleCommentDto.Dto.class);

        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("댓글이 없습니다 - articleCommentId : " + articleCommentId));
    }

    public void deleteArticleComment(Long articleCommentId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articleComments/" + articleCommentId)
                .build()
                .toUri();

        restTemplate.delete(uri);
    }
}
