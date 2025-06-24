package com.diachenko.dev_pulse_jira_service.controller;

import com.diachenko.dev_pulse_jira_service.service.JiraIssueService;
import com.diachenko.dev_pulse_jira_service.service.JiraProjectService;
import com.diachenko.dev_pulse_jira_service.service.JiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/*  Dev_Pulse
    15.05.2025
    @author DiachenkoDanylo
*/

@RequestMapping("/project-jira")
@RestController
@RequiredArgsConstructor
public class JiraProjectController {

    private final JiraService jiraApiService;
    private final JiraIssueService jiraIssueService;
    private final JiraProjectService jiraProjectService;

    @GetMapping("/{jiraServerId}/{key}")
    public ResponseEntity<?> getJiraProjectByKey(@P("jiraServerId") @PathVariable(name = "jiraServerId") Long jiraServerId,
                                                 @P("key") @PathVariable(name = "key") String key,
                                                 @P("update") @RequestParam(name = "update", required = false, defaultValue = "false") Boolean update) {
        if (update != null && update.equals(Boolean.TRUE)) {
            jiraApiService.fetchAndSaveJiraProjectIssues(jiraServerId, key);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Updated successfully");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(jiraProjectService.findByKeyAndJiraServerId(key, jiraServerId));
    }

    @GetMapping("/{jiraServerId}/{key}/issues")
    public ResponseEntity<?> getIssues(@PathVariable(name = "jiraServerId") Long jiraServerId,
                                       @PathVariable(name = "key") String key,
                                       @RequestParam(name = "id", required = false) String id) {
        if (id == null) {
            return ResponseEntity.ok(jiraIssueService.getIssueListByKeyAndJiraServerId(key, jiraServerId));
        }
        return null;
    }

    @GetMapping("/{jiraServerId}/{key}/issues/sync")
    public String fetchIssues(@PathVariable(name = "jiraServerId") Long jiraServerId,
                              @PathVariable(name = "key") String key) {
        jiraApiService.fetchAndSaveJiraProjectIssues(jiraServerId, key);
        return "redirect:/project-jira/" + jiraServerId + "/" + key + "/issues";
    }


}
