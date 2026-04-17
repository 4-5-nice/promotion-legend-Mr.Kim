package com.wanted.legendkim.domain.section;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

// HTML이 아닌 JSON 같은 데이터를 반환하는 API 컨트롤러임을 선언
@RestController
// 생성자 주입
@RequiredArgsConstructor
public class SectionController {

    // 의존성이 변경되지 않게 final 선언
    private final SectionService sectionService;

    // file.upload-dir 의 값을 읽어와서 아래 변수에 넣는다.
    @Value("${file.upload-dir}")
    private String uploadDir; // 로컬 서버 영상 저장 경로

    /* comment.
        Section 목록 조회
        기능 : 특정 Course 에 속한 전체 섹션 목록 조회
        1. SectionService 를 호출하여 특정 강의에 매핑된 전체
        섹션 데이터를 가져온다.
        2. 조회된 섹션 리스트를 HTTP 상태 코드와 함께 클라이언트로 반환한다.
     */

    @GetMapping("/admin/courses/{courseId}/sections")
    // @PathVariable : URL 의 {courseId} 부분에 들어온 숫자를 Long 타입 변수로 뺀다.
    public ResponseEntity<List<Section>> getSectionList(@PathVariable Long courseId) {
        // SectionService 에서 비즈니스 로직을 위임
        List<Section> sections = sectionService.getSectionList(courseId);
        // HTTP의 상태와 전달할 메시지
        return ResponseEntity.ok(sections);
    }

    /* comment.
        섹션 생성 로직
        기능 : 특정 강의에 새로운 Section 추가
        1. user 한테 받은 강의 ID 와 새로운 제목을 SectionController
        한테 넘겨 DB 에 저장한다.
        2. 성공적으로 생성되면, 새로 부여된 해당 섹션의 PK 를 반환한다.
     */
    // 새로운 리소스 생성
    @PostMapping("/admin/courses/{courseId}/sections")
    public ResponseEntity<Long> registerSection(
            @PathVariable Long courseId, // URL 경로에서 추출
            // 쿼리 파라미터 또는 폼 데이터에서 값을 추출한다.
            @RequestParam("title") String title) {
        Long sectionId = sectionService.registerSection(courseId, title);
        return ResponseEntity.ok(sectionId); // 생성된 PK를 반환해준다.
    }

    /* comment.
        동영상 업로드 로직
        Lecture 에 맵핑할 동영상 파일을 서버에 업로드
        1. 전달받은 동영상 파일과 식별자들을 서비스 계층에 넘겨 실제
        서버 디렉토리에 파일을 물리적으로 저장한다.
        2. 저장이 완료되면 지정한 텍스트 메세지 응답
     */

    @PostMapping("/admin/lectures/{lectureId}/video")
    public ResponseEntity<String> uploadVideo(
            // 현재 로직에서는 사용되지 않았지만, 우리는 API 명세서에 작성한
            // 요구사항대로 일단은 넣었다.
            @PathVariable Long lectureId,
            @RequestParam("courseId") Long courseId,
            @RequestParam("sectionId") Long sectionId,
            // MultipartFile : Spring 에서 파일 업로드 처리시 사용하는 메소드
            @RequestParam("file") MultipartFile file) throws IOException {
        // 실제 파일 저장 로직은 서비스 레이어로 전환
        sectionService.uploadVideo(courseId, sectionId, file);
        return ResponseEntity.ok("업로드 성공");
    }

    /* comment
        동영상 다운로드 로직
        서버에 저장된 동영상 파일을 브라우저에서 스트리밍
        1. 서버의 로컬 저장소 경로와 파일명을 조합해 실제 파일을 찾아낸다.
        2. user 가 파일을 다운하지 않고 웹에서 스트리밍할 수 있게 타입까지 설정한다.
     */
    // Spring 에서는 기본적으로 URL 마지막 부분에 '.mp4' 같은 확장자를 무시하는 경향이 있다.
    // 이를 위해 우리는 정규식을 작성해 확장자까지 온전하게 fileName에 담아준다.
    @GetMapping("/video/{fileName:.+}")
    public ResponseEntity<Resource> serveVideo(@PathVariable String fileName) throws MalformedURLException {
        // 안전하게 directory 에 경로와 파일 이름을 합쳐서 절대 경로를 만들어낸다.
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        // 로컬 파일 시스템에 있는 파일을 Spring 에서 전송할 수 있는 리소스 객체로 변환한다.
        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                // HTTP 에게 mp4 영상 파일을 명시
                .contentType(MediaType.parseMediaType("video/mp4"))
                // 브라우저에서 재생할 수 있게 유도한다.
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
