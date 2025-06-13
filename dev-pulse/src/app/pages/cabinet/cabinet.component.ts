import {Component, inject} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {HeaderComponent} from '../../common-ui/header/header.component';
import {UserService} from '../../data/services/user.service';
import {JsonPipe} from '@angular/common';
import {AppUser} from '../../data/interfaces/user.interface';
import {ProfileCardComponent} from '../profile-card/profile-card.component';

@Component({
  selector: 'app-cabinet',
  standalone: true,
  imports: [
    RouterOutlet,
    HeaderComponent,
    JsonPipe,
    ProfileCardComponent
  ],
  templateUrl: './cabinet.component.html',
  styleUrl: './cabinet.component.scss'
})
export class CabinetComponent {
  userService = inject(UserService)
  appUser!: AppUser

  constructor() {
    this.userService.getLoggedUser().subscribe(
      val => {
        this.appUser = val
        console.log(val)
      }
    )
  }
}
