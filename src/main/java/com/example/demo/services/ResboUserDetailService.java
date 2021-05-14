package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Entity.ConfirmationToken;
import com.example.demo.Entity.UserEntity;
import com.example.demo.dto.SuccessResponseDto;
import com.example.demo.dto.UserAuthCheckDto;
import com.example.demo.dto.UserDetailsDto;
import com.example.demo.repository.UserRepository;

@Service
public class ResboUserDetailService implements UserDetailsService{
	@Autowired 
	UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ConfirmationTokenService confirmationTokenService;
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> user = userRepository.findByEmail(username);
		user.orElseThrow(()-> new UsernameNotFoundException("Not Found " + username));
		return user.map(UserDetailsDto::new).get();
	}
	
	public ResponseEntity<Object> addResboUser(UserEntity user){
		List<UserEntity> existingUser = userRepository.findAllByEmailOrMobile(user.getEmail(), user.getMobile());
		if(existingUser.size() > 0) 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with given email or mobile exists.");
		
		user.setEnabled(false);
		user.setRoles("ROLE_USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		
		final ConfirmationToken confirmationToken = new ConfirmationToken(user);
		confirmationTokenService.saveConfirmationToken(confirmationToken);
		sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());
		return ResponseEntity.ok(new SuccessResponseDto("User added successfully."));
	}
	
	public UserAuthCheckDto isAuthenticated(Authentication authentication) throws NullPointerException{
			UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
			UserAuthCheckDto authCheckData = new UserAuthCheckDto(userDetails);
			return authCheckData;
	}
	
	private void sendConfirmationMail(String userMail, String token) {
	
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(userMail);
		mailMessage.setSubject("Mail Confirmation Link!");
		mailMessage.setFrom("<MAIL>");
		mailMessage.setText(
				"Thank you for registering. Please click on the below link to activate your account." + "http://localhost:8080/sign-up/confirm?token="
						+ token);
			
		emailSenderService.sendEmail(mailMessage);
	}
}
