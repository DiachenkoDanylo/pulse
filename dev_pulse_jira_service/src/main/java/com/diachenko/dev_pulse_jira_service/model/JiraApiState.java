package com.diachenko.dev_pulse_jira_service.model;
/*  Dev_Pulse
    16.05.2025
    @author DiachenkoDanylo
*/

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "jira_api_state")
public class JiraApiState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    private Long projectId;
}
