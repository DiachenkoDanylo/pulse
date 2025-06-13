package com.diachenko.dev_pulse_jira_service.model;
/*  Dev_Pulse
    14.05.2025
    @author DiachenkoDanylo
*/

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "jira_user")
@Data
public class JiraUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountId;
    private String displayName;
    private String emailAddress;
    private String avatarUrl;

    @Transient
    private String accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jira_server_id")
    private JiraServer jiraServer;
}
