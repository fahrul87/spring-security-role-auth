package com.fahrul.springsecurityroleauth.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fahrul.springsecurityroleauth.common.UserConstant;
import com.fahrul.springsecurityroleauth.entity.User;
import com.fahrul.springsecurityroleauth.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping("join")
	public String joinGroup(@RequestBody User user) {
		user.setRoles(UserConstant.DEFAULT_ROLE); // USER
		String encryptedPwd = passwordEncoder.encode(user.getPassword());
		user.setPassword(encryptedPwd);
		userRepository.save(user);
		return "Hi " + user.getUserName() + "Welcome to group !";
	}
	// If loggedin user is ADMIN -> ADMIN OR MODERATOR
	// If loggedin user is MODERATOR -> MODERATOR

	@GetMapping("/access/{userId}/{userRole}")
	// @Secured("ROLE_ADMIN")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String giveAccesToUser(@PathVariable int userId, @PathVariable String userRole, Principal principal) {
		User user = userRepository.findById(userId).get();
		List<String> activeRoles = getRolesByLoggedInUser(principal);
		String newRole = "";
		if (activeRoles.contains(userRole)) {
			newRole = user.getRoles() + "," + userRole;
			user.setRoles(newRole);
		}
		userRepository.save(user);
		return "Hai " + user.getUserName() + " New Role assign to you by " + principal.getName();

	}

	@GetMapping("")
	@Secured("ROLE_ADMIN")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public List<User> loadUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/test")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String testUserAccess() {
		return "user can only acces this.";
	}

	private List<String> getRolesByLoggedInUser(Principal principal) {
		String roles = getLoggedInUser(principal).getRoles();
		List<String> assignRoles = Arrays.stream(roles.split(",")).collect(Collectors.toList());
		if (assignRoles.contains("ROLE_ADMIN")) {
			return Arrays.stream(UserConstant.ADMIN_ACCESS).collect(Collectors.toList());
		}
		if (assignRoles.contains("ROLE_MODERATOR")) {
			return Arrays.stream(UserConstant.MODERATOR_ACCESS).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private User getLoggedInUser(Principal principal) {
		return userRepository.findByUserName(principal.getName()).get();
	}
}
