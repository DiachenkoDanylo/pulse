import {Project} from './project.interface';

export interface AppUser {
  id: number
  email: string
  jiraEmail: string
  firstName: string
  lastName: string
  jiraServerList: Project[]
}
