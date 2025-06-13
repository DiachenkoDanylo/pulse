package com.diachenko.dev_pulse_jira_service.dto;

import lombok.Data;

/*  Dev_Pulse
    08.05.2025
    @author DiachenkoDanylo
*/
@Data
public class ProjectCreationDto {
    private Long userId;
    private String name;
    private String projectUrl;
}
