package com.wanted.legendkim.domain.comment.commentservice;

import com.wanted.legendkim.domain.comment.dao.QuestionCommentRepository;
import com.wanted.legendkim.domain.comment.dto.QuestionCommentDTO;
import com.wanted.legendkim.domain.comment.entity.QuestionComment;
import com.wanted.legendkim.domain.questionboard.dao.QuestionBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminQuestionCommentService {

    private final QuestionCommentRepository questionCommentRepository;
    private final QuestionBoardRepository questionBoardRepository;

    private static final DateTimeFormatter COMMENT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    public List<QuestionCommentDTO> getComments(Long questionId) {
        questionBoardRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));

        return questionCommentRepository.findByQuestion_IdOrderByCreatedAtAsc(questionId)
                .stream()
                .map(comment -> new QuestionCommentDTO(
                        comment.getId(),
                        comment.getUser().getName(),
                        comment.getContent(),
                        comment.getCreatedAt().format(COMMENT_DATE_FORMATTER),
                        true
                ))
                .toList();
    }

    @Transactional
    public void deleteComment(Long questionId, Long commentId) {
        QuestionComment comment = questionCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (comment.getQuestion() == null || !comment.getQuestion().getId().equals(questionId)) {
            throw new IllegalArgumentException("해당 문제의 댓글이 아닙니다.");
        }

        questionCommentRepository.delete(comment);
    }
}