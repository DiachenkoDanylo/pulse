package com.diachenko.dev_pulse_jira_service.config;
/*  Dev_Pulse
    01.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.dto.JiraIssueDto;
import com.diachenko.dev_pulse_jira_service.dto.JiraUserDto;
import com.diachenko.dev_pulse_jira_service.model.JiraIssue;
import com.diachenko.dev_pulse_jira_service.model.JiraUser;
import com.diachenko.dev_pulse_jira_service.service.JiraUserService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Configuration
public class Config {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ModelMapper modelMapper(JiraUserService jiraUserService) {
        ModelMapper modelMapper = new ModelMapper();

        Converter<JiraIssueDto, JiraIssue> issueConverter = ctx -> {
            JiraIssueDto dto = ctx.getSource();
            JiraIssue issue = new JiraIssue();
            issue.setJiraIssueId(dto.getId());
            issue.setKey(dto.getKey());
            issue.setSummary(dto.getFields().getSummary());
            issue.setStatus(dto.getFields().getStatus().getName());
            issue.setType(dto.getFields().getIssuetype().getName());
            issue.setCreatedAt(dto.getFields().getCreated());
            issue.setUpdatedAt(dto.getFields().getUpdated());
            issue.setResolvedAt(dto.getFields().getResolutiondate());

            issue.setStoryPoints(dto.getFields().getStoryPoints());
            // 💡 Мапінг JiraUser через сервіс
            if (dto.getFields().getAssignee() != null) {
                JiraUser assignee = jiraUserService.getByAccountId(dto.getFields().getAssignee().getAccountId());
                issue.setAssigneeId(assignee.getId());
            } else {
                System.out.println("AssigneeId is null");
            }
            if (dto.getFields().getCreator() != null) {
                JiraUser creator = jiraUserService.getByAccountId(dto.getFields().getCreator().getAccountId());
                issue.setCreatorId(creator.getId());
            } else {
                System.out.println("Creator is null");
            }
            return issue;
        };

        Converter<JiraUserDto, JiraUser> jiraUserConverter = ctx -> {
            JiraUserDto dto = ctx.getSource();
            JiraUser jiraUser = new JiraUser();
            jiraUser.setAccountId(dto.getAccountId());
            jiraUser.setDisplayName(dto.getDisplayName());
            jiraUser.setAccountType(dto.getAccountType());
            jiraUser.setEmailAddress(dto.getEmailAddress());
            jiraUser.setId(null);
            return jiraUser;
        };

        modelMapper.createTypeMap(JiraUserDto.class, JiraUser.class)
                .setConverter(jiraUserConverter);

        modelMapper.createTypeMap(JiraIssueDto.class, JiraIssue.class)
                .setConverter(issueConverter);

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setSkipNullEnabled(true);

        return modelMapper;
    }

}
