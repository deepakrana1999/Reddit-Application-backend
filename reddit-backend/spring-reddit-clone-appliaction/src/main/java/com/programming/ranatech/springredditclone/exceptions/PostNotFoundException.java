package com.programming.ranatech.springredditclone.exceptions;


public class PostNotFoundException extends RuntimeException{
	public PostNotFoundException(String message) {
		super(message);
	}

}
