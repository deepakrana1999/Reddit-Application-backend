package com.programming.ranatech.springredditclone.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.programming.ranatech.springredditclone.dto.PostRequest;
import com.programming.ranatech.springredditclone.dto.PostResponse;
import com.programming.ranatech.springredditclone.model.Post;
import com.programming.ranatech.springredditclone.model.Subreddit;
import com.programming.ranatech.springredditclone.model.User;
import com.programming.ranatech.springredditclone.model.Vote;
import com.programming.ranatech.springredditclone.model.VoteType;
import com.programming.ranatech.springredditclone.repository.CommentRepository;
import com.programming.ranatech.springredditclone.repository.VoteRepository;
import com.programming.ranatech.springredditclone.service.AuthService;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

	@Autowired
	private CommentRepository commentReposetory;
	@Autowired
	private VoteRepository voteRepository;
	@Autowired
	private AuthService authService;
	
	
	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "subreddit", source = "subreddit")
	@Mapping(target = "voteCount", constant = "0")
	@Mapping(target = "user", source = "user")
	public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);
	
	@Mapping(target = "id", source = "postId")
	@Mapping(target = "subredditName", source = "subreddit.name")
	@Mapping(target = "userName", source = "user.username")
	@Mapping(target = "commentCount", expression = "java(commentCount(post))")
	@Mapping(target = "duration", expression = "java(getDuration(post)")
	@Mapping(target = "upVote", expression = "java(isPostUpVoted(post)")
	@Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
	public abstract PostResponse mapToDto(Post post);
	
	Integer commentCount(Post post) {
		return commentReposetory.findByPost(post).size();
	}
	
	String getDuration(Post post) {
		return TimeAgo.using(post.getCreatedDate().toEpochMilli());
	}
	
    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, VoteType.UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, VoteType.DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }
}
