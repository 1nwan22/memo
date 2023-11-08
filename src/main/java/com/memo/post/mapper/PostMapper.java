package com.memo.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.memo.post.domain.Post;

@Repository
public interface PostMapper {
	
	public List<Post> selectPostList();
	
	public List<Post> selectPostListByUserId(int userId);
	
	public Post selectPostByPostIdAndUserId(
			@Param("postId") int postId, 
			@Param("userId") int userId);
	
	public void insertPost(
			@Param("userId") int userId, 
			@Param("subject") String subject, 
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	
	public void updatePostByPostIdAndUserId(
			@Param("postId") int postId, 
			@Param("userId") int userId, 
			@Param("subject") String subject, 
			@Param("content") String content, 
			@Param("imagePath") String imagePath);
}
