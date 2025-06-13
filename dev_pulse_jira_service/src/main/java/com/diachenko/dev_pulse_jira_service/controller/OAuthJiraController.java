package com.diachenko.dev_pulse_jira_service.controller;
/*  Dev_Pulse
    03.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.dto.JiraTokenResponse;
import com.diachenko.dev_pulse_jira_service.dto.jiraResponse.JiraCodeDto;
import com.diachenko.dev_pulse_jira_service.model.JiraAccessToken;
import com.diachenko.dev_pulse_jira_service.repo.JiraApiStateRepository;
import com.diachenko.dev_pulse_jira_service.service.AccessTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/oauth/jira")
@RequiredArgsConstructor
public class OAuthJiraController {

    private final AccessTokenService service;
    private final JiraApiStateRepository jiraApiStateRepository;

    @GetMapping("/codes")
    public String handleAuthorizationCode(@RequestParam(name = "state", required = false) String state,
                                          @RequestParam(name = "code", required = false) String authCode,
                                          @RequestBody(required = false) JiraTokenResponse tokenResponse) {
        if (tokenResponse != null) {
            System.out.println("Token =" + tokenResponse.getAccess_token());
        }
        System.out.println("state = " + state);

        JiraAccessToken token = service.exchangeCodeToToken(jiraApiStateRepository.findByValue(state).get().getProjectId(), authCode);
        System.out.println("TOKEN =   \n" + token);
        return "redirect:/project/";
    }

    @PostMapping("/code")
    public ResponseEntity<?> handleAuthorizationCode(@RequestBody JiraCodeDto dto) {
        String state = dto.getState();
        String authCode = dto.getCode();
        service.exchangeCodeToToken(
                jiraApiStateRepository.findByValue(state).get().getProjectId(),
                authCode
        );

        return ResponseEntity.ok().build();
    }

}
