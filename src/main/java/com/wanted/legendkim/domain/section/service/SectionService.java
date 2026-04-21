package com.wanted.legendkim.domain.section.service;

import com.wanted.legendkim.domain.course.entity.Course;
import com.wanted.legendkim.domain.course.dao.CourseRepository;
import com.wanted.legendkim.domain.section.dao.SectionRepository;
import com.wanted.legendkim.domain.section.entity.Section;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

// Spring 에서 이 클래스를 서비스 레이어로 인식
@Service
// final 필드 생성자 주입을 위해 사용
@RequiredArgsConstructor
public class SectionService {

    // final 선언을 통해 의존성이 바뀌지 않게 설정
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;

    // application.yml 의 file.upload-dir 값을 가져온다.
    // 경로가 바뀌더라도 yml 파일만 수정하면 된다.
    @Value("${file.upload-dir}")
    private String uploadDir;

    /* comment.
        섹션 등록
        - 기존에는 섹션 등록과 파일 업로드를 하나의 메서드에서 처리했으나,
        - 코드의 유지보수를 위해 섹션 등록과 파일 업로드를 분리했다.
        *
        반환값 변경
        - 기존 : void
        - 변경 : Long (sectionId)
        - 이유 : FE 에서 섹션 등록 후 sectionId 를 받아 영상 업로드 API 를 호출해야 하기 때문
     */
    // 영상 업로드 API 가 sectionId를 필요로 하기 때문에 Long 타입으로 반환한다.
    public List<Section> getSectionList(Long courseId) {
        return sectionRepository.findByCourseId(courseId);
    }

    public Long registerSection(Long courseId, String title, String note) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코스입니다."));

        Section section = Section.create(course, title, note);
        sectionRepository.save(section);

        return section.getId();
    }

    /* comment.
        영상 업로드 메서드
        - 기존 Section 과 업로드를 하나의 메서드에서 처리했지만,
        - 아래의 코드 블럭은 영상을 업로드하는 역할만 한다.
     */
    public void uploadVideo(Long courseId, Long sectionId, MultipartFile file) throws IOException {

        // 1. Course 존재 여부 확인 - 없는 코스의 섹션에 영상이 올라가는 것을 방지한다.
        courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코스입니다."));

        // 2. Section 존재 여부 확인 - 없는 섹션에 영상이 올라가는 것을 방지한다.
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 섹션입니다."));

        // 3. 저장 디렉토리 생성
        // Files.createDirectories() 는 존재 여부 체크 + 생성을 한번에 처리한다.
        Path dirPath = Paths.get(uploadDir);
        Files.createDirectories(dirPath);

        // 4. 파일명을 UUID만으로 구성 (한글, 공백, 특수문자 전부 제거)
        String extension = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            extension = original.substring(original.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID() + extension;

        // 5. 파일 저장
        Path filePath = dirPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        // 6. Section 에 videoUrl 업데이트 후 DB 저장
        // uploadVideo() 내부에서 videoUrl 저장 + uploadSuccess = true 처리
        section.uploadVideo(fileName);
        sectionRepository.save(section);
    }
}
