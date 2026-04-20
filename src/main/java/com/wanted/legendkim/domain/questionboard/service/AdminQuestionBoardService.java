package com.wanted.legendkim.domain.questionboard.service;

import com.wanted.legendkim.domain.comment.dao.QuestionCommentRepository;
import com.wanted.legendkim.domain.questionboard.dao.QuestionBoardRepository;
import com.wanted.legendkim.domain.questionboard.dao.QuestionSubmissionRepository;
import com.wanted.legendkim.domain.questionboard.dto.QuestionBoardDTO;
import com.wanted.legendkim.domain.questionboard.dto.QuestionDetailDTO;
import com.wanted.legendkim.domain.questionboard.entity.Questions;
import com.wanted.legendkim.domain.questionboard.entity.Rank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminQuestionBoardService {

    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionSubmissionRepository questionSubmissionRepository;
    private final QuestionCommentRepository questionCommentRepository;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public List<QuestionBoardDTO> getQuestionList(String rank) {
        Rank requestedRank = Rank.valueOf(rank); //문자열 rank를 enum 값으로 바꾸는 코드

        List<Questions> questions = questionBoardRepository.findAllByOrderByCreatedAtDesc();
        // 모든 문제를 날짜순으로 조회

        return questions.stream() // stream으로 하나씩 변환할 준비
                .filter(question -> question.getUser().getRank().isHigherThan(requestedRank))
                // 조건에 맞는 문제만 남기고 나머지는 제외하는 filter
                .map(question -> new QuestionBoardDTO(
                        question.getId(),
                        question.getTitle(),
                        question.getCourse().getTitle(),
                        question.getSection().getTitle(),
                        question.getUser().getName(),
                        question.getUser().getRank().getLabel(),
                        question.getCreatedAt().format(DATE_FORMATTER),
                        false
                ))
                .toList(); // DTO로 변환하여 반환
    }

    @Transactional
    public QuestionDetailDTO getQuestionDetail(Long questionId) {
        Questions question = questionBoardRepository.findById(questionId) // 문제 아이디로 문제 정보 찾기
                .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));

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
                true,
                null,
                null
        ); // 문제 정보를 DTO 값으로 반환
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Questions question = questionBoardRepository.findById(questionId) // 문제 아이디로 문제 정보 조회
                .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));

        questionCommentRepository.deleteByQuestion_Id(questionId); // 문제 아이디로 찾은 문제의 댓글을 삭제
        questionSubmissionRepository.deleteByQuestion_Id(questionId); // 문제 아이디로 찾은 문제 제출 내역 삭제
        questionBoardRepository.delete(question); // 문제 삭제
    }
}