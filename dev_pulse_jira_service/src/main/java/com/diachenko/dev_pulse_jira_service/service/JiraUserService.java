package com.diachenko.dev_pulse_jira_service.service;
/*  Dev_Pulse
    07.06.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dev_pulse_jira_service.model.JiraUser;
import com.diachenko.dev_pulse_jira_service.repo.JiraUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JiraUserService {

    private final JiraUserRepository jiraUserRepository;

    public JiraUser saveIfNotExistsOrUpdate(JiraUser jiraUser) {
        Optional<JiraUser> jiraUserOptional = jiraUserRepository.findByAccountId(jiraUser.getAccountId());
        if (jiraUserOptional.isPresent()) {
            jiraUser.setId(jiraUserOptional.get().getId());
        }
        return jiraUserRepository.save(jiraUser);
    }

    public JiraUser getById(Long id) {
        return jiraUserRepository.findById(id).orElse(null);
    }

    public JiraUser getByAccountId(String accountId) {
        return jiraUserRepository.findByAccountId(accountId).orElse(null);
    }
//
//"accountId": "70121:96c8e73a-b47f-4b2c-98d7-7a371f8bbe5b",
//        "accountType": "atlassian",
//        "emailAddress": "moyo6699@gmail.com",
//        "avatarUrls": {
//        "48x48": "https://secure.gravatar.com/avatar/227bc47d5dbc3a2b7ca66d87f4c225c0?d=https%3A%2F%2Favatar-management--avatars.us-west-2.prod.public.atl-paas.net%2Finitials%2FDD-4.png",
//                "24x24": "https://secure.gravatar.com/avatar/227bc47d5dbc3a2b7ca66d87f4c225c0?d=https%3A%2F%2Favatar-management--avatars.us-west-2.prod.public.atl-paas.net%2Finitials%2FDD-4.png",
//                "16x16": "https://secure.gravatar.com/avatar/227bc47d5dbc3a2b7ca66d87f4c225c0?d=https%3A%2F%2Favatar-management--avatars.us-west-2.prod.public.atl-paas.net%2Finitials%2FDD-4.png",
//                "32x32": "https://secure.gravatar.com/avatar/227bc47d5dbc3a2b7ca66d87f4c225c0?d=https%3A%2F%2Favatar-management--avatars.us-west-2.prod.public.atl-paas.net%2Finitials%2FDD-4.png"
//    },
//            "displayName": "Danil Diachenko",
//            "active": true,
//            "locale": "uk_UA"
//},
}
