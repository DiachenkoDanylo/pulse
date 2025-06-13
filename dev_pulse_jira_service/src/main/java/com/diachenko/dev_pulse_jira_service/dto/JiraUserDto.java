package com.diachenko.dev_pulse_jira_service.dto;
/*  Dev_Pulse
    09.06.2025
    @author DiachenkoDanylo
*/

import lombok.Data;

@Data
public class JiraUserDto {
    private String accountId;
    private String emailAddress;
    private String displayName;
    private boolean active;
    private String timeZone;
    private String accountType;
}

