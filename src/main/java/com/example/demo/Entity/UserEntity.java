package com.example.demo.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@Entity
@Table(name = "users")
//@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class UserEntity extends AuditModel{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotNull
	@Size(max=20)
	private String firstname;
	
	@NotNull
	@Size(max=20)
	@Column(length = 20, nullable = false)
	private String lastname;
	
	@NotNull
	@Size(max=10)
	@Column(unique = true)
	private String mobile;
	
	@NotNull
	@Size(max=320)
	@Column(unique = true)
	private String email;
	
	@Column(nullable = false, columnDefinition = "boolean default 'f'")
	private boolean enabled = false;
	
	@NotNull
	private String password;
	
	@NotNull
	private String address;
	
	@NotNull
	@Size(max=6, min = 6)
	@Column(length = 6)
	private String pincode;
	
	@NotNull
	private String roles = "ROLE_USER";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", mobile=" + mobile
				+ ", email=" + email + ", enabled=" + enabled + ", address=" + address + ", pincode=" + pincode
				+ ", roles=" + roles + "]";
	}

}
