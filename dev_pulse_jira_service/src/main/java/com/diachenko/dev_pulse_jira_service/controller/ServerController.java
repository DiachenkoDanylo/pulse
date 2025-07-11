package com.diachenko.dev_pulse_jira_service.controller;
/*  Dev_Pulse
    08.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.dto.JiraServerDto;
import com.diachenko.dev_pulse_jira_service.dto.ProjectCreationDto;
import com.diachenko.dev_pulse_jira_service.model.AppUser;
import com.diachenko.dev_pulse_jira_service.model.JiraServer;
import com.diachenko.dev_pulse_jira_service.service.AppUserService;
import com.diachenko.dev_pulse_jira_service.service.JiraProjectService;
import com.diachenko.dev_pulse_jira_service.service.JiraServerService;
import com.diachenko.dev_pulse_jira_service.service.JiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/project")
@RestController
@RequiredArgsConstructor
public class ServerController {

    private final JiraServerService jiraServerService;
    private final JiraService jiraApiService;
    private final AppUserService appUserService;
    private final JiraProjectService jiraProjectService;
    private final JiraService jiraService;

    @GetMapping("/")
    public ResponseEntity<?> allJiraServer(Authentication principal,
                                           @RequestParam(name = "update", required = false, defaultValue = "false") Boolean update) {
        AppUser appUser = appUserService.findByEmail(principal.getName());
        Long start = LocalDateTime.now().toLocalTime().toNanoOfDay();
        if (update != null && update.equals(Boolean.TRUE)) {
            List<JiraServerDto> server = jiraServerService.findByUserId(appUser.getId());
            for (JiraServerDto dto : server) {
                jiraService.fetchAndSaveJiraUsers(dto.getId());
                jiraService.fetchAndSaveJiraProjects(dto.getId());
            }
            Map<String, String> response = new HashMap<>();
            response.put("message", "Updated successfully");
            return ResponseEntity.ok(response);
        }
        Long end = LocalDateTime.now().toLocalTime().toNanoOfDay();
        long time = end - start;
        System.out.println("Controller time method in milsec : " + time / 1_000);
        return ResponseEntity.ok(jiraServerService.findByUserId(appUser.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> generalProject(@PathVariable(name = "id") String id) {
        jiraApiService.fetchAndSaveJiraProjects(Long.valueOf(id));
        return ResponseEntity.ok(jiraProjectService.findByGeneralProjectIdDto(Long.valueOf(id)));
    }

    @GetMapping("/{id}/sync")
    public String generalProjectUpdate(@PathVariable(name = "id") String id) {
        jiraApiService.fetchAndSaveJiraProjects(Long.valueOf(id));
        return "redirect:/project/" + id;
    }

    @PostMapping("/add-project")
    public ResponseEntity<?> addProject(@RequestBody ProjectCreationDto projectCreationDto, Authentication authentication) {
        projectCreationDto.setUserId(appUserService.findByEmail(authentication.getName()).getId());
        JiraServer addedProject = jiraServerService.handleSaveNewProject(projectCreationDto);
        String url = jiraServerService.buildAuthorizationLink(addedProject.getId());
        if (url != null) {
            return ResponseEntity.ok(Map.of("redirect", url));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Unable to generate URL"));
        }
    }

}
