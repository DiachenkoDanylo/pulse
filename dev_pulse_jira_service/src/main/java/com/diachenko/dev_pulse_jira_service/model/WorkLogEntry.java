package com.diachenko.dev_pulse_jira_service.model;
/*  Dev_Pulse
    14.05.2025
    @author DiachenkoDanylo
*/

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "work_log_entry")
public class WorkLogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jiraWorklogId;
    private Long issueId;
    private Long userId;

    private Long timeSpentSeconds;

    private Instant startedAt;
    private Instant createdAt;
}
