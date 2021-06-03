package com.example.demo.dto;

public class AdvertiseDetailsDto {
	
	private Long id;
	private String name; 
	private String description; 
	private String image;
	private String genre; 
	private int price; 
	private Long user_id; 
	private String user_fullname; 
	private String address; 
	private boolean finalised; 
	private String created_at;
	private int user_interested;
	
	public AdvertiseDetailsDto() {}
	
	public AdvertiseDetailsDto(Long id, String name, String description, String image, String genre, int price,
			Long user_id, String user_fullname, String address, boolean finalised, String created_at, int user_interested) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.image = image;
		this.genre = genre;
		this.price = price;
		this.user_id = user_id;
		this.user_fullname = user_fullname;
		this.address = address;
		this.finalised = finalised;
		this.created_at = created_at;
		this.user_interested = user_interested;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUser_fullname() {
		return user_fullname;
	}

	public void setUser_fullname(String user_fullname) {
		this.user_fullname = user_fullname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isFinalised() {
		return finalised;
	}

	public void setFinalised(boolean finalised) {
		this.finalised = finalised;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public int getUser_interested() {
		return user_interested;
	}

	public void setUser_interested(int user_interested) {
		this.user_interested = user_interested;
	}
	
	
}
