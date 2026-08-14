package com.programming.ranatech.springredditclone.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programming.ranatech.springredditclone.dto.AuthenticationResponse;
import com.programming.ranatech.springredditclone.dto.LoginRequest;
import com.programming.ranatech.springredditclone.dto.RefreshTokenRequest;
import com.programming.ranatech.springredditclone.dto.RegisterRequest;
import com.programming.ranatech.springredditclone.exceptions.SpringRedditException;
import com.programming.ranatech.springredditclone.model.NotificationEmail;
import com.programming.ranatech.springredditclone.model.User;
import com.programming.ranatech.springredditclone.model.VarificationToken;
import com.programming.ranatech.springredditclone.repository.UserRepository;
import com.programming.ranatech.springredditclone.repository.VarificationTokenRepository;
import com.programming.ranatech.springredditclone.security.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VarificationTokenRepository varificationTokenRepository;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final RefreshTokenService refreshTokenService;
	
	public void signup(RegisterRequest registerRequest) {
		
		User user = new User();
		
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnable(false);
		
		userRepository.save(user);
		
		String token = generateVarificationToken(user);
		
		String activationUrl = "http://localhost:8080/api/auth/accountVarification/"+token;
		
		String message = mailContentBuilder.activationMailBuilder(
				user.getUsername(), 
				activationUrl);
		
		mailService.sendMail(new NotificationEmail("Please Activet your account", user.getEmail(),message));
		
		
	}

	private String generateVarificationToken(User user) {
		  String token = UUID.randomUUID().toString();
		  VarificationToken varificationToken = new VarificationToken();
		  
		  varificationToken.setUser(user);
		  varificationToken.setToken(token);
		  
		  varificationTokenRepository.save(varificationToken);
		  
		  return token;
	}
	
	
	public void verifyAccount(String token) {
		Optional<VarificationToken> varificationToken =varificationTokenRepository.findByToken(token);
		fetchUserAndEnable(varificationToken.orElseThrow(()-> new SpringRedditException("Invalid Token")));
	}
	

	private void fetchUserAndEnable(VarificationToken varificationToken) {
		String username = varificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("User not found with username -"+username));
		user.setEnable(true);
		userRepository.save(user);
	}
	
	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), 
				loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenService.generateRefreshToken().getToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(loginRequest.getUsername())
				.build();
	}
	
	
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
    }
	
	
	public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
		
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenRequest.getRefreshToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(refreshTokenRequest.getUsername())
				.build();
				
	}
	
	   public boolean isLoggedIn() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
	    }

}
