package com.diachenko.dev_pulse_jira_service.controller;
/*  Dev_Pulse
    03.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.dto.jiraResponse.JiraCodeDto;
import com.diachenko.dev_pulse_jira_service.repo.JiraApiStateRepository;
import com.diachenko.dev_pulse_jira_service.service.AccessTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/oauth/jira")
@RequiredArgsConstructor
public class AuthJiraController {

    private final AccessTokenService service;
    private final JiraApiStateRepository jiraApiStateRepository;
//
//    @GetMapping("/codes")
//    public String handleAuthorizationCode(@RequestParam(name = "state", required = false) String state,
//                                          @RequestParam(name = "code", required = false) String authCode) {
//        service.exchangeCodeToToken(jiraApiStateRepository.findByValue(state).get().getProjectId(), authCode);
//        return "redirect:/project/";
//    }

    @PostMapping("/code")
    public ResponseEntity<?> handleAuthorizationCode(@RequestBody JiraCodeDto dto) {
        String state = dto.getState();
        String authCode = dto.getCode();
        System.out.println("INSIDE CONTROLLER");
        service.exchangeCodeToToken(
                jiraApiStateRepository.findByValue(state).get().getProjectId(),
                authCode
        );


        return ResponseEntity.ok().build();
    }
}
