package dev.be.boardadminproject.service;

import dev.be.boardadminproject.dto.MemberDto;
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
public class MemberManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties properties;


    public List<MemberDto.Dto> getMembers() {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/members")
                .queryParam("size", 10000)
                .build()
                .toUri();

        MemberDto.ClientResponse response = restTemplate.getForObject(uri, MemberDto.ClientResponse.class);

        return Optional.ofNullable(response).orElseGet(MemberDto.ClientResponse::empty).members();
    }

    public MemberDto.Dto getMember(String memberId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/members/" + memberId)
                .build()
                .toUri();

        MemberDto.Dto response = restTemplate.getForObject(uri, MemberDto.Dto.class);

        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("게시글이 없습니다 - memberId : " + memberId));
    }

    public void deleteMember(String memberId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(properties.board().url() + "/api/members/" + memberId)
                .build()
                .toUri();

        restTemplate.delete(uri);
    }
}
