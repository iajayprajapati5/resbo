package com.example.demo.dto;

public class SimpleMessageResponseDto {
	private String message;

	public SimpleMessageResponseDto(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
