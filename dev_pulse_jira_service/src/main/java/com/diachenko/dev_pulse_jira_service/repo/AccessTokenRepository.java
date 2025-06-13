package com.diachenko.dev_pulse_jira_service.repo;
/*  Dev_Pulse
    06.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.model.JiraAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenRepository extends JpaRepository<JiraAccessToken, Long> {
}
