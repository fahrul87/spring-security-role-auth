package com.fahrul.springsecurityroleauth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fahrul.springsecurityroleauth.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUserName(String userName);
}
