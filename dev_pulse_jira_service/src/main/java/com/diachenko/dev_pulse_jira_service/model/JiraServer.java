package com.diachenko.dev_pulse_jira_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*  Dev_Pulse
    06.05.2025
    @author DiachenkoDanylo
*/
@Data
@Entity
@Table(name = "jira_server")
public class JiraServer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_token_id")
    private JiraAccessToken accessToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private AppUser user;
    private String name;
    @Column(length = 1000)
    private String projectUrl;
    private String projectCloudId;

    @OneToMany(mappedBy = "jiraServer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JiraProject> jiraProjectList = new ArrayList<>();
}
