import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ViewPostComponent } from './post/view-post/view-post.component';
import { UserProfileComponent } from './auth/user-profile/user-profile.component';
import { authGuard } from './auth/auth.guard';
import { ListSubredditsComponent } from './subreddit/list-subreddits/list-subreddits.component';
import { CreatePostComponent } from './post/create-post/create-post.component';
import { CraeteSubredditComponent } from './subreddit/craete-subreddit/craete-subreddit.component';
import { SignupComponent } from './auth/signup/signup.component';
import { LoginComponent } from './auth/login/login.component';
import { HeaderComponent } from './header/header.component';

export const routes: Routes = [
    {path:'', component: HomeComponent},
    {path:'view-post/:id', component: ViewPostComponent},
    {path:'user-profile/:name', component: UserProfileComponent, canActivate:[authGuard]},
    {path: 'list-subreddits', component: ListSubredditsComponent},
    {path: 'create-post', component:CreatePostComponent, canActivate:[authGuard]},
    {path: 'create-subreddit', component:CraeteSubredditComponent, canActivate:[authGuard]},
    {path:'sign-up', component:SignupComponent},
    {path:'login', component:LoginComponent},
    {path:'header', component:HeaderComponent}
];
