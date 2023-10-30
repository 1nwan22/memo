package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.entity.UserEntity;

@RequestMapping("/user")
@RestController
public class UserRestController {
	
	@Autowired
	private UserBO userBO;
	
	/*
	 *  로그인 아이디 중복 확인 API
	 * 
	 * */
	
	@RequestMapping("/is-duplicated-id")
	public Map<String, Object> isDuplicatedId(
			@RequestParam("loginId") String loginId) {
		
		// db 조회
		UserEntity user = userBO.getUserEntityByLoginId(loginId);
		
		// 응답값 만들고 리턴 => JSON
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		
		if (user == null) {
			// 중복 아님
			result.put("isDuplicated", false);
		} else {
			// 중복
			result.put("isDuplicated", true);
		}
		return result;
	}
	
	@PostMapping("/sign-up")
	public Map<String, Object> signUp(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("name") String name,
			@RequestParam("email") String email) {
			
		// password 해싱 - md5 알고리즘 (암호화 복호화 없이 해싱된 값끼리 비교) 낮은 보안의 알고리즘
		// aaaa => 74b8733745420d4d33f80c4663dc5e5
		String hashedPassword = EncryptUtils.md5(password);
		
		// DB insert
		Integer id = userBO.addUser(loginId, hashedPassword, name, email);
		
		// 응답값
		Map<String, Object> result = new HashMap<>();
		if (id == null) {
			result.put("code", 500);
			result.put("errorMessage", "회원가입 실패");
		} else {
			result.put("code", 200);
			result.put("result", "성공");
		}
		
		return result; // json
		
	}
}
