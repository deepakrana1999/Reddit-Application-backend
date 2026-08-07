import { Component, OnInit } from '@angular/core';
import { SignupRequestPayload } from './signup-request.payload';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../service/auth-service.service';
import { Route, Router, RouterLink } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent implements OnInit {

signupRequestPayload : SignupRequestPayload ={
  username: '',
  email:'',
  password: ''
};
signupForm!: FormGroup;

constructor(
  private authService: AuthService, 
  private router:Router, 
  private toastr: ToastrService
){}

  ngOnInit(): void {
    this.signupForm = new FormGroup({
      username: new FormControl('',Validators.required),
      email: new FormControl('',Validators.required),
      password: new FormControl('',Validators.required)
    });
  }

  signup(){
    this.signupRequestPayload.email = this.signupForm.get('email')?.value??'';
     this.signupRequestPayload.username = this.signupForm.get('username')?.value??'';
    this.signupRequestPayload.password = this.signupForm.get('password')?.value??'';

    this.authService.signup(this.signupRequestPayload).subscribe(
      {next:() => {this.router.navigate(['/login'],
      {queryParams:{registered: 'true'}});
    },error: (error) =>{
      console.log(error);
      this.toastr.error('Registration Failed! please try again');
    }
   } );
  
   }

}
