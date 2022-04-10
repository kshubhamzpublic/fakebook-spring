package com.kshz.fakebookserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.kshz.fakebookserver.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	
	@Query("{ username: ?0 }")
	public Optional<User> findUserByUsername(String username);
	
	@Query("{ email: ?0 }")
	public Optional<User> findUserByEmail(String email);
	
	@Query("{ name: { $regex: /?0/, $options: 'i' } }")
	public List<User> findByName(String searchQuery);
}
