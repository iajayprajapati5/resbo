package com.example.demo.services;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.PasswordResetToken;
import com.example.demo.Entity.UserEntity;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;

@Service
public class ResetPasswordService {
	@Autowired
	Environment env;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	EmailSenderService mailSender;
	
	@Autowired
	PasswordResetTokenRepository passwordResetRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// Generate password change Token
	public void generateToken(String email) {
		UserEntity user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User doesn't exists."));
		
	    String token = UUID.randomUUID().toString();
		createPasswordResetTokenForUser(user, token);
		mailSender.sendEmail(constructResetTokenEmail(env.getProperty("app.url"), token, user));
		
	}
	
	private void createPasswordResetTokenForUser(UserEntity user, String token) {
		PasswordResetToken myToken = new PasswordResetToken(token, user);
	    passwordResetRepository.save(myToken);
	}
	
	private SimpleMailMessage constructResetTokenEmail(String contextPath, String token, UserEntity user) {
	    String url = contextPath + env.getProperty("app.ui.change.password") + "?token=" + token;
	    String message = String.format("Hi %1$s,\n This is you password reset link\n %2$s \n It will be valid only till next 24hours. \n\n Team ReSBO.",
	     user.getFirstname(), url);
	    return constructEmail("Reset Password", message, user);
	}

	private SimpleMailMessage constructEmail(String subject, String body, UserEntity user) {
	    SimpleMailMessage email = new SimpleMailMessage();
	    email.setSubject(subject);
	    email.setText(body);
	    email.setTo(user.getEmail());
	    email.setFrom(env.getProperty("support.email"));
	    return email;
	}
	
	// Validate password reset token
	public String validatePasswordResetToken(String token) {
	    final PasswordResetToken passToken = passwordResetRepository.findByToken(token)
	    		.orElseThrow(() -> new ResourceNotFoundException("Invalid token."));
	    
	    boolean isValid = isTokenExpired(passToken);
	    if(isValid)
	    	return "valid";
    	
	    throw new ResourceNotFoundException("Expired token.");
	}

	private boolean isTokenExpired(PasswordResetToken passToken) {
	    final Calendar cal = Calendar.getInstance();
	    return passToken.getExpiryDate().after(cal.getTime());
	}
	
	// Change password using token
	public void changeUserPasswordUsingToken(String token, String password) {
		validatePasswordResetToken(token);
		
		UserEntity user = passwordResetRepository.findByToken(token).map(tokenData -> {
			return tokenData.getUser();
		}).orElseThrow(() -> new ResourceNotFoundException("Unable to get user."));
		
	    updatePassword(user, password);
	    
	    passwordResetRepository.deleteByToken(token);
	}
	
	private void updatePassword(UserEntity user, String password) {
		user.setPassword(passwordEncoder.encode(password));
	    userRepo.save(user);
	}
	
	// Change password using existing password
	public void updatePasswordUsingExitingPassword(Long user_id, String oldPassword, String newPassword) {
		UserEntity user = userRepo.findById(user_id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found."));
		
		boolean isCurrentPasswordCorrect = passwordEncoder.matches(oldPassword, user.getPassword());
		if(!isCurrentPasswordCorrect) {
			throw new ResourceNotFoundException("Current password not matching.");
		}
		
		updatePassword(user, newPassword);
	}
}
