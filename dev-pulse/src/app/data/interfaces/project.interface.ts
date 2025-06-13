import {JiraProject} from './jira-project.interface';

export interface Project {
  id: number,
  name: string,
  projectUrl: string,
  jiraProjectListDto: JiraProject[]
}
