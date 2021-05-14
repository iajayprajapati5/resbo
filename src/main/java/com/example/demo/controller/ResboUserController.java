package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.ConfirmationToken;
import com.example.demo.Entity.UserEntity;
import com.example.demo.dto.UserAuthCheckDto;
import com.example.demo.services.ConfirmationTokenService;
import com.example.demo.services.ResboUserDetailService;

@RestController
public class ResboUserController {
	@Autowired
	private ResboUserDetailService userService;
	
	@Autowired
	private ConfirmationTokenService confirmationTokenService;
	
	@PostMapping("/signup")
	public ResponseEntity<Object> registerUser(@RequestBody UserEntity user){
		return userService.addResboUser(user);
	}
	
	@GetMapping("/confirm")
	public String confirmMail(@RequestParam("token") String token) {

		Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);

		optionalConfirmationToken.ifPresent(confirmationTokenService::confirmUser);

		return "/sign-in";
	}
	
	@GetMapping("/isAuthenticated")
	public ResponseEntity<Object> isAuhenticated(Authentication authentication){
		try {
			UserAuthCheckDto userDetails = userService.isAuthenticated(authentication);
			return new ResponseEntity<Object>(userDetails, HttpStatus.OK);
		}catch(NullPointerException npe) {
			npe.printStackTrace();
			Map<String, String> res = new HashMap<>();
			res.put("message", "No user session exists, please log in to continue.");
			return new ResponseEntity<Object>(res, HttpStatus.UNAUTHORIZED);
		}
	} 
}
