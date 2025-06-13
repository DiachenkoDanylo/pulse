package com.diachenko.dev_pulse_jira_service.repo;
/*  Dev_Pulse
    06.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.model.JiraServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JiraServerRepository extends JpaRepository<JiraServer, Long> {
    Optional<JiraServer> findById(Long id);
    List<JiraServer> findAllByUserId(Long id);
    Optional<JiraServer> findGeneralProjectByProjectUrl(String url);
}
