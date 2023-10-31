package com.memo.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.memo.user.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	// null or 채워져 있거나(UserEntity 단건)
	public UserEntity findByLoginId(String loginId);
	
	public UserEntity findByLoginIdAndPassword(String loginId, String password);
}
