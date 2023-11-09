package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component // spring bean -> Autowired 사용해서 호출
public class FileManagerService {
	
	private Logger logger = LoggerFactory.getLogger(FileManagerService.class); // this.getclass()
	
	// 실제 업로드가 된 이미지가 저장될 경로(서버) 주소마지막에 / 붙이기
	public static final String FILE_UPLOAD_PATH = "D:\\godh22\\5_spring_project\\memo\\workspace\\images/";
	
	// 집에서 할 것
//	public static final String FILE_UPLOAD_PATH = "C:\\Salt\\coading\\5_spring_project\\memo\\workspace\\images/";
	
	// input: userLoginId, file(이미지)		output: web imagePath
	public String saveFile(String loginId, MultipartFile file) {
		// 폴더 생성 (로그인아이디 + 현재시간 ms)
		// 예: aaaa_14324123/sun.png
		String directoryName = loginId + "_" + System.currentTimeMillis();
		String filePath = FILE_UPLOAD_PATH + directoryName;  //D:\\godh22\\5_spring_project\\memo\\workspace\\images/aaaa_14324123
		
		File directory = new File(filePath);
		if (directory.mkdir() == false) {
			// 폴더 생성 실패 시 이미지 경로 null로 리턴
			return null;
		}
				
				
		// 파일 업로드: byte 단위로 업로드
		try {
			byte[] bytes = file.getBytes();
			// ★★★★★ 한글 이름 이미지는 올릴 수 없으므로 나중에 영문자로 바꿔서 올리기 (file.getOriginalFilename())
			Path path = Paths.get(filePath + "/" + file.getOriginalFilename()); // 디렉토리 경로 + 사용자가 올린 파일명
			Files.write(path, bytes); // 파일 업로드
		} catch (IOException e) {
			logger.error("[이미지 업로드] 업로드 실패 loginId:{}, filePath:{}", loginId, filePath);
			return null; // 이미지 업로드 실패 시 null 리턴
		}
		
		
		
		// 파일 업로드가 성공했으면 웹 이미지 url path를 리턴
		// 주소는 이렇게 될 것이다.(예언)
		// http://localhost/images/aaaa_현재시간/sun.png
		
		return "/images/" + directoryName + "/" + file.getOriginalFilename();
	}
	
	// input:imagePath		output:X
	public void deleteFile(String imagePath) { // imagePath: /images/aaaaa_1698924006248/screenshot4.png
		// D:\\godh22\\5_spring_project\\memo\\workspace\\images/aaaaa_1698924006248/screenshot4.png
		// 주소에 겹치는  /images/ 지운다.
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", ""));
		if (Files.exists(path)) { // 이미지가 존재하는가?
			// 이미지 삭제
			try {
				Files.delete(path);
			} catch (IOException e) {
				logger.error("[이미지 삭제] 파일 삭제 실패. imagePath:{}", imagePath);  // Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", "")); 이게 더 정확하긴 함
				return;
			}
			
			// 폴더(디렉토리 ) 삭제
			path = path.getParent();
			if (Files.exists(path)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
					logger.error("[이미지 삭제] 폴더 삭제 실패. imagePath:{}", imagePath);
					return;
				}
			}
		}
		
	}
}
