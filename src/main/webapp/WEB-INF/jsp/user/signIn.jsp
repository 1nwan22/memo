<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="d-flex align-items-center justify-content-center h-100 w-100 sign-">
	<div class="">
		<form method="post">
			<div class="input-group">
			  <span class="input-group-text">👨‍💼<span>
			  <input type="text" class="form-control" placeholder="Username">
			</div>
			<div class="input-group">
			  <span class="input-group-text">🔑<span>
			  <input type="password" class="form-control" placeholder="Password">
			</div>
			<button type="submit" class="btn btn-primary">로그인</button>
		</form>
		<button type="button" onclick="location.href=/signIn.jsp'" class="btn btn-dark">회원가입</button>
	</div>
</div>