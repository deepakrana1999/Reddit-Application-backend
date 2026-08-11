package com.programming.ranatech.springredditclone.service;

import org.springframework.stereotype.Service;

import com.programming.ranatech.springredditclone.dto.CommentsDto;
import com.programming.ranatech.springredditclone.exceptions.PostNotFoundException;
import com.programming.ranatech.springredditclone.mapper.CommentMapper;
import com.programming.ranatech.springredditclone.model.Comment;
import com.programming.ranatech.springredditclone.model.Post;
import com.programming.ranatech.springredditclone.repository.CommentRepository;
import com.programming.ranatech.springredditclone.repository.PostRepository;
import com.programming.ranatech.springredditclone.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {

//	private static final String POST_URL = "http://localhost:4200/api/posts/";
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final CommentMapper commentMapper;
	private final CommentRepository commentRepository;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;
	
	public void save(CommentsDto commentsDto) {
		Post post = postRepository.findById(commentsDto.getPostId())
				.orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
		Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
		commentRepository.save(comment);
	}
}
