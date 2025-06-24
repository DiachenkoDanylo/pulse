package com.diachenko.dev_pulse_github_service.service;
/*  Dev_Pulse
    23.06.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_github_service.dto.github.GitHubTokenResponse;
import com.diachenko.dev_pulse_github_service.model.GitAccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final GitApiService gitApiService;
    @Value("${github.api.redirect}")
    private String redirectUrl;

    @Value("${github.api.secret}")
    private String secret;

    @Value("${github.api.clientId}")
    private String clientId;

    public GitAccessToken exchangeCodeToToken(String code) {

        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("grant_type", "authorization_code");
        requestBody.put("code", code);
        requestBody.put("redirect_uri", redirectUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<GitHubTokenResponse> response = gitApiService.makeRequestToken("code",requestBody,headers,GitHubTokenResponse.class);
        System.out.println("RESPONSE \n" + response);
        if (response.getStatusCode().is2xxSuccessful()) {
            GitAccessToken token = new GitAccessToken();
            token.setValue(response.getBody().getAccess_token());
//            token.setExpiresAt();
            return token;
        }
//        System.out.println(response.toString());
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            JiraAccessToken accessToken = new JiraAccessToken();
//            accessToken.setValue(response.getBody().getAccess_token());
//            accessToken.setExpiresAt(LocalDateTime.now().plusSeconds(response.getBody().getExpires_in()));
//            accessToken = accessTokenRepository.save(accessToken);
//            project.setAccessToken(accessToken);
//            JiraServer saved = generalProjectService.save(project);
//            JiraRefreshToken jiraRefreshToken = new JiraRefreshToken();
//            jiraRefreshToken.setValue(response.getBody().getRefresh_token());
//            jiraRefreshToken.setAccessToken(saved.getAccessToken());
//            jiraRefreshTokenRepository.save(jiraRefreshToken);
//            return saved.getAccessToken();
//        } else {
//            throw new RuntimeException("❌ Failed to exchange code for token: " + response.getStatusCode());
//        }
//    }

        return null;
    }
}
