package com.kshz.fakebookserver.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kshz.fakebookserver.exceptions.EntityNotFoundException;
import com.kshz.fakebookserver.model.Post;
import com.kshz.fakebookserver.model.User;
import com.kshz.fakebookserver.request.UpdatePostRequest;
import com.kshz.fakebookserver.response.PostResponse;
import com.kshz.fakebookserver.service.PostService;
import com.kshz.fakebookserver.service.UserService;

@RestController
@RequestMapping("/post")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PostResponse postMessage(@Valid @RequestBody Post newPost, HttpServletRequest request) {
		String clientId = (String) request.getAttribute("userId");
		
		Optional<User> client = userService.findById(clientId);
		
		// set up author on new post
		newPost.setAuthor(client.get());
		
		// initialize post
		newPost.initializePost();
		
		Post savedPost = postService.save(newPost);
		
		return new PostResponse(savedPost, true);
	}
	
	@GetMapping("/{postId}")
	public PostResponse getPostByPostId(@PathVariable String postId, HttpServletRequest request) {
		String clientId = (String) request.getAttribute("userId");
		
		// find post
		Optional<Post> foundPost = postService.findById(postId);
		
		if (foundPost.isEmpty()) {
			throw new EntityNotFoundException("No post exist with postId: " + postId, null);
		}
		
		return new PostResponse(foundPost.get(), foundPost.get().getAuthor().getId().equals(clientId));
	}
	
	@PatchMapping("/{postId}")
	public PostResponse updatePost(@PathVariable String postId, @Valid @RequestBody UpdatePostRequest requestBody,
			HttpServletRequest request) {
		String clientId = (String) request.getAttribute("userId");
		
		Optional<User> client = userService.findById(clientId);
		
		Post updatedPost = postService.updatePost(postId, client.get(), requestBody);
		
		return new PostResponse(updatedPost, true);
	}
	
	@DeleteMapping("/{postId}")
	public Map<String, Object> deletePost(@PathVariable String postId, HttpServletRequest request) {
		String clientId = (String) request.getAttribute("userId");
		
		Post deletedPost = postService.deletePost(postId, clientId);
		
		Map<String, Object> returnObj = new LinkedHashMap<>();
		returnObj.put("message", "Post deleted successfully.");
		returnObj.put("post", new PostResponse(deletedPost, true));
		
		return returnObj;
	}
	
	@GetMapping("/all")
	public List<PostResponse> getAllPosts(HttpServletRequest request) {
		String clientId = (String) request.getAttribute("userId");
		
		if (clientId != null) {
			return postService.findPostsForAuthenticatedRequest(clientId).stream()
					.map(post -> new PostResponse(post, false))
					.collect(Collectors.toList());
		} else {
			return postService.findPostsForUnauthenticatedRequest().stream()
					.map(post -> new PostResponse(post, false))
					.collect(Collectors.toList());
		}
	}
}
