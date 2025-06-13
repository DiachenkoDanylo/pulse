package com.diachenko.dev_pulse_jira_service.model;
/*  Dev_Pulse
    14.05.2025
    @author DiachenkoDanylo
*/

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "jira_project",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"jira_project_id", "jira_server_id"})
        }
)
public class JiraProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jiraProjectId;
    private String name;
    private String key;
    private boolean isPrivate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jira_server_id")
    @JsonBackReference
    private JiraServer jiraServer;

    @OneToMany(mappedBy = "jiraProject", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JiraIssue> jiraIssueList;
}
