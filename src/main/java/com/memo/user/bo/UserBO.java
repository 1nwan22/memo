package com.memo.user.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memo.user.entity.UserEntity;
import com.memo.user.repository.UserRepository;

@Service
public class UserBO {
	
	@Autowired
	private UserRepository userRepository;
	
	// input:loginId		output:UserEntity(null이거나 entity)
	public UserEntity getUserEntityByLoginId(String loginId) {
		return userRepository.findByLoginId(loginId);
	}
	
	// input:loginId, (hashed)Password		output:UserEntity(null or entity)
	public UserEntity getUserEntityByLoginIdPassword(String loginId, String password) {
		return userRepository.findByLoginIdAndPassword(loginId, password);
	}
	
	// input:4 params		output:id(pk) // 자동으로 insert한 객체 (Entity)를 주지만 가공해서 id(pk)로 준다.
	public Integer addUser(String loginId, String password, String name, String email) { 
		// null이 들어갈 수 있으므로 , BO 입장에선 해싱된 여부는 알 필요 없음
		// UserEntity = save(UserEntity);
		UserEntity userEntity = userRepository.save( // new 객체 생성과 setter
				UserEntity.builder()
				.loginId(loginId)
				.password(password)
				.name(name)
				.email(email)
				.build());
		
		return userEntity == null ? null : userEntity.getId();
	}
	
}
