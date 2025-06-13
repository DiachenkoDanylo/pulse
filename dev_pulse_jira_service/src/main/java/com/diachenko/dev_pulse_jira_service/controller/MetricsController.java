package com.diachenko.dev_pulse_jira_service.controller;

import com.diachenko.dev_pulse_jira_service.service.JiraMetricsService;
import com.diachenko.dev_pulse_jira_service.service.JiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*  Dev_Pulse
    08.06.2025
    @author DiachenkoDanylo
*/

@RequiredArgsConstructor
@RequestMapping("/metrics")
@RestController
public class MetricsController {

    private final JiraMetricsService metricsService;
    private final JiraService jiraService;

    @GetMapping("/{serverId}/{key}/tasks-per-user")
    public Map<String, Long> getTasksPerUser(@PathVariable(name = "serverId") Long serverId,
                                             @PathVariable(name = "key") String key) {
        jiraService.fetchAndSaveJiraProjectIssues(serverId, key);
        return metricsService.getTasksPerUserByServerIdAndProjectKey(serverId, key);
    }

    //
//    @GetMapping("/{serverId}/{projectKey}/avg-lead-time-per-user")
//    public Map<String, Double> getAvgLeadTimePerUser(@PathVariable Long serverId,
//                                                     @PathVariable String projectKey) {
//        return metricsService.avgLeadTimePerUser(serverId,projectKey);
//    }
//
    @GetMapping("/{serverId}/{key}/task-status-distribution")
    public Map<String, Long> getTaskStatusDistribution(@PathVariable(name = "serverId") Long serverId,
                                                       @PathVariable(name = "key") String key) {
        return metricsService.getTaskStatusDistribution(serverId, key);
    }

    @GetMapping("/{serverId}/{key}/task-resolve-month")
    public Map<String, Long> getTaskResolveMonth(@PathVariable(name = "serverId") Long serverId,
                                                 @PathVariable(name = "key") String key,
                                                 @RequestParam(name = "value") Long value) {
        return metricsService.getTaskSolvedByMonthAsWeek(serverId, key, value);
    }

    @GetMapping("/{serverId}/{key}/task-resolve-week")
    public Map<String, Long> getTaskResolveWeek(@PathVariable(name = "serverId") Long serverId,
                                                @PathVariable(name = "key") String key,
                                                @RequestParam(name = "value") Long value) {
        return metricsService.getTaskSolvedByWeekAsDays(serverId, key, value);
    }


}
