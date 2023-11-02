package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;

@RequestMapping("/post")
@RestController
public class PostRestController {
	
	@Autowired
	private PostBO postBO;
	
	@PostMapping("/create")
	public Map<String, Object> create(
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value = "file", required=false) MultipartFile file,
			HttpSession session) {
		// mybatis
		
		// session에 들어있는 유저 id 꺼낸다. session은 무겁기도하고 request와 가까워서 컨트롤러에서 꺼내서 BO로 보낸다
		int userId = (int) session.getAttribute("userId");
		String userLoginId = (String) session.getAttribute("userLoginId");
		
		// DB insert
		postBO.addPost(userId, userLoginId, subject, content, file);
		
		
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("result", "success");
		
		return result;
		
	}
}
