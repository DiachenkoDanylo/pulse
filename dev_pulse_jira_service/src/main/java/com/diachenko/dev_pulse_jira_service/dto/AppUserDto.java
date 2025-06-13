package com.diachenko.dev_pulse_jira_service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*  Dev_Pulse
    15.05.2025
    @author DiachenkoDanylo
*/
@Data
public class AppUserDto {

    private Long id;
    private String email;
    private String jiraEmail;
    private String firstName;
    private String lastName;
    private List<JiraServerDto> generalProjectList = new ArrayList<>();

}
