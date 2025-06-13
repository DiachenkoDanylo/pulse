package com.diachenko.dev_pulse_jira_service.repo;
/*  Dev_Pulse
    14.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.model.JiraProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface JiraProjectRepository extends JpaRepository<JiraProject, Long> {
    Optional<JiraProject> findByJiraProjectId(String jiraProjectId);
    List<JiraProject> findAllByJiraServerId(Long id);
    Optional<JiraProject> findByJiraServerIdAndKey(Long projectId, String key);
    Optional<JiraProject> findByJiraProjectIdAndJiraServer_Id(String jiraProjectId, Long id);
}
