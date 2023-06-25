package dev.be.boardadminproject.service;

import dev.be.boardadminproject.dto.ArticleDto;
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
public class ArticleManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties properties;


    public List<ArticleDto.Dto> getArticles() {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articles")
                .queryParam("size", 10000)
                .build()
                .toUri();

        ArticleDto.ClientResponse response = restTemplate.getForObject(uri, ArticleDto.ClientResponse.class);

        return Optional.ofNullable(response).orElseGet(ArticleDto.ClientResponse::empty).articles();
    }

    public ArticleDto.Dto getArticle(Long articleId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articles/" + articleId)
                .queryParam("projection", "withMember")
                .build()
                .toUri();

        ArticleDto.Dto response = restTemplate.getForObject(uri, ArticleDto.Dto.class);

        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("게시글이 없습니다 - articleId : " + articleId));
    }

    public void deleteArticle(Long articleId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/articles/" + articleId)
                .build()
                .toUri();

        restTemplate.delete(uri);
    }
}
