package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SimpleMessageResponseDto;
import com.example.demo.dto.UserDetailsDto;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.services.ResetPasswordService;

@Controller
public class ResetPasswordController {
	@Autowired
	private ResetPasswordService resetPasswordServ; 
	
	@PostMapping("/resetPassword/generateToken")
	public ResponseEntity<Object> generateToken(@RequestParam("email") String email){
		resetPasswordServ.generateToken(email);
		
		SimpleMessageResponseDto response = new SimpleMessageResponseDto("Reset password link has been sent to you.");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/resetPassword/validate")
	public ResponseEntity<Object> validateToken(@RequestParam("token") String token){
		String validated = resetPasswordServ.validatePasswordResetToken(token);
		
		SimpleMessageResponseDto response = new SimpleMessageResponseDto(validated);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/resetPassword/update")
	public ResponseEntity<Object> changePasswordUsingToken(@RequestParam("token") String token, 
			@RequestParam("password") String password){
		resetPasswordServ.changeUserPasswordUsingToken(token, password);
		
		SimpleMessageResponseDto response = new SimpleMessageResponseDto("Password changed successfully.");
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("user/{user_id}/resetPassword")
	@PreAuthorize("#user_id == authentication.principal.getId")
	public ResponseEntity<Object> changePasswordForUser(@PathVariable("user_id") Long user_id, 
			@RequestParam("currentPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword){
		
		resetPasswordServ.updatePasswordUsingExitingPassword(user_id, oldPassword, newPassword);
		
		SimpleMessageResponseDto response = new SimpleMessageResponseDto("Your password updated successfully.");
		return ResponseEntity.ok(response);
	}
}
