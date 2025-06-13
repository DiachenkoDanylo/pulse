package com.diachenko.dev_pulse_jira_service.controller;
/*  Dev_Pulse
    06.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.dto.AppUserDto;
import com.diachenko.dev_pulse_jira_service.repo.AppUserRepository;
import com.diachenko.dev_pulse_jira_service.repo.JiraServerRepository;
import com.diachenko.dev_pulse_jira_service.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final AppUserService appUserService;

    @GetMapping("")
    public ResponseEntity<AppUserDto> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(appUserService.findByEmailDto(username));
    }
}
