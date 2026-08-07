package com.programming.ranatech.springredditclone.model;

import java.util.Arrays;

import com.programming.ranatech.springredditclone.exceptions.SpringRedditException;

public enum VoteType {
 
	UPVOTE(1),DOWNVOTE(-1),;
	
	private final int direction;
	
	VoteType(int direction){
		this.direction = direction;
	}
	
	public Integer getDerection() {
		return direction;
	}
	
	public static VoteType lookup(Integer direction) {
		return Arrays.stream(VoteType.values())
				.filter(value -> value.getDerection().equals(direction))
				.findAny()
				.orElseThrow(() -> new SpringRedditException("Vote not found"));
	}
	
	
}
