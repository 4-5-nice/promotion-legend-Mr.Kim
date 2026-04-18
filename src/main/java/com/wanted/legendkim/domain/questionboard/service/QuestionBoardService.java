package com.wanted.legendkim.domain.questionboard.service;

import com.wanted.legendkim.domain.comment.dao.QuestionCommentRepository;
import com.wanted.legendkim.domain.questionboard.dao.*;
import com.wanted.legendkim.domain.questionboard.dto.*;
import com.wanted.legendkim.domain.questionboard.entity.*;
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
    private final QuestionSubmissionRepository questionSubmissionRepository;

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
                .map(question -> {
                    boolean solved = questionSubmissionRepository.existsByQuestion_IdAndUser_Id(question.getId(), user.getId());

                    return new QuestionBoardDTO(
                            question.getId(),
                            question.getTitle(),
                            question.getCourse().getTitle(),
                            question.getSection().getTitle(),
                            question.getUser().getName(),
                            question.getUser().getRank().getLabel(),
                            question.getCreatedAt().format(DATE_FORMATTER),
                            solved
                    );
                })
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

    @Transactional
    public QuestionDetailDTO getQuestionDetail(Long questionId, String email) {

        // 로그인 한 사용자 정보를 이메일로 찾기
        QuestionBoardUser user = questionBoardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 문제 아이디로 문제 정보 찾기
        Questions question = questionBoardRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));

        // 문제 상세 조회 권한 증명하기
        validateQuestionAccess(user.getRank(), question);

        QuestionSubmission submission = questionSubmissionRepository
                .findByQuestion_IdAndUser_Id(questionId, user.getId()).orElse(null);
        // 문제 아이디로 찾은 문제에 대해서 유저 아이디로 찾은 유저가 푼 이력을 찾기. 없으면 null

        boolean solved = submission != null; // submission에 값이 있으면 true, null이면 false
        Integer myAnswer = solved ? submission.getSelectedAnswer() : null;
        // solved가 true면 submission에 저장된 내가 고른 답을 가져온다. 아니면 null

        Boolean correct = solved ? submission.getIsCorrect() : null;
        // solved가 true면 submission에 저장된 나의 정답 유무를 가져온다. 아니면 null

        return new QuestionDetailDTO(
                question.getId(),
                question.getTitle(),
                question.getOption1(),
                question.getOption2(),
                question.getOption3(),
                question.getOption4(),
                question.getOption5(),
                question.getAnswer(),
                question.getUser().getName(),
                question.getUser().getRank().getLabel(),
                question.getCreatedAt().format(DATE_FORMATTER),
                question.getViewCount(),
                question.getCourse().getTitle(),
                question.getSection().getTitle(),
                solved,
                myAnswer,
                correct
        ); // entity 값들과 앞에서 만든 값들을 DTO로 만들어서 반환
    }

    private void validateQuestionAccess(Rank myRank, Questions question) {
        Rank authorRank = question.getUser().getRank(); // 문제 출제자의 직급을 가져오기

        // 사용자보다 상위의 직급이 낸 문제가 아니면 볼 수 없게 했다.
        if (!authorRank.isHigherThan(myRank)) {
            throw new IllegalArgumentException("하등한 것들한테 관심을 주지 말자. 우리가 바라볼 것은 오직 위다!");
        }
    }

    @Transactional
    public QuestionSolveResponseDTO solveQuestion(Long questionId, Integer selectedAnswer, String email) {
        if (selectedAnswer == null || selectedAnswer < 1 || selectedAnswer > 5) {
            throw new IllegalArgumentException("정답은 1번부터 5번 중 하나를 선택해야 합니다.");
        } // 만일 답을 선택하지 않았을 경우 띄우는 알림

        // 이메일로 사용자 정보 찾기
        QuestionBoardUser user = questionBoardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 문제 아이디로 문제 정보 찾기
        Questions question = questionBoardRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));

        boolean correct = question.getAnswer().equals(selectedAnswer);
        // 위에서 가져온 문제의 정보에서 답을 가져와서 사용자가 고른 답과 비교하여 같으면 true 아니면 false

        QuestionSubmission submission = new QuestionSubmission(
                question,
                user,
                selectedAnswer,
                correct
        ); // 위에서 도출한 정보들을 모아서 문제 제출 객체 생성

        questionSubmissionRepository.save(submission); // 그걸 persistence context에 연결

        return new QuestionSolveResponseDTO(
                correct,
                question.getAnswer(),
                selectedAnswer
        ); // 정답 유무와 정답과 고른 답을 DTO로 만들어서 반환
    }
}