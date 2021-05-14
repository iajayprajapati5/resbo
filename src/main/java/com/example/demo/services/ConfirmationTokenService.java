package com.example.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.ConfirmationToken;
import com.example.demo.Entity.UserEntity;
import com.example.demo.repository.ConfirmationTokenRepository;
import com.example.demo.repository.UserRepository;

@Service
public class ConfirmationTokenService {
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	private UserRepository userRepository;
	
	public void saveConfirmationToken(ConfirmationToken confirmationToken) {
		confirmationTokenRepository.save(confirmationToken);
	}
	
	public void deleteConfirmationToken(Long id){
		confirmationTokenRepository.deleteById(id);
	}
	
	public void confirmUser(ConfirmationToken confirmationToken) {
		final UserEntity user = confirmationToken.getUser();
		user.setEnabled(true);
		userRepository.save(user);
		deleteConfirmationToken(confirmationToken.getId());
	}
	
	public Optional<ConfirmationToken> findConfirmationTokenByToken(String token){
		return confirmationTokenRepository.findByConfirmationToken(token);
	}
}
