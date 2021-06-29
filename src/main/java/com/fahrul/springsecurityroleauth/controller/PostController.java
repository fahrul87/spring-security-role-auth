package com.fahrul.springsecurityroleauth.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fahrul.springsecurityroleauth.entity.Post;
import com.fahrul.springsecurityroleauth.entity.PostStatus;
import com.fahrul.springsecurityroleauth.repository.PostRepository;

@RestController
@RequestMapping("/post")
public class PostController {

	@Autowired
	private PostRepository postRepository;

	@PostMapping("/create")
	public String createPost(@RequestBody Post post, Principal principal) {
		post.setPostStatus(PostStatus.PENDING);
		post.setUserName(principal.getName());
		postRepository.save(post);
		return principal.getName() + "Your post published Istimiwir , Required ADMIN/MODERATOR Action !";
	}

	@GetMapping("approve/{postId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String approvePost(@PathVariable int postId) {

		Post post = postRepository.findById(postId).get();
		post.setPostStatus(PostStatus.APPROVED);
		postRepository.save(post);
		return "Post Approved Istimiwir!!";

	}

	@GetMapping("/approveAll")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String approveAll() {
		postRepository.findAll().stream().filter(post -> post.getPostStatus().equals(PostStatus.PENDING))
				.forEach(post -> {
					post.setPostStatus(PostStatus.APPROVED);
					postRepository.save(post);
				});
		return "Approved all posts !";
	}

	@GetMapping("/removePost/{postId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String removePost(@PathVariable int postId) {
		Post post = postRepository.findById(postId).get();
		post.setPostStatus(PostStatus.REJECTED);
		postRepository.save(post);
		return "Post Rejected!!";
	}

	@GetMapping("/rejectAll")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String rejectAll() {
		postRepository.findAll().stream().filter(post -> post.getPostStatus().equals(PostStatus.PENDING))
				.forEach(post -> {
					post.setPostStatus(PostStatus.REJECTED);
					postRepository.save(post);
				});
		return "Reject All Post!!";
	}

	@GetMapping("/viewAll")
	public List<Post> findAll() {
		return postRepository.findAll().stream().filter(post -> post.getPostStatus().equals(PostStatus.APPROVED))
				.collect(Collectors.toList());
	}
}
