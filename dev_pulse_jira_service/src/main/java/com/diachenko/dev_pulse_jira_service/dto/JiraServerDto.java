package com.diachenko.dev_pulse_jira_service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*  Dev_Pulse
    15.05.2025
    @author DiachenkoDanylo
*/
@Data
public class JiraServerDto {
    private Long id;
    private String name;
    private String projectUrl;
    private List<JiraProjectDto> jiraProjectListDto = new ArrayList<>();

}
