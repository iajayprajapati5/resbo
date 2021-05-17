package com.example.demo.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.BookAdvertiseEntity;
import com.example.demo.dto.AdvertiseDetailsDto;
import com.example.demo.dto.MyBookAdvertiseDto;
import com.example.demo.dto.SimpleMessageResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookAdvertiseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.requests.UserBookAdvertise;

@Service
@Transactional
public class BookAdvertiseService {
	private final static String imageStoreBasePath = "D://ReSBOImages//";
	
	@Value("${images.endpoint}") 
	private String imageGetPath;
	
	@Autowired
	private BookAdvertiseRepository advertiseRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	public BookAdvertiseEntity addNewBookAdvertise(Long user_id, UserBookAdvertise bookAdvertise) throws IOException{
		MultipartFile imageFile = bookAdvertise.getImageFile();
		if(imageFile.isEmpty()) new ResponseEntity<>("Image for advertise is mandatory.", HttpStatus.BAD_REQUEST);
		String imageName = storeImage(imageFile);
		
		BookAdvertiseEntity savedAdvertise = userRepo.findById(user_id).map(user -> {
			BookAdvertiseEntity bookAdv = new BookAdvertiseEntity();
			bookAdv.setUser(user);
			bookAdv.setName(bookAdvertise.getName());
			bookAdv.setDescription(bookAdvertise.getDescription());
			bookAdv.setGenre(bookAdvertise.getGenre());
			bookAdv.setPrice(bookAdvertise.getPrice());
			bookAdv.setImageName(imageName);
			return advertiseRepo.save(bookAdv);
		}).orElseThrow( () -> new ResourceNotFoundException("User not found."));
		
		return savedAdvertise;
	}
	
	public BookAdvertiseEntity updateBookAdvertise(Long user_id, Long advertise_id, UserBookAdvertise bookAdvertise) throws IOException{
		if(!userRepo.existsById(user_id)) throw new ResourceNotFoundException("User not found.");
		
		MultipartFile imageFile = bookAdvertise.getImageFile();
		String imageName = storeImage(imageFile);
		
		// Below multiple if statements prevent blank data update
		BookAdvertiseEntity updatedAdvertise = advertiseRepo.findById(advertise_id).map(advertise -> {
			if(!bookAdvertise.getName().trim().isEmpty()) advertise.setName(bookAdvertise.getName());
			if(!bookAdvertise.getDescription().trim().isEmpty()) advertise.setDescription(bookAdvertise.getDescription());
			if(!bookAdvertise.getGenre().trim().isEmpty()) advertise.setGenre(bookAdvertise.getGenre());
			if(bookAdvertise.getPrice() > 0) advertise.setPrice(bookAdvertise.getPrice());
			
			if(!imageName.trim().isEmpty()) {
				String existingImage = advertise.getImageName();
				advertise.setImageName(imageName);
				deleteExistingImage(existingImage);
			}
			
			return advertiseRepo.save(advertise);
		}).orElseThrow( () -> new ResourceNotFoundException("Advertise with this Id not found."));
		
		return updatedAdvertise;
	}
	
	public ResponseEntity<Object> deleteBookAdvertise(Long user_id, Long advertise_id) throws IOException{
		if(!userRepo.existsById(user_id)) throw new ResourceNotFoundException("User not found.");
		
		advertiseRepo.findById(advertise_id).map(advertise -> { 
			deleteExistingImage(advertise.getImageName());
			advertiseRepo.deleteById(advertise_id);
			return advertise;
		}).orElseThrow( () -> new ResourceNotFoundException("Advertise not found with given Id."));
		return new ResponseEntity<>(new SimpleMessageResponseDto("Advertise deleted Successfully."), HttpStatus.OK);
	} 
	
	public List<Map<String, Object>> getFilteredAdvertise(String advertiseName){
		return advertiseRepo.getFilteredAdvertise(advertiseName);
	} 
	
	public ResponseEntity<Object> getAdvertiseDetailById(Long advertise_id, Long user_id){
		AdvertiseDetailsDto advertise = advertiseRepo.findAdvertiseAndUserDetailsById(advertise_id, user_id)
				.map(adv -> {
					adv.setImage(imageGetPath + adv.getImage());
					return adv;
				})
				.orElseThrow(() -> new ResourceNotFoundException("Advertise not found."));
		return ResponseEntity.ok(advertise);
	}
	
	public List<MyBookAdvertiseDto> getAdvertiseOfUser(Long user_id){
		List<MyBookAdvertiseDto> advertiseOfUser = userRepo.findById(user_id).map(user -> {
			return advertiseRepo.findAllByUserId(user.getId()).stream().map(advertise -> {
				MyBookAdvertiseDto adv = new MyBookAdvertiseDto(advertise);
				adv.setImageName(imageGetPath + advertise.getImageName());
				return adv;
			}).collect(Collectors.toList());
		})
		.orElseThrow( () -> new ResourceNotFoundException("User doesn't exists."));
		
		return advertiseOfUser;
	}
	
	public List<Map<String, Object>> getInterestedAdvertise(Long user_id){
		return advertiseRepo.getInterestedAdvertise(user_id);
	} 
	
	private String storeImage(MultipartFile image) throws IOException{
		if(image == null) return "";
		
		String originalImageName = image.getOriginalFilename();
		String[] imageNameArr = originalImageName.split("\\.");
		String imageExt = imageNameArr[imageNameArr.length - 1];
		String imageName = imageName()+"."+imageExt;
		
		File file = new File(imageStoreBasePath + imageName);
		file.createNewFile();
		
		FileOutputStream fout = new FileOutputStream(file);
		fout.write(image.getBytes());
		fout.close();
		
		return imageName;
	}
	
	private void deleteExistingImage(String existingImage){
		try {
			Path fileToDelete = Paths.get(imageStoreBasePath + existingImage);
			Files.deleteIfExists(fileToDelete);
		}catch(IOException ioe) {
			ioe.getStackTrace();
		}
	}
	
	private String imageName() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 20;
        Random random = new Random();
    
        String generatedString = random.ints(leftLimit, rightLimit + 1)
          .limit(targetStringLength)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
    
        return generatedString+"_"+System.currentTimeMillis();
    }

}
