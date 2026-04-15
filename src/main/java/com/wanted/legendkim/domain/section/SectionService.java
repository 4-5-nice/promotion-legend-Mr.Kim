package com.wanted.legendkim.domain.section;

import com.wanted.legendkim.domain.course.Course;
import com.wanted.legendkim.domain.course.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

// Spring 에서 이 클래스를 서비스 레이어로 인식
@Service
// final 필드를 생성자 주입으로 자동 처리
@RequiredArgsConstructor
public class SectionService {

    // final 선언 이유 : 한번 주입된 의존성이 바뀌어서는 안되기 때문이다.
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;

    // application.yml file.upload.dir 의 값을 가져옴
    // 이를 통해 경로가 바뀌더라도, yml 파일만 수정하면 된다.
    @Value("${file.upload-dir}")
    private String uploadDir;


    public void uploadSection(Long courseId, String title, MultipartFile file) throws IOException {

        // 1. Course 존재 여부 확인
        // 없는 코스의 섹션에 올리는 것을 방지한다.
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코스입니다."));

        // 2. 저장 디렉토리 생성
        // 폴더가 없으면 생성하게 된다.
        /* comment.
            ** 폴더가 없으면 생성하는데, 왜? if 문이 필요없을까? **
            Files.createDirectories() 는 존재 여부를 체크 + 생성을 한번에 진행한다.
            if문은 mkdir() 같은 구식 방식에 사용할 때 필요하기 때문에 여기서는 사용 X
         */
        Path dirPath = Paths.get(uploadDir);
        Files.createDirectories(dirPath);

        // 3. 파일명 UUID로 변환 (충돌 방지)
        // 같은 이름의 파일이 올라올 경우 충돌을 방지한다.
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 4. 파일 저장
        Path filePath = dirPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        // 5. Section 생성 및 DB 저장
        Section section = Section.create(course, title, fileName);
        sectionRepository.save(section);
    }

}
