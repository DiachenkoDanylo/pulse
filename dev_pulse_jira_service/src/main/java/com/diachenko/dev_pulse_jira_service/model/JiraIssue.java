package com.diachenko.dev_pulse_jira_service.model;
/*  Dev_Pulse
    14.05.2025
    @author DiachenkoDanylo
*/

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;


@Entity
@Data
@Table(name = "jira_issue")
public class JiraIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jiraIssueId;
    private String key;
    private String summary;
    private String status;
    private String type;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jira_project_id")
    @JsonBackReference
    private JiraProject jiraProject;

    private Long assigneeId;
    private Long creatorId;
    private Long reporterId;

    private Long timeSpentSeconds;
    private Integer storyPoints;

    @Override
    public String toString() {
        return "JiraIssue{" +
                "id=" + id +
                ", jiraIssueId='" + jiraIssueId + '\'' +
                ", key='" + key + '\'' +
                ", summary='" + summary + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", resolvedAt=" + resolvedAt +
                ", assigneeId=" + assigneeId +
                ", creatorId=" + creatorId +
                ", reporterId=" + reporterId +
                ", timeSpentSeconds=" + timeSpentSeconds +
                ", storyPoints=" + storyPoints +
                '}';
    }
}
