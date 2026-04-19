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
        Rank requestedRank = Rank.valueOf(rank);

        List<Questions> questions = questionBoardRepository.findAllByOrderByCreatedAtDesc();

        return questions.stream()
                .filter(question -> question.getUser().getRank().isHigherThan(requestedRank))
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
                .toList();
    }

    @Transactional
    public QuestionDetailDTO getQuestionDetail(Long questionId) {
        Questions question = questionBoardRepository.findById(questionId)
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
        );
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Questions question = questionBoardRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));

        questionCommentRepository.deleteByQuestion_Id(questionId);
        questionSubmissionRepository.deleteByQuestion_Id(questionId);
        questionBoardRepository.delete(question);
    }
}