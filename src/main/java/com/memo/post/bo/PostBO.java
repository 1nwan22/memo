package com.memo.post.bo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

@Service
public class PostBO {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass()); // this 이 클래스 PostBO

	@Autowired
	private PostMapper postMapper;

	@Autowired
	private FileManagerService fileManager;

	// input:userId		output:List<Post>
	public List<Post> getPostListByUserId(int userId) {
		return postMapper.selectPostListByUserId(userId);
	}
	
	// input:postId, userId		output:Post
	public Post getPostByPostIdAndUserId(int postId, int userId) {
		return postMapper.selectPostByPostIdAndUserId(postId, userId);
	}

	// input:params output:X
	public void addPost(int userId, String userLoginId, String subject, String content, MultipartFile file) {
		String imagePath = null;

		// 이미지가 있으면 업로드
		if (file != null) {
			imagePath = fileManager.saveFile(userLoginId, file);
		}

		postMapper.insertPost(userId, subject, content, imagePath);
	}
	
	public void updatePost(int userId, String userLoginId, int postId, String subject, String content, MultipartFile file) {
		
		// 기존 글을 가져와본다.(1. 이미지 교체시 삭제 위해		2. 업데이트 대상이 있는지 확인)
		Post post = postMapper.selectPostByPostIdAndUserId(postId, userId);
		// post null check (혹시 모르는 일이 발생)
		// logging   console에 찍을 때 syso 사용 X (사용자의 요청마다 새로운 쓰레드로 만들어지는데 println 메소드는 Thread Safe하지 않아 메소드가 수행되는 동안 사용자에게 Lock이 걸릴 수 있다.)
		// 로깅 습관화!   에러 안잡힐 최악의 상황에는 모든 코드 줄에 찍어야 할 수도
		if (post == null) {
			logger.error("[글 수정] post is null. postId:{}, userId:{}", postId, userId); // 와일드카드 문법
			return;
		}
		
		// 파일이 있다면
		// 1) 새 이미지를 업로드 한다
		// 2) 새 이미지 업로드 성공 시 기존 이미지 제거(기존 이미지가 있을 때)
		String imagePath = null;  // mybatis에서 처리 (기존 이미지가 있는데 null로 교체되면 안되니까)
		if (file != null) {
			// 업로드
			imagePath = fileManager.saveFile(userLoginId, file);
			
			// 업로드 성공 시 기존 이미지 제거(있으면)
			if (imagePath != null && post.getImagePath() != null) { // 업로드가 성공했고, 기존 이미지가 존재한다면 => 삭제
				// 기존 이미지 제거
				fileManager.deleteFile(post.getImagePath());
			}
		}
		
		// DB 글 update
		postMapper.updatePostByPostIdAndUserId(postId, userId, subject, content, imagePath);
		
	}
	
	public void deletePost(int postId, int userId) {
		// 기존글 가져옴(이미지가 있으면 삭제 해야 하기 때)
		Post post = postMapper.selectPostByPostIdAndUserId(postId, userId);
		
		if (post == null) {
			logger.error("[글 삭제] post is null. postId:{}, userId:{}", postId, userId); // 와일드카드 문법
			return;
		}
		
		// 기존 이미지가 존재하면 -> 삭제
		if (post.getImagePath() != null) {
			fileManager.deleteFile(post.getImagePath());
		}
		// DB 삭제
		
		postMapper.deletePostByPostId(postId);
	}
}
