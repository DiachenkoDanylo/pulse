package com.diachenko.dev_pulse_jira_service.model;
/*  Dev_Pulse
    06.05.2025
    @author DiachenkoDanylo
*/

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@Entity
@Table(name = "access_token")
public class JiraAccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4096)
    private String value;

    private LocalDateTime expiresAt;
}
