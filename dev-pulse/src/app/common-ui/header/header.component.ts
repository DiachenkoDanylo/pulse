import { Component } from '@angular/core';
import {NgIf, NgOptimizedImage} from '@angular/common';
import {canActivateAuth} from '../../auth/access.guard';
import {AuthService} from '../../auth/auth.service';
import {Router, RouterLink} from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgIf,
    RouterLink
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  logout() {
    this.authService.logout();
    this.router.navigate(['']);
  }
}
