package com.diachenko.dev_pulse_jira_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*  Dev_Pulse
    27.05.2025
    @author DiachenkoDanylo
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppTokenResponse {

    private String access_token;
    private String refresh_token;
}
