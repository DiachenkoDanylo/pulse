package com.diachenko.dev_pulse_jira_service.service;

import com.diachenko.dev_pulse_jira_service.model.JiraIssue;
import com.diachenko.dev_pulse_jira_service.model.JiraUser;
import com.diachenko.dev_pulse_jira_service.repo.JiraUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/*  Dev_Pulse
    08.06.2025
    @author DiachenkoDanylo
*/
@Service
@RequiredArgsConstructor
public class JiraMetricsService {

    private final JiraIssueService issueService;
    private final JiraUserRepository jiraUserRepository;

    public Map<String, Long> getTasksPerUserByServerIdAndProjectKey(Long jiraServerId, String projectKey) {
        List<JiraIssue> issues = issueService.getTaskListByJiraServerIdAndProjectKey(projectKey, jiraServerId);

        // Групуємо за assigneeId
        Map<Long, Long> tasksCount = issues.stream()
                .filter(issue -> issue.getAssigneeId() != null)
                .collect(Collectors.groupingBy(JiraIssue::getAssigneeId, Collectors.counting()));

        // Створюємо мапу id → displayName
        Map<Long, String> userIdToName = jiraUserRepository.findAllById(tasksCount.keySet()).stream()
                .collect(Collectors.toMap(JiraUser::getId, JiraUser::getDisplayName));

        // Перетворюємо в Map<displayName, count>

        Map<String, Long> map = tasksCount.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> userIdToName.getOrDefault(entry.getKey(), "Unassigned Task"),
                        Map.Entry::getValue
                ));
        System.out.println("MAP IN METRICS");
        System.out.println(map.keySet());
        System.out.println(map.values());
        return map;
    }

    public Map<String, Long> getTaskStatusDistribution(Long serverId, String projectKey) {

        Map<String, Long> map = issueService.getTaskListByJiraServerIdAndProjectKey(projectKey, serverId).stream()
                .collect(Collectors.groupingBy(
                        JiraIssue::getStatus,
                        Collectors.counting()
                ));

        System.out.println("MAP IN METRICS TASK DISTRIBUTION");
        System.out.println(map.keySet());
        System.out.println(map.values());
        return map;
    }

    public Map<String, Long> getTaskSolvedByMonthAsWeek(Long serverId, String projectKey, Long monthNumber) {
        Year currentYear = Year.now(); // You might want to pass year as param or handle dynamically

        // Define the start and end of the month
        LocalDate monthStart = LocalDate.of(currentYear.getValue(), monthNumber.intValue(), 1);
        LocalDate monthEnd = monthStart.with(TemporalAdjusters.lastDayOfMonth());

        List<JiraIssue> tasks = issueService.getTaskListByJiraServerIdAndProjectKey(projectKey, serverId).stream()
                .filter(issue -> "Done".equals(issue.getStatus()))
                .filter(issue -> {
                    Instant resolvedAt = issue.getResolvedAt();
                    if (resolvedAt == null) return false;
                    LocalDate resolvedDate = resolvedAt.atZone(ZoneId.systemDefault()).toLocalDate();
                    return !resolvedDate.isBefore(monthStart) && !resolvedDate.isAfter(monthEnd);
                })
                .collect(Collectors.toList());

        // Group by week of month
        Map<String, Long> tasksByWeek = tasks.stream()
                .collect(Collectors.groupingBy(issue -> {
                    Instant resolvedAt = issue.getResolvedAt();
                    LocalDate resolvedDate = resolvedAt.atZone(ZoneId.systemDefault()).toLocalDate();

                    WeekFields weekFields = WeekFields.ISO;
                    int weekOfMonth = resolvedDate.get(weekFields.weekOfMonth());

                    // Отримаємо перший і останній день цього тижня
                    LocalDate startOfWeek = resolvedDate.with(weekFields.dayOfWeek(), 1); // Monday
                    LocalDate endOfWeek = resolvedDate.with(weekFields.dayOfWeek(), 7);   // Sunday

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                    return "Week " + weekOfMonth + " (" + startOfWeek.format(formatter) + " - " + endOfWeek.format(formatter) + ")";
                }, Collectors.counting()));
        System.out.println(tasksByWeek.values());
        return tasksByWeek;
    }

    public Map<String, Long> getTaskSolvedByWeekAsDays(Long serverId, String projectKey, Long weekNumber) {
        Year currentYear = Year.now();
        WeekFields weekFields = WeekFields.ISO;

        List<JiraIssue> tasks = issueService.getTaskListByJiraServerIdAndProjectKey(projectKey, serverId).stream()
                .filter(issue -> "Done".equals(issue.getStatus()))
                .filter(issue -> {
                    Instant resolvedAt = issue.getResolvedAt();
                    if (resolvedAt == null) return false;
                    LocalDate resolvedDate = resolvedAt.atZone(ZoneId.systemDefault()).toLocalDate();

                    int resolvedWeek = resolvedDate.get(weekFields.weekOfWeekBasedYear());
                    int resolvedYear = resolvedDate.get(weekFields.weekBasedYear());

                    // Filter tasks resolved in the given week number AND current year
                    return resolvedWeek == weekNumber.intValue() && resolvedYear == currentYear.getValue();
                })
                .collect(Collectors.toList());

        Map<String, Long> tasksByDay = tasks.stream()
                .collect(Collectors.groupingBy(issue -> {
                    Instant resolvedAt = issue.getResolvedAt();
                    LocalDate resolvedDate = resolvedAt.atZone(ZoneId.systemDefault()).toLocalDate();

                    String dayName = resolvedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                    String formattedDate = resolvedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

                    return dayName + " " + formattedDate;
                }, Collectors.counting()));

        System.out.println(tasksByDay.values());
        return tasksByDay;
    }

}
