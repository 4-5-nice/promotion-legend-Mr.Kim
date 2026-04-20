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
        questionBoardRepository.findById(questionId) // 문제 아이디로 문제 정보 찾기
                .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));

        // 문제 아이디로 불러온 해당 문제의 댓글을 날짜순으로 조회
        return questionCommentRepository.findByQuestion_IdOrderByCreatedAtAsc(questionId)
                .stream()
                .map(comment -> new QuestionCommentDTO(
                        comment.getId(),
                        comment.getUser().getName(),
                        comment.getContent(),
                        comment.getCreatedAt().format(COMMENT_DATE_FORMATTER),
                        true
                ))
                .toList(); // 하나씩 뽑아서 DTO로 만들어서 반환
    }

    @Transactional
    public void deleteComment(Long commentId) {
        QuestionComment comment = questionCommentRepository.findById(commentId) // 댓글 아이디로 댓글 정보 찾기
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        questionCommentRepository.delete(comment); // 댓글 삭제
    }
}