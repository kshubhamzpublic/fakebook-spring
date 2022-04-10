package com.kshz.fakebookserver.service;

import java.util.List;
import java.util.Optional;

import com.kshz.fakebookserver.model.Post;
import com.kshz.fakebookserver.model.User;
import com.kshz.fakebookserver.request.UpdatePostRequest;

public interface IPostService {

	public Optional<Post> findById(String id);

	public List<Post> findPostsForAuthenticatedRequest(String clientId);

	public List<Post> findPostsForUnauthenticatedRequest();

	public List<Post> findPostsByCurrentClient(String clientId);

	public List<Post> findAllPostOfUser(String userId);

	public Post save(Post post);

	public Post updatePost(String postId, User client, UpdatePostRequest requestBody);
	
	public Post deletePost(String postId, String clientId);
}
