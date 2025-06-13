import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {JiraProjectService} from '../../data/services/jira-project.service';
import {JiraProject} from '../../data/interfaces/jira-project.interface';
import {JiraIssue} from '../../data/interfaces/jira-issue.interface';
import {FormsModule} from '@angular/forms';
import {DatePipe, DecimalPipe, NgForOf} from '@angular/common';

@Component({
  selector: 'app-project-page',
  standalone: true,
  imports: [
    FormsModule,
    DatePipe,
    DecimalPipe,
    NgForOf,
    RouterLink
  ],
  templateUrl: './project-page.component.html',
  styleUrl: './project-page.component.scss'
})
export class ProjectPageComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private jiraProjectService = inject(JiraProjectService);

  project?: JiraProject;
  errorMessage: string = '';
  filteredIssues: JiraIssue[] = [];

  searchTerm: string = '';
  sortField: 'id' | 'createdAt' = 'createdAt';
  sortDirection: 'asc' | 'desc' = 'asc';

  composite: string = '';

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const composite = params.get('composite');
      if (composite) {
        this.composite = composite;
        const [key, projectIdStr] = composite.split('_');
        const projectId = Number(projectIdStr);

        this.jiraProjectService.getJiraProjectByKey(projectId, key).subscribe({
          next: data => {
            this.project = data;
            this.applyFilterAndSort();
          },
          error: err => console.error('Load failed:', err)
        });
      }
    });
  }

  trackByIssue(index: number, issue: JiraIssue): number {
    return issue.id;
  }

  applyFilterAndSort(): void {
    if (!this.project) return;

    const term = this.searchTerm.toLowerCase();

    this.filteredIssues = this.project.jiraIssueList
      .filter(issue =>
        issue.id.toString().includes(term) ||
        issue.createdAt?.toLowerCase().includes(term) ||
        issue.key?.toLowerCase().includes(term) ||
        issue.summary?.toLowerCase().includes(term) ||
        issue.assigneeId?.toLowerCase().includes(term) ||
        issue.type?.toLowerCase().includes(term) ||
        issue.status?.toLowerCase().includes(term)
      )
      .sort((a, b) => {
        const aVal = a[this.sortField];
        const bVal = b[this.sortField];

        if (aVal < bVal) return this.sortDirection === 'asc' ? -1 : 1;
        if (aVal > bVal) return this.sortDirection === 'asc' ? 1 : -1;
        return 0;
      });
  }

  onSearchChange(): void {
    this.applyFilterAndSort();
  }

  onSortChange(field: 'id' | 'createdAt'): void {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.applyFilterAndSort();
  }
}
