package com.example.demo.requests;

import org.springframework.web.multipart.MultipartFile;

public class UserBookAdvertise {
	private MultipartFile imageFile;
	private String name;
	private String description;
	private int price;
	private String address;
	private String genre;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public MultipartFile getImageFile() {
		return imageFile;
	}
	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
	@Override
	public String toString() {
		return "UserBookAdvertise [name=" + name + ", description=" + description + ", price=" + price + ", address="
				+ address + ", genre=" + genre + "]";
	}
	
	
}
