package com.diachenko.dev_pulse_jira_service.repo;
/*  Dev_Pulse
    08.06.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.model.JiraUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JiraUserRepository extends JpaRepository<JiraUser, Long> {
    Optional<JiraUser> findById(Long id);
    Optional<JiraUser> findByAccountId(String accountId);
}
