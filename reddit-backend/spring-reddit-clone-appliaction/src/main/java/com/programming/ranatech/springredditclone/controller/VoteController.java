package com.programming.ranatech.springredditclone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programming.ranatech.springredditclone.dto.VoteDto;
import com.programming.ranatech.springredditclone.service.VoteService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VoteController {

	private final VoteService voteService;
	
	public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto){
		voteService.vote(voteDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
