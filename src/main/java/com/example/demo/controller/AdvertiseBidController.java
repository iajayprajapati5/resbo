package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserDetailsDto;
import com.example.demo.services.BidForAdvertiseService;

@RestController
@RequestMapping("/user")
public class AdvertiseBidController {
	
	@Autowired
	private BidForAdvertiseService bidService;
	
	@PostMapping("{user_id}/advertise/{advertise_id}/bid")
	@PreAuthorize("#user_id == authentication.principal.getId")
	public ResponseEntity<Object> placeBid(@PathVariable Long user_id, @PathVariable Long advertise_id){
		return bidService.placeBid(user_id, advertise_id);
	}
	
	@DeleteMapping("{user_id}/advertise/{advertise_id}/myBid")
	@PreAuthorize("#user_id == authentication.principal.getId")
	public ResponseEntity<Object> deleteMyBid(@PathVariable Long user_id, @PathVariable Long advertise_id){		
		return bidService.deleteMyBid(user_id, advertise_id);
	}
	
	@PostMapping("{user_id}/advertise/{advertise_id}/finaliseBid")
	@PreAuthorize("#user_id != authentication.principal.getId")
	public ResponseEntity<Object> finalizeBid(@PathVariable Long user_id, @PathVariable Long advertise_id, Authentication authentication){
		UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
		Long advertise_owner_id = userDetails.getId();
		return bidService.finalizeBid(user_id, advertise_id, advertise_owner_id);
	}
	
	@GetMapping("{user_id}/advertise/{advertise_id}/bidList")
	@PreAuthorize("#user_id == authentication.principal.getId")
	public ResponseEntity<Object> getAdvertiseBids(@PathVariable Long user_id, @PathVariable Long advertise_id){
		return bidService.getBidForAdvertise(user_id, advertise_id);
	}
}
