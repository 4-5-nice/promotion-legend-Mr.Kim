package com.wanted.legendkim.domain.section;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SectionViewController {

    // 브라우저에서 GET 요청을해서 sectionMoviePage를 실행할 수 있게한다.
    @GetMapping("/admin/section/upload")
    public String sectionMoviePage() {
        return "movie/movieUpload/movieUpload";
    }

    // 사용자가 코스 & 섹션 버튼을 클릭했을 때 코스 섹션 등록 페이지로 전환
    @GetMapping("/admin/course/section")
    public String courseSectionPage() {
        return "movie/courseSection/courseSection";
    }
}
