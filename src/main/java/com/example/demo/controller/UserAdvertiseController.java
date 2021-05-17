package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.BookAdvertiseEntity;
import com.example.demo.dto.MyBookAdvertiseDto;
import com.example.demo.dto.SimpleMessageResponseDto;
import com.example.demo.dto.UserDetailsDto;
import com.example.demo.requests.UserBookAdvertise;
import com.example.demo.services.BookAdvertiseService;

@RestController
@RequestMapping("/user")
public class UserAdvertiseController {
	@Autowired
	private BookAdvertiseService bookAdvService;
	
	@PostMapping(value="/{user_id}/advertise", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("#user_id == authentication.principal.getId")
	public ResponseEntity<Object> addNewAdvertise(@PathVariable Long user_id, UserBookAdvertise userBook){
		try {
			BookAdvertiseEntity savedAdvertise = bookAdvService.addNewBookAdvertise(user_id, userBook);
			return new ResponseEntity<>(new SimpleMessageResponseDto("Advertise added successfully."), HttpStatus.OK);
		}catch(IOException ioe) {
			ioe.printStackTrace();
			return new ResponseEntity<>("Failed to add new advertise.", HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Failed to add new advertise.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value="/{user_id}/advertise/{advertise_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("#user_id == authentication.principal.getId")
	public ResponseEntity<Object> updateAdvertise(@PathVariable(required = true) Long user_id, 
			@PathVariable(required = true) Long advertise_id,
			UserBookAdvertise userBook){
		try {
			BookAdvertiseEntity updatedAdvertise = bookAdvService.updateBookAdvertise(user_id, advertise_id, userBook);
			return new ResponseEntity<>(new SimpleMessageResponseDto("Advertise updated successfully."), HttpStatus.OK);
		}catch(IOException ioe) {
			ioe.printStackTrace();
			return new ResponseEntity<>("Failed to update advertise.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value="/{user_id}/advertise/{advertise_id}")
	@PreAuthorize("#user_id == authentication.principal.getId")
	public ResponseEntity<Object> deleteAdvertise(@PathVariable(required = true) Long user_id, 
			@PathVariable(required = true) Long advertise_id){
		try {
			return bookAdvService.deleteBookAdvertise(user_id, advertise_id);
		}catch(IOException ioe) {
			ioe.printStackTrace();
			return new ResponseEntity<>("Failed to delete advertise.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value="/getFilteredAdvertise")
	public ResponseEntity<Object> getFilteredAdvertise(String advertiseName){
		List<Map<String, Object>> filteredAdvertise= bookAdvService.getFilteredAdvertise(advertiseName);
		return ResponseEntity.ok(filteredAdvertise);
	}

	@GetMapping("/{user_id}/my-advertise")
	@PreAuthorize("#user_id == authentication.principal.getId")
	public ResponseEntity<Object> getAdvertiseOfUser(@PathVariable Long user_id){
		List<MyBookAdvertiseDto> userAdvertises = bookAdvService.getAdvertiseOfUser(user_id);
		return ResponseEntity.ok(userAdvertises);
	}
	
	@GetMapping("/advertise/{advertise_id}")
	public ResponseEntity<Object> getAdvertiseDetailsById(@PathVariable(required = true) Long advertise_id, Authentication authentication){
		UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
		Long user_id = userDetails.getId();
		return bookAdvService.getAdvertiseDetailById(advertise_id, user_id);
	}
	
	@GetMapping("/{user_id}/my-interest")
	@PreAuthorize("#user_id == authentication.principal.getId")
	public ResponseEntity<Object> getInterestedAdvertise(@PathVariable Long user_id){
		List<Map<String, Object>> userAdvertises = bookAdvService.getInterestedAdvertise(user_id);
		return ResponseEntity.ok(userAdvertises);
	}
}
