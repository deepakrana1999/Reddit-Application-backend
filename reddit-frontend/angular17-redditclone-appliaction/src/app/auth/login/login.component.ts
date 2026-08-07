import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LoginRequestPayload } from './login-request.payload';
import { AuthService } from '../service/auth-service.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-lonin',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;
  loginRequestPayload: LoginRequestPayload = {
    username: '',
    password: ''
  };

  registerSuccessMessage = '';
  isError = false;

  constructor(
    private authService: AuthService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {

    this.loginForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });

    this.activatedRoute.queryParams.subscribe(params => {

      if (
        params['registered'] !== undefined &&
        params['registered'] === 'true'
      ) {
        this.toastr.success('Signup Successful');

        this.registerSuccessMessage =
          'Please check your inbox for the activation email. Activate your account before logging in.';
      }

    });
  }

  login(): void {

    this.loginRequestPayload = {
      username: this.loginForm.get('username')?.value ?? '',
      password: this.loginForm.get('password')?.value ?? ''
    };

    this.authService.login(this.loginRequestPayload).subscribe({
      next: () => {
        this.isError = false;
        this.toastr.success('Login Successful');
        this.router.navigateByUrl('/');
      },
      error: (error) => {
        this.isError = true;
        this.toastr.error('Invalid username or password');
        console.error(error);
      }
    });
  }
}
