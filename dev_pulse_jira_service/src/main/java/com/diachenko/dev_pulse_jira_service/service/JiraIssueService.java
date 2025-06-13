package com.diachenko.dev_pulse_jira_service.service;
/*  Dev_Pulse
    15.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.model.JiraIssue;
import com.diachenko.dev_pulse_jira_service.model.JiraProject;
import com.diachenko.dev_pulse_jira_service.repo.JiraIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JiraIssueService {

    private final JiraIssueRepository issueRepository;
    private final JiraProjectService jiraProjectService;

    public List<JiraIssue> getIssueListByKeyAndJiraServerId(String key, Long projectId) {
        JiraProject jiraProject = jiraProjectService.findByKeyAndJiraServerId(key, projectId);
        return issueRepository.findAllByJiraProjectId(jiraProject.getId());
    }

    public List<JiraIssue> getTaskListByJiraServerIdAndProjectKey(String key, Long jiraServerId) {
        JiraProject jiraProject = jiraProjectService.findByKeyAndJiraServerId(key, jiraServerId);
        return issueRepository.findAllByTypeAndJiraProjectId("Task",jiraProject.getId());}

    public Optional<JiraIssue> getOptionalIssueByKeyAndJiraProjectId(String key, Long id) {
        return issueRepository.findByKeyAndJiraProjectId(key, id);
    }
}
