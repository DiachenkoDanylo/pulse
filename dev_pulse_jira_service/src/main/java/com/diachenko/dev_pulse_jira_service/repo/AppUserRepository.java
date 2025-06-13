package com.diachenko.dev_pulse_jira_service.repo;
/*  Dev_Pulse
    06.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}
