package com.diachenko.dev_pulse_github_service.controller;
/*  Dev_Pulse
    12.05.2025
    @author DiachenkoDanylo
*/

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/")
public class OAuthGitHubController {

    @GetMapping("github")
    public ResponseEntity<String> handleAuthorizationCode(@RequestParam(name = "code") String code) {
        System.out.println(code);
        return null;
    }
}
