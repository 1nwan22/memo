<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="h-100 d-flex align-items-center justify-content-between bg-warning">
	<%-- logo --%>
	<div>
		<h1 class="font-weight-bold">MEMO 게시판</h1>
	</div>
	
	<%-- 로그인 정보 --%>
	<!-- 세션을 사용할 때 모델명이랑 겹치지 않게 주의 -->
	<div>
		<c:if test="${not empty userName}">
			<span>${userName}님 안녕하세요</span>
			<a href="/user/sign-out">로그아웃</a>
		</c:if>
	</div>
</div>
