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

@RestController
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/admin/courses/{courseId}/sections")
    public ResponseEntity<List<Section>> getSectionList(@PathVariable Long courseId) {
        List<Section> sections = sectionService.getSectionList(courseId);
        return ResponseEntity.ok(sections);
    }

    @PostMapping("/admin/courses/{courseId}/sections")
    public ResponseEntity<Long> registerSection(
            @PathVariable Long courseId,
            @RequestParam("title") String title) {
        Long sectionId = sectionService.registerSection(courseId, title);
        return ResponseEntity.ok(sectionId);
    }

    @PostMapping("/admin/lectures/{lectureId}/video")
    public ResponseEntity<String> uploadVideo(
            @PathVariable Long lectureId,
            @RequestParam("courseId") Long courseId,
            @RequestParam("sectionId") Long sectionId,
            @RequestParam("file") MultipartFile file) throws IOException {
        sectionService.uploadVideo(courseId, sectionId, file);
        return ResponseEntity.ok("업로드 성공");
    }

    @GetMapping("/video/{fileName:.+}")
    public ResponseEntity<Resource> serveVideo(@PathVariable String fileName) throws MalformedURLException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp4"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
