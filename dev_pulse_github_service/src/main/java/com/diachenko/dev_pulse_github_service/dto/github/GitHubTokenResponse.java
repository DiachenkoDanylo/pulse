package com.diachenko.dev_pulse_github_service.dto.github;
/*  Dev_Pulse
    23.06.2025
    @author DiachenkoDanylo
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubTokenResponse {

    private String access_token;
    private String token_type;
    private String scope;
}
