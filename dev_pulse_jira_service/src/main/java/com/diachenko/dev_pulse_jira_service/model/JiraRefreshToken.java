package com.diachenko.dev_pulse_jira_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

/*  Dev_Pulse
    16.05.2025
    @author DiachenkoDanylo
*/
@Data
@ToString
@Entity
@Table(name = "jira_refresh_token")
public class JiraRefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4096)
    private String value;

    @OneToOne
    private JiraAccessToken accessToken;
}
