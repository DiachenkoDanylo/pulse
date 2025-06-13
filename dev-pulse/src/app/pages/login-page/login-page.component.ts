import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../../auth/auth.service';
import {Router} from '@angular/router';
import {catchError} from 'rxjs';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})

export class LoginPageComponent {

  authService = inject(AuthService)
  router = inject(Router)

  loginErrorMessage: string = '';

  form = new FormGroup({
    username: new FormControl(null, Validators.required),
    password: new FormControl(null, Validators.required)
  })

  onRegister() {
    this.router.navigate(['/register']);
  }

  onLogin() {
    if (this.form.valid) {
      //@ts-ignore
      this.authService.login(this.form.value).subscribe({
        next: (res) => {
          this.router.navigate(['']);
          console.log(res);
        },
        error: (err) => {
          if (err.status === 401) {
            // Наприклад, показати повідомлення на екрані або в консоль
            console.error('Invalid credentials');
            // або покажи повідомлення користувачу
            this.loginErrorMessage = 'Invalid username or password';
          } else {
            console.error('Login error:', err);
            this.loginErrorMessage = 'Unexpected error occurred. Try again later.';
          }
        }
      });
    }
  }
}
