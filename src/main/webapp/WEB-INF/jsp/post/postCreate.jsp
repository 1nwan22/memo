<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글쓰기</h1>
		
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요">
		<textarea id="content" class="form-control" rows="10" placeholder="내용을 입력하세요"></textarea>
		
		<div class="d-flex justify-content-end my-4">
			<input type="file" id="file">
		</div>
		
		<div class="d-flex justify-content-between">
			<button type="button" id="postListBtn" class="btn btn-dark">목록</button>
			
			<div>
				<button type="button" id="clearBtn" class="btn btn-secondary">모두 지우기</button>
				<button type="button" id="saveBtn" class="btn btn-warning">저장</button>
			</div>
		</div>
	</div>
</div>

<script>
	$(document).ready(function() {
		// 목록 버튼 클릭 => 글 목록 화면 이동
		$("#postListBtn").on("click", function() {
			location.href = "/post/post-list-view";
		});
		
		// 모두 지우기 버튼
		$("#clearBtn").on("click", function() {
			$("#subject").val("");
			$("#content").val("");
		});
		
		// 글 저장 버튼
		$("#saveBtn").on("click", function() {
			let subject = $("#subject").val().trim();
			let content = $("#content").val();
			
			// validation check
			if (!subject) {
				alert("제목을 입력하세요");
				return;
			}
			
			if (!content) {
				alert("내용을 입력하세요");
				return;
			}
			
			// request param 구성
			// 이미지를 업로드 할 때는 반드시 form 태그가 있어야 한다. (위에서 만들거나 자바스크립트로 만들거나)
			let formData = new FormData();
			formData.append("subject", subject); // key는 form 태그의 name 속성과 같고 Request parameter명이 된다.
			formData.append("content", content);
			
			$.ajax({
				type:"post"
				, url:"/post/create"
				, data:formData
				, enctype:"multipart/form-data" // 파일 업로드를 위한 필수 설정
				, processData:false  // 파일 업로드를 위한 필수 설정
				, contentType:false  // 파일 업로드를 위한 필수 설정
				
			
				, success:function(data) {
					if (data.result == "success") {
						alert("메모가 저장되었습니다.");
						location.href = "/post/post-list-view";
					} else {
						// 로직 실패
						alert(data.errorMessage);
					}
				}
				, error:function(request, status, error) {
					alert("글을 저장하는데 실패했습니다");
				}
			});
		});
		
	});
</script>