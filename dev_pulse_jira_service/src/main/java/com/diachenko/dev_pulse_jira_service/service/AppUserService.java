package com.diachenko.dev_pulse_jira_service.service;
/*  Dev_Pulse
    14.05.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.dto.AppUserDto;
import com.diachenko.dev_pulse_jira_service.dto.JiraServerDto;
import com.diachenko.dev_pulse_jira_service.dto.JiraProjectDto;
import com.diachenko.dev_pulse_jira_service.model.AppUser;
import com.diachenko.dev_pulse_jira_service.repo.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public AppUser saveNewUser(AppUser appUser) {
        if(repository.findByEmail(appUser.getEmail()).isPresent()){
            return null;
        }
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return repository.save(appUser);
    }

    public AppUser findById(Long userId) {
        return repository.findById(userId).orElse(null);
    }

    public AppUser findByEmail(String name) {
        return repository.findByEmail(name).orElse(null);
    }

    public AppUserDto findByEmailDto(String name) {
        AppUser appUser = repository.findByEmail(name).orElse(null);
        return appUser!=null ? modelMapper.map(appUser, AppUserDto.class) : null;
    }

    public AppUserDto findByIdDto(Long id) {
        AppUser appUser = repository.findById(id).orElse(null);
        if (appUser == null) return null;

        AppUserDto dto = new AppUserDto();
        dto.setId(appUser.getId());
        dto.setEmail(appUser.getEmail());
        dto.setFirstName(appUser.getFirstName());
        dto.setLastName(appUser.getLastName());
        dto.setJiraEmail(appUser.getJiraEmail());

        // Мапимо generalProjectList
        List<JiraServerDto> generalProjectDtos = appUser.getGeneralProjectList().stream()
                .map(gp -> {
                    JiraServerDto gpDto = modelMapper.map(gp, JiraServerDto.class);

                    List<JiraProjectDto> jiraProjectDtos = gp.getJiraProjectList().stream()
                            .map(jiraProject -> modelMapper.map(jiraProject, JiraProjectDto.class))
                            .collect(Collectors.toList());

                    gpDto.setJiraProjectListDto(jiraProjectDtos);
                    return gpDto;
                })
                .collect(Collectors.toList());

        dto.setGeneralProjectList(generalProjectDtos);
        return dto;
    }

}
