package com.example.demo.services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.BookAdvertiseEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Entity.UserInterestAdvertiseEntity;
import com.example.demo.Entity.UserInterestKey;
import com.example.demo.dto.AdvertiseBidsListDto;
import com.example.demo.dto.SuccessResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookAdvertiseRepository;
import com.example.demo.repository.UserInterestRepository;
import com.example.demo.repository.UserRepository;

@Service
public class BidForAdvertiseService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BookAdvertiseRepository advertiseRepo;
	
	@Autowired
	private UserInterestRepository userInterestRepo;
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	public ResponseEntity<Object> placeBid(Long user_id, Long advertise_id){
		UserEntity user = userRepo.findById(user_id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		BookAdvertiseEntity advertise = advertiseRepo.findById(advertise_id).orElseThrow(() -> new ResourceNotFoundException("Advertise Not Found"));
		if(advertise.getUser().getId()==user_id) throw new AccessDeniedException("Access Denied.");
		
		UserInterestAdvertiseEntity userInterest = new UserInterestAdvertiseEntity(new UserInterestKey(user_id, advertise_id));
		userInterest.setUser(user);
		userInterest.setAdvertise(advertise);
		userInterest.setAmount(advertise.getPrice());
		
		userInterestRepo.save(userInterest);
		
		return ResponseEntity.ok(new SuccessResponseDto("Your response addded successfully."));
	}
	
	public ResponseEntity<Object> deleteMyBid(Long user_id, Long advertise_id){
		userInterestRepo.findById(new UserInterestKey(user_id, advertise_id)).map(bidData -> {
			userInterestRepo.delete(bidData);
			if(bidData.isFinalised()) {
				BookAdvertiseEntity advertise = bidData.getAdvertise();
				advertise.setFinalised(false);
				advertiseRepo.save(advertise);
				
				Map<String, String> mailDetails = new HashMap<>();
				mailDetails.put("recipientEmail", bidData.getAdvertise().getUser().getEmail());
				mailDetails.put("recipientName", bidData.getAdvertise().getUser().getFirstname());
				mailDetails.put("interestedName", bidData.getUser().getFirstname() + " " + bidData.getUser().getLastname());
				mailDetails.put("advertiseName", bidData.getAdvertise().getName());
				
				sendRemoveInterestFromFinalisedNotify(mailDetails);
			}
			
			return bidData;
		}).orElseThrow(()-> new ResourceNotFoundException("No bid exists for requested query."));
		
		return ResponseEntity.ok(new SuccessResponseDto("Your interest removed successfully."));
	}
	
	public ResponseEntity<Object> finalizeBid(Long user_id, Long advertise_id, Long advertise_owner_id){
		BookAdvertiseEntity advertise = advertiseRepo.findById(advertise_id)
				.orElseThrow(() -> new ResourceNotFoundException("Advertise Not Found"));
		
		
		if(advertise.getUser().getId() != advertise_owner_id) {
			throw new AccessDeniedException("You dont own this advertise.");
		}
		
		UserInterestAdvertiseEntity userInterestDetails = userInterestRepo.findById(new UserInterestKey(user_id, advertise_id))
				.map(interest -> {
					interest.setFinalised(true);
					advertise.setFinalised(true);
					advertiseRepo.save(advertise);
					return userInterestRepo.save(interest);
				}).orElseThrow(() -> new ResourceNotFoundException("User and Advertise interest doesn't exists."));
		
		Map<String, String> mailDetails = new HashMap<>();
		mailDetails.put("recipientName", userInterestDetails.getUser().getFirstname());
		mailDetails.put("recipientEmail", userInterestDetails.getUser().getEmail());
		mailDetails.put("advertiseName", advertise.getName());
		mailDetails.put("ownerName", advertise.getUser().getFirstname() + " " + advertise.getUser().getLastname());
		mailDetails.put("ownerAddress", advertise.getUser().getAddress());
		mailDetails.put("ownerEmail", advertise.getUser().getEmail());
		mailDetails.put("ownerMobile", advertise.getUser().getMobile());
		
		sendBidFinaliseNotification(mailDetails);
		
		return ResponseEntity.ok(new SuccessResponseDto("You have finalised this bid for your advertise."));
	}

	public ResponseEntity<Object> getBidForAdvertise(Long user_id, Long advertise_id){
		BookAdvertiseEntity advertise = advertiseRepo.findById(advertise_id).orElseThrow(() -> new ResourceNotFoundException("Advertise doesn't exists."));
		
		if(advertise.getUser().getId() != user_id) {
			throw new AccessDeniedException("You don't own this advertise.");
		}
		
		List<AdvertiseBidsListDto> bidList = userInterestRepo.findAllBidsByAdvertise(advertise_id); 
		return ResponseEntity.ok(bidList);
	}
	
	private void sendBidFinaliseNotification(Map<String, String> maildetails) {
		
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(maildetails.get("recipientEmail"));
		mailMessage.setSubject("Advertise Interest Finalise Notification");
		mailMessage.setFrom("<MAIL>");
		
		String mailText = String.format("Hello %1$s, \nYour interest shown regarding the \"%2$s\" has been finalised by advertiser. Please find the contact details of the advertiser below. \n\nName: %3$s \nAddress: %4$s \nEmail: %5$s \nMobile: %6$s \n\nTeam ReSBO", maildetails.get("recipientName"), maildetails.get("advertiseName"), maildetails.get("ownerName"), maildetails.get("ownerAddress"), maildetails.get("ownerEmail"), maildetails.get("ownerMobile"));
		
		mailMessage.setText(mailText);	
		emailSenderService.sendEmail(mailMessage);
	}
	
	private void sendRemoveInterestFromFinalisedNotify(Map<String, String> maildetails) {
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(maildetails.get("recipientEmail"));
		mailMessage.setSubject("Advertise Interest Removed Notification");
		mailMessage.setFrom("<MAIL>");
		
		String mailText = String.format("Hello %1$s, \n\"%2$s\" is no more interested in your advertise \"%3$s\" which you had finalised. Please finalise it with some other interested people.\n\nTeam ReSBO", maildetails.get("recipientName"), maildetails.get("interestedName"), maildetails.get("advertiseName"));
		
		mailMessage.setText(mailText);	
		emailSenderService.sendEmail(mailMessage);
	}
}
