import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {NgChartsModule} from "ng2-charts";
import {ChartData} from 'chart.js';

@Component({
  selector: 'app-task-per-time',
  standalone: true,
  imports: [
    NgChartsModule
  ],
  templateUrl: './task-per-time.component.html',
  styleUrl: './task-per-time.component.scss'
})
export class TaskPerTimeComponent implements OnChanges {
  @Input() dataMonth: Record<string, number> = {};
  @Input() dataWeek: Record<string, number> = {};
  @Input() mode: 'month' | 'week' = 'month';

  public chartData: ChartData<'polarArea'> = {
    labels: [],
    datasets: [
      {
        label: 'Tasks',
        data: []
      }
    ]
  };

  ngOnChanges(): void {
    const data = this.mode === 'month' ? this.dataMonth : this.dataWeek;

    const keys: string[] = Object.keys(data);
    const values: number[] = Object.values(data);

    this.chartData = {
      labels: keys,
      datasets: [
        {
          label: 'Tasks',
          data: values
        }
      ]
    };
  }
}
