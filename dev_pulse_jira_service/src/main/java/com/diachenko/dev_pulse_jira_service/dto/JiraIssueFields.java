package com.diachenko.dev_pulse_jira_service.dto;
/*  Dev_Pulse
    14.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.dto.jiraResponse.JiraDtoUserResponse;
import com.diachenko.dev_pulse_jira_service.service.CustomInstantDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.Instant;

@Data
public class JiraIssueFields {

    private JiraDtoUserResponse assignee;
    private JiraDtoUserResponse creator;
    private String summary;
    private JiraIssueType issuetype;
    private JiraIssueStatus status;
    @JsonDeserialize(using = CustomInstantDeserializer.class)
    private Instant created;
    @JsonDeserialize(using = CustomInstantDeserializer.class)
    private Instant updated;
    @JsonDeserialize(using = CustomInstantDeserializer.class)
    private Instant resolutiondate;
    @JsonProperty("customfield_10026")
    private Integer storyPoints;
}
