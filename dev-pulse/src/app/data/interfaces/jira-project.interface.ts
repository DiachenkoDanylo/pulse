import {JiraIssue} from './jira-issue.interface';

export interface JiraProject {
  id: number,
  name: string,
  key: string,
  jiraServerId: number,
  jiraIssueList : JiraIssue[]
}
