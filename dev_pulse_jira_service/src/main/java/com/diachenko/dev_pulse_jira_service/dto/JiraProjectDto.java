package com.diachenko.dev_pulse_jira_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/*  Dev_Pulse
    15.05.2025
    @author DiachenkoDanylo
*/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraProjectDto {

    private String  id;
    private String name;
    private String key;
    private Long generalProjectId;
}
