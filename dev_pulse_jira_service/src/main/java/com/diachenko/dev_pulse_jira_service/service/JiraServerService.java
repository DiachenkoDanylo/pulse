package com.diachenko.dev_pulse_jira_service.service;
/*  Dev_Pulse
    15.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.dto.JiraServerDto;
import com.diachenko.dev_pulse_jira_service.dto.ProjectCreationDto;
import com.diachenko.dev_pulse_jira_service.model.JiraServer;
import com.diachenko.dev_pulse_jira_service.model.JiraApiState;
import com.diachenko.dev_pulse_jira_service.repo.JiraServerRepository;
import com.diachenko.dev_pulse_jira_service.repo.JiraApiStateRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JiraServerService {

    private final JiraServerRepository jiraServerRepository;
    private final AppUserService appUserService;
    private final ModelMapper modelMapper;
    private final String baseUrl = "https://auth.atlassian.com/authorize?audience=api.atlassian.com&client_id={clientId}&scope={scope}&redirect_uri={redirectUri}&state={state}&response_type=code&prompt=consent";
    private final String generalScopes = "read%3Ajira-work%20read%3Ajira-user";
    private final JiraApiStateRepository jiraApiStateRepository;

    @Value("${jira.api.redirect}")
    private String redirectUrl;

    @Value("${jira.api.clientId}")
    private String clientId;

    public JiraServer handleSaveNewProject(ProjectCreationDto projectCreationDto) {
        JiraServer jiraServer = saveNewProject(projectCreationDto);
        return jiraServer;
    }

    public JiraServer saveNewProject(ProjectCreationDto projectCreationDto) {
        JiraServer project = new JiraServer();
        project.setUser(appUserService.findById(projectCreationDto.getUserId()));
        project.setProjectUrl(projectCreationDto.getProjectUrl());
        project.setName(projectCreationDto.getName());
        return jiraServerRepository.save(project);
    }

    public JiraServer findById(Long projectId) {
        return jiraServerRepository.findById(projectId).orElse(null);
    }

    public String buildAuthorizationLink(Long id) {
        JiraServer jiraServer = jiraServerRepository.findById(id).orElse(null);

        if (jiraServer != null) {
            String uuid = UUID.randomUUID().toString();
            while (jiraApiStateRepository.findByValue(uuid).isPresent()) {
                uuid = UUID.randomUUID().toString();
            }
            JiraApiState state = new JiraApiState();
            state.setProjectId(jiraServer.getId());
            state.setValue(UUID.randomUUID().toString());
            state = jiraApiStateRepository.save(state);
            String url;
            url = baseUrl;
            url = url.replace("{clientId}", clientId)
                    .replace("{scope}", generalScopes + "%20" + "offline_access")
                    .replace("{redirectUri}", redirectUrl)
                    .replace("{state}", state.getValue());
            return url;
        }
        return null;
    }

    public JiraServerDto findByIdDto(Long id) {
        JiraServer jiraServer = jiraServerRepository.findById(id).orElse(null);
        return modelMapper.map(jiraServer, JiraServerDto.class);
    }

    public JiraServer findByUrl(String url) {
        JiraServer jiraServer = jiraServerRepository.findGeneralProjectByProjectUrl(url).orElse(null);
        return jiraServer;
    }

    public JiraServer save(JiraServer project) {
        return jiraServerRepository.save(project);
    }

    public List<JiraServerDto> findByUserId(Long userId) {
        List<JiraServer> jiraServer = jiraServerRepository.findAllByUserId(userId);
        return modelMapper.map(jiraServer, new TypeToken<List<JiraServerDto>>() {
        }.getType());
    }
}
