package com.diachenko.dev_pulse_github_service.controller;
/*  Dev_Pulse
    12.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_github_service.dto.github.GitHubCodeDto;
import com.diachenko.dev_pulse_github_service.service.AccessTokenService;
import com.diachenko.dev_pulse_github_service.service.GitApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth/github/")
public class AuthGitHubController {

    private final GitApiService gitApiService;
    private final AccessTokenService accessTokenService;

    public AuthGitHubController(GitApiService gitApiService, AccessTokenService accessTokenService) {
        this.gitApiService = gitApiService;
        this.accessTokenService = accessTokenService;
    }

    @GetMapping("")
    public ResponseEntity<String> handleAuthorizationCode(@RequestParam(name = "code") String code) {
        System.out.println(code);
        return null;
    }

    @PostMapping("code")
    public ResponseEntity<?> handleAuthorizationCode(@RequestBody GitHubCodeDto dto) {
//        String state = dto.getState();
        String authCode = dto.getCode();
        System.out.println(authCode);
        accessTokenService.exchangeCodeToToken(authCode);
        return ResponseEntity.ok().build();
    }
}
