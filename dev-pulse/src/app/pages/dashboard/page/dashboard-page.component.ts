import {Component, inject, OnInit} from '@angular/core';
import {TaskPerUserComponent} from '../task-per-user/task-per-user.component';
import {MetricsService} from '../../../data/services/metrics.service';
import {ActivatedRoute, RouterLink, RouterOutlet} from '@angular/router';
import {NgChartsModule} from 'ng2-charts';
import {TaskStatusComponent} from '../task-status/task-status.component';
import {FormsModule} from '@angular/forms';
import {TaskPerTimeComponent} from '../task-per-time/task-per-time.component';


@Component({
  selector: 'app-page',
  standalone: true,
  imports: [
    TaskPerUserComponent,
    RouterLink,
    NgChartsModule,
    RouterOutlet,
    TaskStatusComponent,
    FormsModule,
    TaskPerTimeComponent,
  ],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss'
})
export class DashboardPageComponent implements OnInit {

  metricsService = inject(MetricsService);
  private route = inject(ActivatedRoute);
  composite: string = '';
  projId: number = 0;
  projKey: string = '';
  tasksPerUserData: Record<string, number> = {}!;

  taskStatusDistribution: Record<string, number> = {}!;

  taskStatusResolveTimeMonth: Record<string, number> = {}!;
  taskStatusResolveTimeWeek: Record<string, number> = {}!;

  mode: 'month' | 'week' = 'month';
  numberInput: number = new Date().getMonth() + 1; // або +1, бо getMonth() дає 0-11

  loadChart(): void {
    if (this.mode === 'month') {
      this.metricsService.getTaskResolveByMonth(this.projId, this.projKey, this.numberInput).subscribe(data => {
        this.taskStatusResolveTimeMonth = data;
      });
    } else if (this.mode === 'week') {
      this.metricsService.getTaskResolveByWeek(this.projId, this.projKey, this.numberInput).subscribe(data => {
        this.taskStatusResolveTimeWeek = data;
      });
    }
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const composite = params.get('composite');
      if (composite) {
        this.composite = composite;
        const [key, projectIdStr] = composite.split('_');
        const projectId = Number(projectIdStr);
        this.projId = projectId;
        this.projKey = key;
        this.metricsService.getTasksFromUser(this.projId, this.projKey).subscribe(data => {
          this.tasksPerUserData = data;
        });
        this.metricsService.getTasksStatusDistribution(this.projId, this.projKey).subscribe(data => {
          this.taskStatusDistribution = data;
        });
      }
    })
  }

}
