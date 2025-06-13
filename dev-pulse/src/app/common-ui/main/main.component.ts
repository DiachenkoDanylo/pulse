import { Component } from '@angular/core';
import {HeaderComponent} from '../header/header.component';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    HeaderComponent,
    RouterLink
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent {

}
