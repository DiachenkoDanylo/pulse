import { Component } from '@angular/core';
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-footer',
  standalone: true,
    imports: [
        NgIf
    ],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss'
})
export class FooterComponent {

}
