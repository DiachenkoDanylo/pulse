package com.diachenko.dev_pulse_github_service.controller;
/*  Dev_Pulse
    20.06.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_github_service.service.GitApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final GitApiService gitApiService;

    @GetMapping("")
    public ResponseEntity<?> testLink() {
        return ResponseEntity.ok(Map.of("redirect", gitApiService.buildAuthorizationLink("any")));
    }

}
