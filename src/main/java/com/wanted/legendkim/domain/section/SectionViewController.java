package com.wanted.legendkim.domain.section;

import com.wanted.legendkim.domain.users.auth.model.dto.AuthDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SectionViewController {

    // 브라우저에서 GET 요청을해서 sectionMoviePage를 실행할 수 있게한다.
    @GetMapping("/admin/section/upload")
    public String sectionMoviePage(@AuthenticationPrincipal AuthDetails authDetails, Model model) {
        if (authDetails != null) {
            model.addAttribute("loginUser", authDetails);
        }
        return "movie/movieUpload/movieUpload";
    }

    // 사용자가 코스 & 섹션 버튼을 클릭했을 때 코스 섹션 등록 페이지로 전환
    @GetMapping("/admin/course/section")
    public String courseSectionPage(@AuthenticationPrincipal AuthDetails authDetails, Model model) {
        if (authDetails != null) {
            model.addAttribute("loginUser", authDetails);
        }
        return "movie/courseSection/courseSection";
    }

    // 수강신청 페이지
    @GetMapping("/user/enrollments/list")
    public String enrollmentListPage() {
        return "enrollment/enrollmentList";
    }
}
