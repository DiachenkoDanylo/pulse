package com.diachenko.dev_pulse_jira_service.dto;
/*  Dev_Pulse
    06.05.2025
    @author DiachenkoDanylo
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JiraTokenResponse {

    private String access_token;
    private String refresh_token;
    private int expires_in;
    private String scope;
}
