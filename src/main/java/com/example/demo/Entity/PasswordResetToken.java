package com.example.demo.Entity;

import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.catalina.User;
import org.springframework.mail.SimpleMailMessage;

@Entity
public class PasswordResetToken {
	 
    private static final int EXPIRATION = 24 * 3600 * 1000;
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    private String token;
 
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;
 
    @Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, updatable = false)
    private Date expiryDate;

    public PasswordResetToken() {
	}

	public PasswordResetToken(String token, UserEntity user){
        this.token = token;
        this.user = user;
        expiryDate = new Date(new Date().getTime() + EXPIRATION);
    }

	public Long getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public UserEntity getUser() {
		return user;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

}
