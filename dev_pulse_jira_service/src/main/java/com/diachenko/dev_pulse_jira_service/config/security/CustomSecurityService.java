package com.diachenko.dev_pulse_jira_service.config.security;

import com.diachenko.dev_pulse_jira_service.model.AppUser;
import com.diachenko.dev_pulse_jira_service.model.JiraProject;
import com.diachenko.dev_pulse_jira_service.service.AppUserService;
import com.diachenko.dev_pulse_jira_service.service.JiraServerService;
import com.diachenko.dev_pulse_jira_service.service.JiraProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/*  Dev_Pulse
    02.06.2025
    @author DiachenkoDanylo
*/

@Service("customSecurityService")
@RequiredArgsConstructor
public class CustomSecurityService {

    private final AppUserService appUserService;
    private final JiraProjectService jiraProjectService;

    public boolean isOwnerOfJiraProject(Long generalProjectId, String jiraProjectKey) {
        System.out.println(generalProjectId);
        System.out.println(jiraProjectKey);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = appUserService.findByEmail(username);
        JiraProject jiraProject = jiraProjectService.findByKeyAndJiraServerId(jiraProjectKey, generalProjectId);

        if (user != null && jiraProject.getJiraServer().getUser() == user) {
            return true;
        }
        return false;
    }

}
