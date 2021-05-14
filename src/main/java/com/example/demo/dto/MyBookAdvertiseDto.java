package com.example.demo.dto;

import java.util.Optional;

import com.example.demo.Entity.BookAdvertiseEntity;
import com.example.demo.Entity.UserEntity;

public class MyBookAdvertiseDto {
	private Long id;
	private String name;
	private String genre; 
	private String description;	
	private int price;
	private String imageName;
	private boolean finalised = false;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public boolean isFinalised() {
		return finalised;
	}
	public void setFinalised(boolean finalised) {
		this.finalised = finalised;
	}
	
	public MyBookAdvertiseDto(BookAdvertiseEntity advertise) {
		this.id = advertise.getId();
		this.name = advertise.getName();
		this.genre = advertise.getGenre();
		this.description = advertise.getDescription();
		this.price = advertise.getPrice();
		this.imageName = advertise.getImageName();
		this.finalised = advertise.isFinalised();
	}
	
}
