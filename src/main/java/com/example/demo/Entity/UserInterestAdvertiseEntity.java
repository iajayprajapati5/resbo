package com.example.demo.Entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="user_interest_advertise")
public class UserInterestAdvertiseEntity extends AuditModel{
	@EmbeddedId
	private UserInterestKey userInterstKey;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("userId")
	@JoinColumn(name="user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserEntity user;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("advertiseId")
	@JoinColumn(name="advertise_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BookAdvertiseEntity advertise;
	
	@NotNull
	private int amount;
	
	@Column(columnDefinition = "boolean default 'f'")
	private boolean accepted = false;
	
	@Column(columnDefinition = "boolean default 'f'")
	private boolean finalised = false;

	public UserInterestAdvertiseEntity() {
	}

	public UserInterestAdvertiseEntity(UserInterestKey userInterstKey) {
		super();
		this.userInterstKey = userInterstKey;
	}

	public UserInterestAdvertiseEntity(UserInterestKey userInterstKey, @NotNull int amount, boolean accepted,
			boolean finalised) {
		this.userInterstKey = userInterstKey;
		this.amount = amount;
		this.accepted = accepted;
		this.finalised = finalised;
	}

	@JsonIgnore
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@JsonIgnore
	public BookAdvertiseEntity getAdvertise() {
		return advertise;
	}

	public void setAdvertise(BookAdvertiseEntity advertise) {
		this.advertise = advertise;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean isFinalised() {
		return finalised;
	}

	public void setFinalised(boolean finalised) {
		this.finalised = finalised;
	}

	@Override
	public String toString() {
		return "UserInterestAdvertise [user=" + user + ", advertise=" + advertise + ", amount=" + amount + "]";
	}
	
	
}
