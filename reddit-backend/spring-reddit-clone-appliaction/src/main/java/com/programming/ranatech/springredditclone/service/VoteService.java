package com.programming.ranatech.springredditclone.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.programming.ranatech.springredditclone.dto.PostResponse;
import com.programming.ranatech.springredditclone.dto.VoteDto;
import com.programming.ranatech.springredditclone.exceptions.PostNotFoundException;
import com.programming.ranatech.springredditclone.exceptions.SpringRedditException;
import com.programming.ranatech.springredditclone.model.Post;
import com.programming.ranatech.springredditclone.model.Vote;
import com.programming.ranatech.springredditclone.model.VoteType;
import com.programming.ranatech.springredditclone.repository.PostRepository;
import com.programming.ranatech.springredditclone.repository.VoteRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoteService {

	private final VoteRepository voteRepository;
	private final PostRepository postRepository;
	private final AuthService authService;
	
	public void vote(VoteDto voteDto) {
		Post post = postRepository.findById(voteDto.getPostId())
				.orElseThrow(() -> new PostNotFoundException("Post Not Found With ID -"+ voteDto.getPostId().toString()));
	Optional<Vote> voteByPostAndUser =	voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
	if(voteByPostAndUser.isPresent() && 
			voteByPostAndUser.get().getVoteType()
			.equals(voteDto.getVoteType())) {
		throw new SpringRedditException("You have already"+ voteDto.getVoteType() + "'d for this post");
	}
	
	if(VoteType.UPVOTE.equals(voteDto.getVoteType())) {
	   post.setVoteCount(post.getVoteCount() + 1);	
	}else {
		post.setVoteCount(post.getVoteCount() - 1);
	}
	voteRepository.save(mapToVote(voteDto,post));
	postRepository.save(post);
	}
	
	
	private Vote mapToVote(VoteDto voteDto, Post post ) {
		return Vote.builder()
				.voteType(voteDto.getVoteType())
				.post(post)
				.user(authService.getCurrentUser())
				.build();
	}
}
