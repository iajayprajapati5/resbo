package com.example.demo.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserInterestKey implements Serializable{
	
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "advertise_id")
	private Long advertiseId;
	
	public UserInterestKey() {
	}
	
	public UserInterestKey(Long userId, Long advertiseId) {
		this.userId = userId;
		this.advertiseId = advertiseId;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getAdvertiseId() {
		return advertiseId;
	}
	public void setAdvertiseId(Long advertiseId) {
		this.advertiseId = advertiseId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((advertiseId == null) ? 0 : advertiseId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInterestKey other = (UserInterestKey) obj;
		if (advertiseId == null) {
			if (other.advertiseId != null)
				return false;
		} else if (!advertiseId.equals(other.advertiseId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
	
}
