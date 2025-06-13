package com.diachenko.dev_pulse_jira_service.service;

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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/*  Dev_Pulse
    16.05.2025
    @author DiachenkoDanylo
*/
@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final JiraServerService generalProjectService;
    private final JiraRefreshTokenRepository jiraRefreshTokenRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final JiraServerService projectService;
    private final JiraClientService jiraClientService;

    @Value("${jira.api.redirect}")
    private String redirectUrl;

    @Value("${jira.api.secret}")
    private String secret;

    @Value("${jira.api.clientId}")
    private String clientId;

    public JiraAccessToken exchangeCodeToToken(Long projectId, String code) {

        JiraServer project = projectService.findById(projectId);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "authorization_code");
        requestBody.put("code", code);
        requestBody.put("redirect_uri", redirectUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<JiraTokenResponse> response = jiraClientService.makeRequestToken("code",requestBody,headers,JiraTokenResponse.class);
        System.out.println(response.toString());

        if (response.getStatusCode().is2xxSuccessful()) {
            JiraAccessToken accessToken = new JiraAccessToken();
            accessToken.setValue(response.getBody().getAccess_token());
            accessToken.setExpiresAt(LocalDateTime.now().plusSeconds(response.getBody().getExpires_in()));
            accessToken = accessTokenRepository.save(accessToken);
            project.setAccessToken(accessToken);
            JiraServer saved = generalProjectService.save(project);
            JiraRefreshToken jiraRefreshToken = new JiraRefreshToken();
            jiraRefreshToken.setValue(response.getBody().getRefresh_token());
            jiraRefreshToken.setAccessToken(saved.getAccessToken());
            jiraRefreshTokenRepository.save(jiraRefreshToken);
            return saved.getAccessToken();
        } else {
            throw new RuntimeException("❌ Failed to exchange code for token: " + response.getStatusCode());
        }
    }


    public JiraAccessToken save(JiraAccessToken accessToken) {
        return accessTokenRepository.save(accessToken);
    }
}
