package com.diachenko.dev_pulse_jira_service.repo;
/*  Dev_Pulse
    14.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.model.JiraIssue;
import com.diachenko.dev_pulse_jira_service.model.JiraProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface JiraIssueRepository extends JpaRepository<JiraIssue, Long> {
    List<JiraIssue> findAllByTypeAndJiraProjectId(String type, Long projectId);
    Optional<JiraIssue> findByJiraIssueId(String jiraIssueId);
    List<JiraIssue> findAllByJiraProjectId(Long id);
    Optional<JiraIssue> findByKeyAndJiraProjectId(String key, Long id);
    boolean existsByKeyAndJiraProject(String key, JiraProject jiraProject);
}
