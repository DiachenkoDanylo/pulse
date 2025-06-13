package com.diachenko.dev_pulse_jira_service.service;
/*  Dev_Pulse
    15.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.dto.JiraProjectDto;
import com.diachenko.dev_pulse_jira_service.model.JiraProject;
import com.diachenko.dev_pulse_jira_service.repo.JiraProjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JiraProjectService {

    private final JiraProjectRepository repository;
    private final ModelMapper modelMapper;

    public JiraProject save(JiraProject jiraProject) {
        return repository.save(jiraProject);
    }

    public List<JiraProject> findByJiraServerId(Long id) {
        return repository.findAllByJiraServerId(id);
    }

    public List<JiraProjectDto> findByGeneralProjectIdDto(Long id) {
        List<JiraProject> projects = repository.findAllByJiraServerId(id);
        return modelMapper.map(projects, new TypeToken<List<JiraProjectDto>>() {
        }.getType());
    }

    public JiraProject saveOrUpdate(JiraProject newProject) {
        Optional<JiraProject> optionalJiraProject = repository.findByJiraProjectIdAndJiraServer_Id(
                newProject.getJiraProjectId(), newProject.getJiraServer().getId());
        if (optionalJiraProject.isPresent()) {
            return updateProject(newProject);
        }
        return repository.save(newProject);
    }

    public JiraProject updateProject(JiraProject project) {
        JiraProject jira = repository.findByJiraProjectIdAndJiraServer_Id(project.getJiraProjectId(), project.getJiraServer().getId()).get();
        project.setId(jira.getId());
        return repository.saveAndFlush(jira);
    }

    @Transactional
    public JiraProject findByKeyAndJiraServerId(String key, Long serverId) {
        return repository.findByJiraServerIdAndKey(serverId, key).orElse(null);
    }
}
