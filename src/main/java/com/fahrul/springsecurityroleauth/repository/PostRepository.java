package com.fahrul.springsecurityroleauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fahrul.springsecurityroleauth.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
