package com.wanted.legendkim.domain.section;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// HTTP 요청을 받고 JSON 형태로 응답하는 컨트롤러 Spring 에 알려주는 역할
@RestController
// final 필드를 생성자 주입하는 역할
@RequiredArgsConstructor
public class SectionController {

    // 의존성이 변경되면 안되기 때문에 final 선언
    private final SectionService sectionService;

    /* comment.
        섹션 등록
        - 리팩터링을 하기 전에는 섹션 등록 + 영상 업로드를 하나의 API 에서 처리.
        - 하지만 리팩토링을 통해 섹션 등록은 title만 받도록 분리.
        - MultipartFile, 예외 처리를 하지 않으며 파일을 처리하지 않는다.
        *
        반환값 변경
        - 기존 : ResponseEntity<String> → 성공 문자열 반환
        - 변경 : ResponseEntity<Long>   → sectionId 반환
        - 이유 : FE에서 2단계 영상 업로드 API 호출 시 sectionId 가 필요하기 때문
     */
    @GetMapping("/admin/courses/{courseId}/sections")
    public ResponseEntity<List<Section>> getSectionList(@PathVariable Long courseId) {
        List<Section> sections = sectionService.getSectionList(courseId);
        return ResponseEntity.ok(sections);
    }

    @PostMapping("/admin/courses/{courseId}/sections")
    public ResponseEntity<Long> registerSection(
            // URL 경로에서 courseId 추출
            @PathVariable Long courseId,
            // 폼데이터에서 title 로 추출
            @RequestParam("title") String title) {

        // SectionService 에서 Long 타입으로 맞춰줬기 때문에 같은 자료형으로 유지
        Long sectionId = sectionService.registerSection(courseId, title);
        return ResponseEntity.ok(sectionId);
    }

    /* comment.
        영상 업로드
        - Section 등록 과 영상 업로드를 분리하며 새롭게 생성한 코드 로직
     */
    @PostMapping("/admin/lectures/{lectureId}/video")
    public ResponseEntity<String> uploadVideo(
            /* comment.
                "lectureId" 는 API 명세서의 URL을 맞추기 위한 Path Variable 이다.
                현재 비즈니스 로직에서는 사용되지 않고, Lectures 테이블 없고
                Sections 테이블에 저장하게 된다. 추후 영상 수강 페이지 구현 시
                재검토 필요.
             */
            @PathVariable Long lectureId,
            // course 존재 여부 확인용
            @RequestParam("courseId") Long courseId,
            // 실제로 영상을 저장할 섹션 지정
            @RequestParam("sectionId") Long sectionId,
            // 업로드할 영상 파일
            @RequestParam("file") MultipartFile file) throws IOException {

        // lectureId 는 현재 서비스 로직에서 사용하지 않음
        // 실제 처리는 courseId 와 sectionId 로 진행
        sectionService.uploadVideo(courseId, sectionId, file);
        return ResponseEntity.ok("✅ 영상 업로드에 성공하셨습니다!");
    }
}
