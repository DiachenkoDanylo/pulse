package com.diachenko.dev_pulse_jira_service.repo;

import com.diachenko.dev_pulse_jira_service.model.JiraRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*  Dev_Pulse
    16.05.2025
    @author DiachenkoDanylo
*/
@Repository
public interface JiraRefreshTokenRepository extends JpaRepository<JiraRefreshToken, Long> {
    Optional<JiraRefreshToken> findByAccessTokenId(Long id);
}
