package com.fahrul.springsecurityroleauth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fahrul.springsecurityroleauth.entity.User;
import com.fahrul.springsecurityroleauth.repository.UserRepository;

@Service
public class GroupUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUserName(username);
		return user.map(GroupUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException(username + " Not Found"));
	}

}
