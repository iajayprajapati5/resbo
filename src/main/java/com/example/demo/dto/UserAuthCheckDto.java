package com.example.demo.dto;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.Entity.UserEntity;

public class UserAuthCheckDto {
	private Long id;
	private String userName;
	private boolean active;
	private List<String> authorities;
	private String firstname;
	private String lastname;
	
	public UserAuthCheckDto() {}
	
	public UserAuthCheckDto(UserDetailsDto user) {
		id = user.getId();
		userName = user.getUsername();
		active = user.isEnabled();
		authorities = user.getAuthorities().stream().map(authority -> authority.toString()).collect(Collectors.toList());
		firstname = user.getFirstname();
		lastname = user.getLastname();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public List<String> getAuthorities() {
		return authorities;
	}

	public String getUsername() {
		return userName;
	}
	
	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public boolean isEnabled() {
		return active;
	}
}
