<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글 상세</h1>
		
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요" value="${post.subject}">
		<textarea id="content" class="form-control" rows="10" placeholder="내용을 입력하세요">${post.content}</textarea>
		
		<!-- 이미지가 있을 때만 이미지 영역 추가 -->
		<c:if test="${not empty post.imagePath}">
			<div class="my-4">
				<img src="${post.imagePath}" alt="업로드 된 이미지" width="300">
			</div>
		</c:if>
		
		<div class="d-flex justify-content-end my-4 row">
			<input type="file" id="file" accept=".jpg, .jpeg, .png, .gif" class="col-3">
		</div>
		
		<div class="d-flex justify-content-between">
			<button type="button" id="deleteBtn" class="btn btn-secondary" data-post-id="${post.id}">삭제</button>
			
			<div>
				<a href="/post/post-list-view" class="btn btn-dark">목록</a>
				<button type="button" id="saveBtn" class="btn btn-warning" data-post-id="${post.id}">수정</button>
			</div>
		</div>
	</div>
</div>

<script>
	$(document).ready(function() {
		// 글 저장 버튼
		$("#saveBtn").on("click", function() {
			let postId = $(this).data("post-id");
			let subject = $("#subject").val().trim();
			let content = $("#content").val();
			let fileName = $("#file").val();
			
			// validation check
			if (!subject) {
				alert("제목을 입력하세요");
				return;
			}
			
			if (!content) {
				alert("내용을 입력하세요");
				return;
			}
			
			// 파일이 업로드 된 경우에만 확장자 체크
			if (fileName) { // C:\fakepath\upload.png
				// 확장자만 뽑은 후 소문자로 변경한다.
				let ext = fileName.split(".").pop().toLowerCase();
			
				if ($.inArray(ext, ['jpg', 'jpeg', 'png', 'gif']) == -1) { // 존재하면 인덱스가, 없으면 -1이 나옴
					alert("이미지 파일만 업로드 할 수 있습니다.");
					$("#file").val(""); // 파일을 비운다.
					return;
				}
				
			}
			
			// request param 구성
			// 이미지를 업로드 할 때는 반드시 form 태그가 있어야 한다. (위에서 만들거나 자바스크립트로 만들거나)
			let formData = new FormData();
			formData.append("postId", postId);
			formData.append("subject", subject); // key는 form 태그의 name 속성과 같고 Request parameter명이 된다.
			formData.append("content", content);
			formData.append("file", $("#file")[0].files[0]); // 멀티 파일 업로드는 검색해 보기
			
			$.ajax({
				type:"PUT"
				, url:"/post/update"
				, data:formData
				, enctype:"multipart/form-data" // 파일 업로드를 위한 필수 설정
				, processData:false  // 파일 업로드를 위한 필수 설정
				, contentType:false  // 파일 업로드를 위한 필수 설정
				
			
				, success:function(data) {
					if (data.result == "success") {
						alert("메모가 수정되었습니다.");
						location.reload(true);
					} else {
						// 로직 실패
						alert(data.errorMessage);
						location.href = "/user/sign-in"
					}
				}
				, error:function(request, status, error) {
					alert("글을 저장하는데 실패했습니다");
				}
			});
				
		});
		
		$("#deleteBtn").on("click", function() {
			let postId = $(this).data("post-id");
			
			$.ajax({
				type:"DELETE"
				, url:"/post/delete"
				, data:{"postId":postId}
			
				, success:function(data) {
					if (data.code == 200) {
						alert("삭제 성공");
						location.href = "/post/post-list-view"
					} else if (data.code == 500) {
						alert(data.errorMessage);
					}
				}
				, error:function(request, status, error) {
					alert("글을 삭제하는데 실패했습니다.");
				}
			});
		});
		
		
	});
</script>