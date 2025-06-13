import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {ChartData, ChartType} from 'chart.js';
import {NgChartsModule} from 'ng2-charts';

@Component({
  selector: 'app-task-per-user',
  standalone: true,
  imports: [
    NgChartsModule
  ],
  templateUrl: './task-per-user.component.html',
  styleUrl: './task-per-user.component.scss'
})
export class TaskPerUserComponent implements OnChanges {
  @Input() data: Record<string, number> = {};


  public chartData: ChartData<'polarArea'> = {
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
