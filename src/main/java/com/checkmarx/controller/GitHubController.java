package com.checkmarx.controller;

import com.checkmarx.dto.OrganizationDto;
import com.checkmarx.dto.RepositoryDto;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/github")
public class GitHubController {

    @Value("${url.pattern.get.user.organizations}")
    private String urlPatternGetUserOrganizations;

    @Value("${url.pattern.get.user.repositories}")
    private String urlPatternGetUserRepositories;

    @Value("${url.pattern.get.org.repositories}")
    private String urlPatternGetOrgRepositories;

    @Value("${github.token.format}")
    private String githubTokenPattern;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping(value="/user/repos")
    public ResponseEntity<List<RepositoryDto>> getUserRepositories(
            @RequestHeader("UserAuthToken") String userAuthToken) {
        List<RepositoryDto> userRepositoryDtos = new ArrayList<>();

        final HttpEntity<String> request = createRequest(null, createHeaders(userAuthToken));
        ResponseEntity response = sendRequest(urlPatternGetUserRepositories, HttpMethod.GET, request, String.class);

        log.info(response.toString());
        return new ResponseEntity<>(userRepositoryDtos, HttpStatus.OK);

    }

    @GetMapping(value="/user/orgs")
    public ResponseEntity<List<OrganizationDto>> getUserOrganizations(
            @RequestHeader("UserAuthToken") String userAuthToken) {
        List<OrganizationDto> userOrganizationDtos = new ArrayList<>();

        final HttpEntity<String> request = createRequest(null, createHeaders(userAuthToken));
        ResponseEntity response = sendRequest(urlPatternGetUserOrganizations, HttpMethod.GET, request, String.class);

        log.info(response.toString());
        return new ResponseEntity<>(userOrganizationDtos, HttpStatus.OK);

    }


    @GetMapping(value="org/repos")
    public ResponseEntity<List<RepositoryDto>> getOrganizationRepositories(
            @RequestHeader("UserAuthToken") String userAuthToken, @RequestHeader("Org") String orgName) {
        List<RepositoryDto> orgRepositoryDtos = new ArrayList<>();

        final HttpEntity<String> request = createRequest(null, createHeaders(userAuthToken));
        String path = String.format(urlPatternGetOrgRepositories, orgName);
        ResponseEntity response = sendRequest(path, HttpMethod.GET, request, String.class);
        log.info(response.toString());
        return new ResponseEntity<>(orgRepositoryDtos, HttpStatus.OK);


    }

    private HttpHeaders createHeaders(String userAuthToken) {
        final HttpHeaders headers = new HttpHeaders();
        String tokenHeader = String.format(githubTokenPattern, userAuthToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", tokenHeader);
        return headers;
    }

    private ResponseEntity sendRequest(String path, HttpMethod method, HttpEntity<String> request, Class responseType) {
        return restTemplate.exchange(path, method, request, responseType);
    }

    private HttpEntity<String> createRequest(Object body, HttpHeaders headers) {
        return new HttpEntity<>((String) body, headers);
    }


}