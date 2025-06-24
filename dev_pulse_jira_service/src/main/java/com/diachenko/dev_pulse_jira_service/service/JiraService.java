package com.diachenko.dev_pulse_jira_service.service;
/*  Dev_Pulse
    01.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.dto.JiraIssueDto;
import com.diachenko.dev_pulse_jira_service.dto.JiraProjectDto;
import com.diachenko.dev_pulse_jira_service.dto.JiraUserDto;
import com.diachenko.dev_pulse_jira_service.dto.jiraResponse.JiraIssuesSearchResponse;
import com.diachenko.dev_pulse_jira_service.dto.jiraResponse.JiraProjectResponse;
import com.diachenko.dev_pulse_jira_service.model.JiraIssue;
import com.diachenko.dev_pulse_jira_service.model.JiraProject;
import com.diachenko.dev_pulse_jira_service.model.JiraServer;
import com.diachenko.dev_pulse_jira_service.model.JiraUser;
import com.diachenko.dev_pulse_jira_service.repo.JiraIssueRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class JiraService {

    private final JiraServerService jiraServerService;
    private final JiraProjectService jiraProjectService;
    private final JiraIssueRepository jiraIssueRepository;
    private final ModelMapper modelMapper;
    private final JiraApiService jiraClientService;
    private final String ACCESSIBLE_RESOURCES_URL = "https://api.atlassian.com/oauth/token/accessible-resources";
    private final JiraUserService jiraUserService;
    private final JiraIssueService jiraIssueService;
    Pattern pattern = Pattern.compile("https://(.*?)\\.atlassian\\.net");

    public String configureProjectCloudIdFromToken(Long jiraServerId) {
        JiraServer project = jiraServerService.findById(jiraServerId);
        if (project.getProjectCloudId() == null) {
            ResponseEntity<String> response = jiraClientService.makeAuthorizedRequest(
                    project, ACCESSIBLE_RESOURCES_URL, HttpMethod.GET, new HashMap<>(), new HttpHeaders(), String.class
            );
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());

                if (root.isArray()) {
                    for (JsonNode node : root) {
                        String url = node.get("url").asText();
                        Matcher matcher = pattern.matcher(url);
                        String match = "";
                        if (matcher.find()) {
                            match = matcher.group(1);
                        }
                        if (!match.isEmpty() && match.equals(project.getProjectUrl())) {
                            String id = node.get("id").asText();
                            project = jiraServerService.findByUrl(match);
                            ;
                            project.setProjectCloudId(id);
                            jiraServerService.save(project);
                            return "successful update";
                        }
                    }
                }

                return "no matching url found";
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        return "was updated";
    }

    public String getProjectsFromJira(Long id) {
        JiraServer generalProject;
        if (jiraServerService.findById(id).getProjectCloudId() == null) {
            configureProjectCloudIdFromToken(id);
        }
        generalProject = jiraServerService.findById(id);
        String url = "https://api.atlassian.com/ex/jira/" + generalProject.getProjectCloudId() + "/rest/api/3/project/search";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        ResponseEntity<String> response = jiraClientService.makeAuthorizedRequest(generalProject, url, HttpMethod.GET, new HashMap<>(), headers, String.class);
        return response.getBody();
    }

    public void fetchAndSaveJiraProjects(Long id) {
        JiraServer generalProject = jiraServerService.findById(id);
        String response = getProjectsFromJira(id);

        ObjectMapper mapper = new ObjectMapper();
        JiraProjectResponse jiraProjects = null;
        try {
            jiraProjects = mapper.readValue(response, JiraProjectResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (jiraProjects != null) {
            for (JiraProjectDto dto : jiraProjects.getValues()) {
                JiraProject project = new JiraProject();
                project.setJiraProjectId(dto.getId());
                project.setKey(dto.getKey());
                project.setName(dto.getName());
                project.setJiraServer(generalProject);
                jiraProjectService.saveOrUpdate(project);
            }
        }
    }

    public void fetchAndSaveJiraUsers(Long jiraServerId) {
        JiraServer jiraServer = jiraServerService.findById(jiraServerId);
        if (jiraServerService.findById(jiraServerId).getProjectCloudId() == null) {
            configureProjectCloudIdFromToken(jiraServerId);
        }

        String url = "https://api.atlassian.com/ex/jira/" + jiraServer.getProjectCloudId()
                + "/rest/api/3/users/search";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<JiraUserDto[]> response = jiraClientService.makeAuthorizedRequest(
                jiraServer,
                url,
                HttpMethod.GET,
                new HashMap<>(),
                headers,
                JiraUserDto[].class
        );

        List<JiraUserDto> jiraUserDtoList = Arrays.stream(Objects.requireNonNull(response.getBody()))
                .filter(x -> "atlassian".equals(x.getAccountType())).toList();
        List<JiraUser> users = modelMapper.map(jiraUserDtoList, new TypeToken<List<JiraUser>>() {
        }.getType());

        users = users.stream().peek(x -> x.setJiraServer(jiraServer)).toList();
        users.forEach(x -> System.out.println( "USER : "+ x.getAccountId() +"  "+ x.getEmailAddress()));
        users.forEach(jiraUserService::saveIfNotExistsOrUpdate);
    }

    public void fetchAndSaveJiraProjectIssues(Long jiraServerId, String key) {
        JiraServer jiraServer = jiraServerService.findById(jiraServerId);

        String url = "https://api.atlassian.com/ex/jira/" + jiraServer.getProjectCloudId()
                + "/rest/api/3/search?jql=project=" + key;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<JiraIssuesSearchResponse> response = jiraClientService.makeAuthorizedRequest(
                jiraServer,
                url,
                HttpMethod.GET,
                new HashMap<>(),
                headers,
                JiraIssuesSearchResponse.class
        );
        List<JiraIssueDto> issueDtos = response.getBody().getIssues();
        JiraProject jiraProject = jiraProjectService.findByKeyAndJiraServerId(key, jiraServerId);

        List<JiraIssue> issuesToSave = issueDtos.stream()
                .map(dto -> {
                    JiraIssue issue = jiraIssueService.getOptionalIssueByKeyAndJiraProjectId(dto.getKey(), jiraProject.getId())
                            .map(existing -> {
                                modelMapper.map(dto, existing);
                                return existing;
                            })
                            .orElseGet(() -> {
                                JiraIssue newIssue = modelMapper.map(dto, JiraIssue.class);
                                newIssue.setJiraProject(jiraProject);
                                return newIssue;
                            });
                    return issue;
                })
                .toList();

        jiraIssueRepository.saveAll(issuesToSave);
    }

}
