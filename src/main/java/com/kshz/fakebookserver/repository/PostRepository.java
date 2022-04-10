package com.kshz.fakebookserver.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.kshz.fakebookserver.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

	@Query("{ $or: [{ public: true }, { author: ObjectId(?0) }] }")
	public List<Post> findPostsForAuthenticatedRequest(String clientId);

	@Query("{ anonymous: true, public: true }")
	public List<Post> findPostsForUnauthenticatedRequest();

	@Query("{ author: ObjectId(?0) }")
	public List<Post> findAllPostsOfRequestingClient(String clientId);

	@Query("{ author: ObjectId(?0), public: true, anonymous: false }")
	public List<Post> findAllPostOfUser(String userId);
}
