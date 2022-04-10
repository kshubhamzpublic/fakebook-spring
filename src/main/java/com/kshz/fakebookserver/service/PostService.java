package com.kshz.fakebookserver.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kshz.fakebookserver.exceptions.AuthorizationException;
import com.kshz.fakebookserver.exceptions.BadRequestException;
import com.kshz.fakebookserver.exceptions.EntityNotFoundException;
import com.kshz.fakebookserver.model.Post;
import com.kshz.fakebookserver.model.User;
import com.kshz.fakebookserver.repository.PostRepository;
import com.kshz.fakebookserver.request.UpdatePostRequest;

@Service
public class PostService implements IPostService {
	
	@Autowired
	private PostRepository postRepository;
	
	@Override
	public Optional<Post> findById(String id) {
		return postRepository.findById(id);
	}

	@Override
	public List<Post> findPostsForAuthenticatedRequest(String clientId) {
		return postRepository.findPostsForAuthenticatedRequest(clientId);
	}

	@Override
	public List<Post> findPostsForUnauthenticatedRequest() {
		return postRepository.findPostsForUnauthenticatedRequest();
	}

	@Override
	public List<Post> findPostsByCurrentClient(String clientId) {
		return postRepository.findAllPostsOfRequestingClient(clientId);
	}

	@Override
	public List<Post> findAllPostOfUser(String userId) {
		return postRepository.findAllPostOfUser(userId);
	}

	@Override
	public Post save(Post post) {
		return postRepository.save(post);
	}

	@Override
	public Post updatePost(String postId, User client, UpdatePostRequest requestBody) {
		// find the post
		Optional<Post> post = findById(postId);
		
		if (post.isEmpty()) {
			throw new EntityNotFoundException("No post available with postId: " + postId, null);
		}
		
		Post foundPost = post.get();
		
		String updateType = requestBody.getType();
		
		switch (updateType.trim().toLowerCase()) {
		case "like":
			Set<User> likesL = foundPost.getLikes();
			Set<User> dislikesL = foundPost.getDislikes();
			
			boolean alreadyLikedL = likesL.stream()
					.anyMatch(user -> user.getId().equals(client.getId()));
					
			boolean alreadyDislikedL = dislikesL.stream()
					.anyMatch(user -> user.getId().equals(client.getId()));
			
			// if likes set contain client then remove, else add
			if (alreadyLikedL) {
				likesL = likesL.stream()
						.filter(user -> !user.getId().equals(client.getId()))
						.collect(Collectors.toSet());
			}else {
				likesL.add(client);
				
				// if client is in dislikes then remove from there
				if (alreadyDislikedL) {
					dislikesL = dislikesL.stream()
							.filter(user -> !user.getId().equals(client.getId()))
							.collect(Collectors.toSet());
				}
			}
			
			foundPost.setDislikes(dislikesL);
			foundPost.setLikes(likesL);
			
			break;
		case "dislike":
			Set<User> dislikesD = foundPost.getDislikes();
			Set<User> likesD = foundPost.getLikes();
			
			boolean alreadyLikedD = likesD.stream()
					.anyMatch(user -> user.getId().equals(client.getId()));
					
			boolean alreadyDislikedD = dislikesD.stream()
					.anyMatch(user -> user.getId().equals(client.getId()));
			
			// if dislikes set contain client then remove, else add
			if (alreadyDislikedD) {
				dislikesD = dislikesD.stream()
						.filter(user -> !user.getId().equals(client.getId()))
						.collect(Collectors.toSet());
			} else {
				dislikesD.add(client);
				
				// if client is in likes then remove from there
				if (alreadyLikedD) {
					likesD = likesD.stream()
							.filter(user -> !user.getId().equals(client.getId()))
							.collect(Collectors.toSet());
				}
			}
			
			foundPost.setLikes(likesD);
			foundPost.setDislikes(dislikesD);
			
			break;
		case "update":
			// verify author of the found post
			if (!client.getId().equals(foundPost.getAuthor().getId())) {
				throw new AuthorizationException("You are not allowed to update this post.", 
						"You cannot update this post as you don't own this post.");
			}
			
			// apply update
			String updatedMessage = requestBody.getMessage();
			boolean newPublicValue = requestBody.isPublicPost();
			boolean newAnonyousValue = requestBody.isAnonymous();
			
			if (updatedMessage != null && updatedMessage.trim().length() > 0) {
				foundPost.setMessage(updatedMessage);
			}
			
			foundPost.setAnonymous(newAnonyousValue);
			foundPost.setPublicPost(newPublicValue);
			
			break;
		default:
			throw new BadRequestException("Cannot perform '" + updateType + "' on the requested post.", 
					"type proerty must be from [like, dislike, update] but got: " + updateType);
		}
		
		// update post and return
		return save(foundPost);
	}

	@Override
	public Post deletePost(String postId, String clientId) {
		Optional<Post> foundPost = findById(postId);
		
		if (foundPost.isEmpty()) {
			throw new EntityNotFoundException("No post available with postId: " + postId, null);
		}
		
		Post post = foundPost.get();
		// check if client owns the post
		if (!clientId.equals(post.getAuthor().getId())) {
			throw new AuthorizationException("Unauthorized to delete this post.",
					"You don't own this post.");
		}
		
		postRepository.deleteById(postId);
		
		return post;
	}
	
}
