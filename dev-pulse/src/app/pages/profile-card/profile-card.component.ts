import {Component, Input} from '@angular/core';
import {AppUser} from '../../data/interfaces/user.interface';

@Component({
  selector: 'app-profile-card',
  standalone: true,
  imports: [],
  templateUrl: './profile-card.component.html',
  styleUrl: './profile-card.component.scss'
})

export class ProfileCardComponent {
  @Input() profile!: AppUser;
}
