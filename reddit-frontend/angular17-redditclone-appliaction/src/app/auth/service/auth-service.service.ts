import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable, Output } from '@angular/core';
import { LoginRequestPayload } from '../login/login-request.payload';
import { BehaviorSubject, map, Observable, tap, throwError } from 'rxjs';
import { SignupRequestPayload } from '../signup/signup-request.payload';
import { LoginResponse } from '../login/login-response.payload';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  
  // BehaviorSubjects with initial values from localStorage
  private loggedInSubject = new BehaviorSubject<boolean>(this.getJwtToken() != null);
  loggedIn = this.loggedInSubject.asObservable();

  private usernameSubject = new BehaviorSubject<string>(this.getUserName() || '');
  username = this.usernameSubject.asObservable();

  refreshTokenPayload ={
    refreshToken: this.getRefreshToken(),
    username: this.getUserName()
  }
  constructor(private httpClient: HttpClient)
   { }

     signup(signuprequestPayload: SignupRequestPayload ): Observable<any>{
      return this.httpClient.post('http://localhost:8080/api/auth/signup',signuprequestPayload, {responseType: 'text'}); 
     }

     login(loginRequestPayload: LoginRequestPayload): Observable<boolean>{
      return this.httpClient.post<LoginResponse>('http://localhost:8080/api/auth/login', loginRequestPayload)
      .pipe(map(data=>{
        localStorage.setItem('authenticationToken', data.authenticationToken);
        localStorage.setItem('username', data.username);
        localStorage.setItem('refreshToken',data.refreshToken);
        localStorage.setItem('expiresAt', data.expiresAt.toString());

           // Notify subscribers
        this.loggedInSubject.next(true);
        this.usernameSubject.next(data.username);
        return true;
      })
      );
     }

     getJwtToken(){
      return localStorage.getItem('authenticationToken');
     }

     refreshToken() : Observable<any> {
      return this.httpClient.post<LoginResponse>('http://localhost:8080/api/auth/refresh/token',this.refreshTokenPayload)
      .pipe(tap(response=>{
        localStorage.removeItem('authenticationToken');
        localStorage.removeItem('expiresAt');

        localStorage.setItem('authenticationToken',
          response.authenticationToken);

          localStorage.setItem('expiresAt', response.expiresAt.toString());
      }));
     }

     logout(){
        this.httpClient.post('http://localhost:8080/api/auth/logout',this.refreshTokenPayload, {responseType: 'text'}) 
        .subscribe(data => {
          console.log(data);
        }, error => {
          throwError(error);
        })
        
        localStorage.clear();

           // Notify subscribers
    this.loggedInSubject.next(false);
    this.usernameSubject.next('');

     }

  getRefreshToken() {
    return localStorage.getItem('refreshToken');
  }

    getUserName() {
    return localStorage.getItem('username');
  }

  isLoggedIn(): boolean{
    return this.getJwtToken()!= null;
  }
}
