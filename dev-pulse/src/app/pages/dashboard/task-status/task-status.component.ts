import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {NgChartsModule} from 'ng2-charts';
import {ChartData} from 'chart.js';


@Component({
  selector: 'app-task-status',
  standalone: true,
  imports: [
    NgChartsModule
  ],
  templateUrl: './task-status.component.html',
  styleUrl: './task-status.component.scss'
})
export class TaskStatusComponent implements OnChanges {
  @Input() data: Record<string, number> = {};
  public chartData: ChartData<'doughnut'> = {
    labels: [],
    datasets: [
      {
        label: 'Tasks',
        data: []
      }
    ]
  };

  ngOnChanges(changes: SimpleChanges): void {
    console.log(this.data.toString());
    const keys: string[] = Object.keys(this.data);
    const values: number[] = Object.values(this.data);
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
