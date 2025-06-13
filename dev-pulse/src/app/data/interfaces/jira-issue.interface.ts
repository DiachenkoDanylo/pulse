export interface JiraIssue {
   id : number,
   jiraIssueId :  number ,
   key :  string,
   summary :  string ,
   status :  string ,
   type :  string ,
   createdAt : string ,
   updatedAt :  string ,
   resolvedAt : string,
   assigneeId : string ,
   reporterId : number ,
   timeSpentSeconds : number,
   storyPoints : number
}
