package com.programming.ranatech.springredditclone.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programming.ranatech.springredditclone.dto.PostRequest;
import com.programming.ranatech.springredditclone.dto.PostResponse;
import com.programming.ranatech.springredditclone.exceptions.PostNotFoundException;
import com.programming.ranatech.springredditclone.exceptions.SubredditNotFoundException;
import com.programming.ranatech.springredditclone.mapper.PostMapper;
import com.programming.ranatech.springredditclone.model.Post;
import com.programming.ranatech.springredditclone.model.Subreddit;
import com.programming.ranatech.springredditclone.model.User;
import com.programming.ranatech.springredditclone.repository.PostRepository;
import com.programming.ranatech.springredditclone.repository.SubredditRepository;
import com.programming.ranatech.springredditclone.repository.UserRepository;
import com.programming.ranatech.springredditclone.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

	private final PostService postService;
	private final PostRepository postRepository;
	private final PostMapper postMapper;
	private final SubredditRepository subredditRepository;
	private final UserRepository userRepository;
	
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
    	Post post = postRepository.findById(id)
    			.orElseThrow(() -> new PostNotFoundException(id.toString()));
    	return postMapper.mapToDto(post);
    }
    
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPost(){
    	return postRepository.findAll()
    			.stream()
    			.map(postMapper::mapToDto)
    			.collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PostResponse> getPostBySubreddit(Long subredditId){
    	Subreddit subreddit = subredditRepository.findById(subredditId)
    			.orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
    	List<Post> posts = postRepository.findAllBySubreddit(subreddit);
    	return posts.stream()
    			.map(postMapper :: mapToDto)
    			.collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
