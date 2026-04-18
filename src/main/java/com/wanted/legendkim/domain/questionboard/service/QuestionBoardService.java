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

    // DB의 날짜 정보를 문자열로 변환
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public String getMyRank(String email) {
        QuestionBoardUser user = questionBoardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // email로 사용자 정보 찾기

        // 사용자의 직급 반환
        return user.getRank().name();
    }

    public List<QuestionBoardDTO> getQuestionList(String rank, String email) {
        QuestionBoardUser user = questionBoardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // email로 사용자 정보 조회

        Rank requestedRank = Rank.valueOf(rank); // 문자열로 들어온 rank를 enum 타입 Rank로 변환
        Rank myRank = user.getRank(); // 사용자의 직급 꺼내기

        validateRankAccess(myRank, requestedRank);// 비교를 위해 전송

        // 문제 목록을 날짜순으로 조회
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
                .toList(); // 조회한 문제의 entity List에서 하나씩 빼서 DTO로 변환해서 반환
    }

    // 직급 비교해서 문제 목록을 조회
    private void validateRankAccess(Rank myRank, Rank requestedRank) {
        if (!myRank.canView(requestedRank)) {
            throw new IllegalArgumentException("해당 직급 목록을 조회할 권한이 없습니다.");
        }
    }

    // 직급으로 문제를 낼 권한을 제한(인턴은 문제를 낼 수 없다)
    public void validateWriteAccess(String email) {
        // email로 사용자 정보를 조회
        QuestionBoardUser user = questionBoardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 인턴이면 문제 못내게 제한
        if (user.getRank() == Rank.INTERN) {
            throw new IllegalArgumentException("승진하세요");
        }
    }

    // course 가져오는 기능
    public List<CourseDTO> getCourses() {
        return courseRepository.findAllByOrderByIdAsc() // 모든 강좌 조회
                .stream()
                .map(course -> new CourseDTO(
                        course.getId(),
                        course.getTitle()
                ))
                .toList();
    } // DB에서 꺼낸 course 들을 List 로 만들어서 반환

    // section 가져오는 기능
    public List<SectionDTO> getSectionsByCourse(Long courseId) {
        return sectionRepository.findByCourse_IdOrderByIdAsc(courseId) // course 아이디에 따라 section 조회
                .stream()
                .map(section -> new SectionDTO(
                        section.getId(),
                        section.getTitle()
                ))
                .toList();
    } // DB에서 꺼낸 section들을 List로 만들어서 반환

    @Transactional
    public void writeQuestion(String title, String option1, String option2, String option3,
                              String option4, String option5, Integer answer, Long courseId,
                              Long sectionId, String email
    ) {
        // 로그인 하지 않은 사용자는 문제 출제 불가
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        // 문제 내용이 없으면 출제 불가
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("문제 내용을 입력해주세요.");
        }

        // 보기 5개가 모두 채워지지 않으면 출제 불가
        if (option1 == null || option1.isBlank()
                || option2 == null || option2.isBlank()
                || option3 == null || option3.isBlank()
                || option4 == null || option4.isBlank()
                || option5 == null || option5.isBlank()) {
            throw new IllegalArgumentException("보기 5개를 모두 입력해주세요.");
        }

        // 정답을 정해놓지 않으면 출제 불가
        if (answer == null || answer < 1 || answer > 5) {
            throw new IllegalArgumentException("정답은 1번부터 5번 중 하나여야 합니다.");
        }

        // 관련 course 정하지 않으면 출제 불가
        if (courseId == null) {
            throw new IllegalArgumentException("코스를 선택해주세요.");
        }

        // 관련 section 정하지 않으면 출제 불가
        if (sectionId == null) {
            throw new IllegalArgumentException("섹션을 선택해주세요.");
        }

        // 출제가 정보 저장
        QuestionBoardUser user = questionBoardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 관련 course 정보 저장
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("코스를 찾을 수 없습니다."));

        // 관련 section 정보 저장
        Section section = sectionRepository.findByIdAndCourse_Id(sectionId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("선택한 섹션이 해당 코스에 속하지 않습니다."));

        // 정보들을 모아 question으로 저장
        Questions question = new Questions(user, course, section, title, option1, option2, option3,
                option4, option5, answer
        );

        questionBoardRepository.save(question); // 문제 등록하기
    }
}