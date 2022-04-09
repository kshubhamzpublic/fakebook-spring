package com.kshz.fakebookserver.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("post")
public class Post {
	private String id;
	private String message;
	private boolean anonymous;
	private boolean publicPost;
	private LocalDateTime postedAt;
	private User author;
	private List<User> likes;
	private List<User> dislikes;
}
