package com.programming.ranatech.springredditclone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programming.ranatech.springredditclone.model.VarificationToken;


@Repository
public interface VarificationTokenRepository extends JpaRepository<VarificationToken, Long> {

	Optional<VarificationToken> findByToken(String token);
}
