package com.wanted.legendkim.domain.section;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SectionViewController {

    @GetMapping("/admin/section/upload")
    public String sectionMoviePage() {
        return "movie/movieUpload/movieUpload";
    }
}
