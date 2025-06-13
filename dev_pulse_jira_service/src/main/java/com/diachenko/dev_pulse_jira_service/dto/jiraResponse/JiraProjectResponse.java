package com.diachenko.dev_pulse_jira_service.dto.jiraResponse;
/*  Dev_Pulse
    15.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.dto.JiraProjectDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraProjectResponse {
    private List<JiraProjectDto> values;
}
