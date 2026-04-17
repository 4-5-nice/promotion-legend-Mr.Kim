package com.wanted.legendkim.domain.questionboard.service;

import com.wanted.legendkim.domain.questionboard.dao.CourseRepository;
import com.wanted.legendkim.domain.questionboard.dao.QuestionBoardRepository;
import com.wanted.legendkim.domain.questionboard.dao.QuestionBoardUserRepository;
import com.wanted.legendkim.domain.questionboard.dao.SectionRepository;
import com.wanted.legendkim.domain.questionboard.dto.CourseDTO;
import com.wanted.legendkim.domain.questionboard.dto.QuestionBoardDTO;
import com.wanted.legendkim.domain.questionboard.dto.SectionDTO;
import com.wanted.legendkim.domain.questionboard.entity.Course;
import com.wanted.legendkim.domain.questionboard.entity.QuestionBoardUser;
import com.wanted.legendkim.domain.questionboard.entity.Questions;
import com.wanted.legendkim.domain.questionboard.entity.Rank;
import com.wanted.legendkim.domain.questionboard.entity.Section;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionBoardService {

    private final QuestionBoardUserRepository questionBoardUserRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public String getMyRank(String email) {
        QuestionBoardUser user = questionBoardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return user.getRank().name();
    }

    public List<QuestionBoardDTO> getQuestionList(String rank, String email) {
        QuestionBoardUser loginUser = questionBoardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Rank requestedRank = Rank.valueOf(rank);
        Rank myRank = loginUser.getRank();

        validateRankAccess(myRank, requestedRank);

        List<Questions> questions = questionBoardRepository.findAllByOrderByCreatedAtDesc();

        return questions.stream()
                .filter(question -> question.getUser().getRank().isHigherThan(requestedRank))
                .map(question -> new QuestionBoardDTO(
                        question.getId(),
                        question.getTitle(),
                        question.getUser().getName(),
                        question.getUser().getRank().getLabel(),
                        question.getCreatedAt().format(DATE_FORMATTER),
                        false
                ))
                .toList();
    }

    private void validateRankAccess(Rank myRank, Rank requestedRank) {
        if (!myRank.canView(requestedRank)) {
            throw new IllegalArgumentException("해당 직급 목록을 조회할 권한이 없습니다.");
        }
    }

    public List<CourseDTO> getCourses() {
        return courseRepository.findAllByOrderByIdAsc()
                .stream()
                .map(course -> new CourseDTO(
                        course.getId(),
                        course.getTitle()
                ))
                .toList();
    }

    public List<SectionDTO> getSectionsByCourse(Long courseId) {
        return sectionRepository.findByCourse_IdOrderByIdAsc(courseId)
                .stream()
                .map(section -> new SectionDTO(
                        section.getId(),
                        section.getTitle()
                ))
                .toList();
    }

    @Transactional
    public void writeQuestion(String title, String option1, String option2, String option3,
                              String option4, String option5, Integer answer, Long courseId,
                              Long sectionId, String email
    ) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("문제 내용을 입력해주세요.");
        }

        if (option1 == null || option1.isBlank()
                || option2 == null || option2.isBlank()
                || option3 == null || option3.isBlank()
                || option4 == null || option4.isBlank()
                || option5 == null || option5.isBlank()) {
            throw new IllegalArgumentException("보기 5개를 모두 입력해주세요.");
        }

        if (answer == null || answer < 1 || answer > 5) {
            throw new IllegalArgumentException("정답은 1번부터 5번 중 하나여야 합니다.");
        }

        if (courseId == null) {
            throw new IllegalArgumentException("코스를 선택해주세요.");
        }

        if (sectionId == null) {
            throw new IllegalArgumentException("섹션을 선택해주세요.");
        }

        QuestionBoardUser user = questionBoardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스를 찾을 수 없습니다."));

        Section section = sectionRepository.findByIdAndCourse_Id(sectionId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("선택한 섹션이 해당 코스에 속하지 않습니다."));

        Questions question = new Questions(user, course, section, title, option1, option2, option3,
                option4, option5, answer
        );

        questionBoardRepository.save(question);
    }
}