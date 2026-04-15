package com.wanted.legendkim.domain.section;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

 // HTTP 요청을 받고 JSON 형태로 응답하는 컨트롤러를 Spring에 알려주는 역할
@RestController
// sectionService를 주입해주기 위해서 사용
@RequiredArgsConstructor
// API 명세서의 앞 부분 URL
@RequestMapping("/admin/courses")
public class SectionController {

    // CourseController 가 생성되는 순간 sectionService 가 주입되고,
    // 이후에 변경되면 안되기 때문이다.
    private final SectionService sectionService;

    //API 명세서의 나머지 URL
     // API 명세서 기준 POST로 지정
    @PostMapping("/{courseId}/sections")

    /* comment.
        uploadSection의 흐름도
        1. 클라이언트 -> POST 로 req 를 보냄
        2. PostMapping -> uploadSection() 으로 연결
        3. URL 에서 courseId 3 파라미터 꺼내기
           form-data tilte, file 꺼냄
        4. sectionService.uploadSection() 호출해서 실제 처리 위임
        5. 성공 시 업로드 성공 반환
     */

    public ResponseEntity<String> uploadSection(
            @PathVariable Long courseId,
            @RequestParam("title") String title,
            @RequestParam("file")MultipartFile file) throws IOException {

        sectionService.uploadSection(courseId, title, file);
        return ResponseEntity.ok("✅업로드에 성공하셨습니다!✅");

    }


}
