package com.example.demo.dto;

public class SuccessResponseDto {
	private String message;

	public SuccessResponseDto(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
