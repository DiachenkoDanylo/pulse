package com.diachenko.dev_pulse_jira_service.dto.jiraResponse;
/*  Dev_Pulse
    07.06.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.dto.JiraUserDto;
import lombok.Data;

import java.util.List;

@Data
public class JiraUserResponse {
    private List<JiraUserDto> users;
}
