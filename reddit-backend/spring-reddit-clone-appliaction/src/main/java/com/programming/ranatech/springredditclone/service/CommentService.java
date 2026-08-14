package com.programming.ranatech.springredditclone.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.programming.ranatech.springredditclone.dto.CommentsDto;
import com.programming.ranatech.springredditclone.exceptions.PostNotFoundException;
import com.programming.ranatech.springredditclone.exceptions.SpringRedditException;
import com.programming.ranatech.springredditclone.mapper.CommentMapper;
import com.programming.ranatech.springredditclone.model.Comment;
import com.programming.ranatech.springredditclone.model.NotificationEmail;
import com.programming.ranatech.springredditclone.model.Post;
import com.programming.ranatech.springredditclone.model.User;
import com.programming.ranatech.springredditclone.repository.CommentRepository;
import com.programming.ranatech.springredditclone.repository.PostRepository;
import com.programming.ranatech.springredditclone.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {

	private static final String POST_URL = "http://localhost:4200/api/posts/";
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
		
		String message = mailContentBuilder.commentMailBuilder(post.getUser().getUsername(), authService.getCurrentUser().getUsername(), POST_URL);
		sendCommentNotification(message, post.getUser());
	}

	
	private void sendCommentNotification(String message, User user) {
		mailService.sendMail(new NotificationEmail(user.getUsername() + "Comment on your Post",user.getEmail(),message));
	}
	
	
	public List<CommentsDto> getAllCommentsFroPost(Long postId){
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
		return commentRepository.findByPost(post)
				.stream()
				.map(commentMapper::mapToDto)
				.toList();
	}
	
	
	public List<CommentsDto> getAllCommentForUser(String userName){
		User user = userRepository.findByUsername(userName)
				.orElseThrow(() -> new UsernameNotFoundException(userName.toString()));
		return commentRepository.findByUser(user)
				.stream()
				.map(commentMapper::mapToDto)
				.toList();
	}
	
	
	public boolean containsSwearWords(String comment) {
		if(comment.toLowerCase().contains("shit")||comment.toLowerCase().contains("fuck")) {
			throw new SpringRedditException("Comments contains unacceptable language");
		}
		return false;
	}
}



















