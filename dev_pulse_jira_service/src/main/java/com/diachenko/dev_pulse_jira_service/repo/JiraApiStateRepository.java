package com.diachenko.dev_pulse_jira_service.repo;
/*  Dev_Pulse
    16.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.model.JiraApiState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JiraApiStateRepository extends JpaRepository<JiraApiState, Long> {
    Optional<JiraApiState> findByValue(String value);
}
