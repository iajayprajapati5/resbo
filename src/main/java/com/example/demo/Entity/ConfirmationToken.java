package com.example.demo.Entity;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ConfirmationToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String confirmationToken;

	private LocalDate createdDate;

	@OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private UserEntity user;

	public ConfirmationToken() {
	}

	public ConfirmationToken(UserEntity user) {
		this.user = user;
		this.createdDate = LocalDate.now();
		this.confirmationToken = UUID.randomUUID().toString();
	}

	public Long getId() {
		return id;
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	
}
