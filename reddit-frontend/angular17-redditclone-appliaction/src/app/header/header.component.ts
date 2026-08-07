import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/service/auth-service.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink,CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
    
    isLoggedIn=false;
    username='';

    showMenu = false;

    constructor( private authService: AuthService, private router: Router){}

    ngOnInit(): void {
     
    this.authService.loggedIn.subscribe(status => {
      this.isLoggedIn = status;
    });

    this.authService.username.subscribe(name => {
      this.username = name;
    });

    }

    toggleMenu() {
  this.showMenu = !this.showMenu;
    }

    goToUserProfile(){
      this.router.navigateByUrl('/user-profile/' + this.username);
      this.showMenu = false;
    }

    
  logout() {
    this.authService.logout();
    this.router.navigateByUrl('');
  }
}
