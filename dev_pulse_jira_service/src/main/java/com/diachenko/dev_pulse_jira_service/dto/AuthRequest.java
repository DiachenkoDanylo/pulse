package com.diachenko.dev_pulse_jira_service.dto;
/*  Dev_Pulse
    14.05.2025
    @author DiachenkoDanylo
*/

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
