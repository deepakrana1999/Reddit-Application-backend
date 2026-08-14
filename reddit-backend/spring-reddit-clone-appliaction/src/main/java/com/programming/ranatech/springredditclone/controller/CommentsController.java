package com.programming.ranatech.springredditclone.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.programming.ranatech.springredditclone.dto.CommentsDto;
import com.programming.ranatech.springredditclone.service.CommentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {
	
	private final CommentService commentService;
	
	@PostMapping
	public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto){
		commentService.save(commentsDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	
	@GetMapping(params ="postId")
	public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@RequestParam Long postId){
		return ResponseEntity.status(HttpStatus.OK)
				.body(commentService.getAllCommentsFroPost(postId));
	}
	
	
	@GetMapping(params = "postId")
	public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@RequestParam String username){
		return ResponseEntity.status(HttpStatus.OK)
				.body(commentService.getAllCommentForUser(username));
	}
}
















