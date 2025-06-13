package com.diachenko.dev_pulse_jira_service.dto.jiraResponse;
/*  Dev_Pulse
    15.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.dto.JiraIssueDto;
import lombok.Data;

import java.util.List;

@Data
public class JiraIssuesSearchResponse {

    private List<JiraIssueDto> issues;
}
