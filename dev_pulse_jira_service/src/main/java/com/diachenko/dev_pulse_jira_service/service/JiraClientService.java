package com.diachenko.dev_pulse_jira_service.service;
/*  Dev_Pulse
    16.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.model.JiraAccessToken;
import com.diachenko.dev_pulse_jira_service.model.JiraServer;
import com.diachenko.dev_pulse_jira_service.model.JiraRefreshToken;
import com.diachenko.dev_pulse_jira_service.dto.JiraTokenResponse;
import com.diachenko.dev_pulse_jira_service.repo.AccessTokenRepository;
import com.diachenko.dev_pulse_jira_service.repo.JiraRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JiraClientService {

    private static final String TOKEN_URL = "https://auth.atlassian.com/oauth/token";
    private final RestTemplate restTemplate;
    private final JiraRefreshTokenRepository jiraRefreshTokenRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final JiraServerService jiraServerService;

    @Value("${jira.api.redirect}")
    private String redirectUrl;
    @Value("${jira.api.secret}")
    private String secret;
    @Value("${jira.api.clientId}")
    private String clientId;

    public <T> ResponseEntity<T> makeAuthorizedRequest(
            JiraServer project,
            String url,
            HttpMethod method,
            Map<String, String> bodyValues,
            HttpHeaders headers,
            Class<T> responseType
    ) {
        try {
            HttpEntity<Map<String, String>> http = new HttpEntity<>(bodyValues, headers);
            return restTemplate.exchange(url, method, withAuthHeader(project, http), responseType);
        } catch (HttpClientErrorException.Unauthorized ex) {
            if (isAccessTokenExpired(ex)) {
                refreshTokenAndSave(project);

                JiraServer updatedProject = jiraServerService.findById(project.getId());
                HttpEntity<Map<String, String>> retryHttp = new HttpEntity<>(bodyValues, headers);
                return restTemplate.exchange(url, method, withAuthHeader(updatedProject, retryHttp), responseType);
            }
            throw ex;
        }
    }

    private void refreshTokenAndSave(JiraServer project) {
        JiraRefreshToken refreshToken = jiraRefreshTokenRepository
                .findByAccessTokenId(project.getAccessToken().getId())
                .orElseThrow(() -> new RuntimeException("❌ Refresh token not found"));

        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "refresh_token");
        body.put("refresh_token", refreshToken.getValue());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<JiraTokenResponse> response = makeRequestToken("refresh", body, headers, JiraTokenResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JiraTokenResponse tokenResponse = response.getBody();

            JiraAccessToken accessToken = project.getAccessToken();
            accessToken.setValue(tokenResponse.getAccess_token());
            accessToken.setExpiresAt(LocalDateTime.now().plusSeconds(tokenResponse.getExpires_in()));
            accessToken = accessTokenRepository.save(accessToken);

            project.setAccessToken(accessToken);
            JiraServer updatedProject = jiraServerService.save(project);

            refreshToken.setValue(tokenResponse.getRefresh_token());
            refreshToken.setAccessToken(updatedProject.getAccessToken());
            jiraRefreshTokenRepository.save(refreshToken);

        } else {
            throw new RuntimeException("❌ Failed to refresh token: " + response.getStatusCode());
        }
    }

    public <T> ResponseEntity<T> makeRequestToken(
            String method,
            Map<String, String> bodyValues,
            HttpHeaders headers,
            Class<T> responseType
    ) {
        if (!method.equals("code") && !method.equals("refresh")) {
            throw new RuntimeException("Invalid method: must be 'code' or 'refresh'");
        }

        HttpEntity<Map<String, String>> http = new HttpEntity<>(bodyValues, headers);
        HttpEntity<Map<String, String>> httpWithCreds = withClientCredentials(http);
        return restTemplate.exchange(TOKEN_URL, HttpMethod.POST, httpWithCreds, responseType);
    }

    private <T> HttpEntity<T> withAuthHeader(JiraServer project, HttpEntity<T> original) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(original.getHeaders());
        headers.setBearerAuth(project.getAccessToken().getValue());
        return new HttpEntity<>(original.getBody(), headers);
    }

    private <T> HttpEntity<T> withClientCredentials(HttpEntity<T> original) {
        HttpHeaders headers = new HttpHeaders(original.getHeaders());

        T body = original.getBody();

        if (body instanceof Map<?, ?> originalMap) {
            @SuppressWarnings("unchecked")
            Map<String, String> newBody = new HashMap<>((Map<String, String>) originalMap);
            newBody.put("client_id", clientId);
            newBody.put("client_secret", secret);
            return new HttpEntity<>((T) newBody, headers);
        }

        throw new IllegalArgumentException("Expected body to be a Map<String, String>, but got: " + body.getClass());
    }

    private boolean isAccessTokenExpired(HttpClientErrorException ex) {
        return ex.getResponseBodyAsString().contains("access token expired")
                || ex.getStatusCode() == HttpStatus.UNAUTHORIZED;
    }
}

