package com.wanted.legendkim.domain.comment.commentservice;

import com.wanted.legendkim.domain.questionboard.dao.QuestionBoardRepository;
import com.wanted.legendkim.domain.questionboard.dao.QuestionBoardUserRepository;
import com.wanted.legendkim.domain.comment.dao.QuestionCommentRepository;
import com.wanted.legendkim.domain.questionboard.dao.QuestionSubmissionRepository;
import com.wanted.legendkim.domain.comment.dto.QuestionCommentDTO;
import com.wanted.legendkim.domain.questionboard.entity.QuestionBoardUser;
import com.wanted.legendkim.domain.comment.entity.QuestionComment;
import com.wanted.legendkim.domain.questionboard.entity.Questions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionCommentService {

    private final QuestionCommentRepository questionCommentRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionBoardUserRepository questionBoardUserRepository;
    private final QuestionSubmissionRepository questionSubmissionRepository;

    // DB에 저장된 시간 값을 화면에 띄우기 위해 문자열로 변환
    private static final DateTimeFormatter COMMENT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    public List<QuestionCommentDTO> getComments(Long questionId, String email) {
        QuestionBoardUser user = questionBoardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // email을 이용해서 사용자의 정보 찾기

        boolean solved = questionSubmissionRepository.existsByQuestion_IdAndUser_Id(questionId, user.getId());
        // 이 문제 아이디로 찾은 문제를 현재 사용자 아이디로 찾은 유저가 푼 데이터가 있는가
        // 있으면 true 없으면 false

        if (!solved) { // false라면?
            throw new IllegalArgumentException("문제를 풀어야 댓글을 볼 수 있습니다.");
        }

        // true 라면?                    문제 아이디로 찾은 댓글들을 날짜순으로 찾기
        return questionCommentRepository.findByQuestion_IdOrderByCreatedAtAsc(questionId)
                .stream()
                .map(comment -> new QuestionCommentDTO(
                        comment.getId(),
                        comment.getUser().getName(),
                        comment.getContent(),
                        comment.getCreatedAt().format(COMMENT_DATE_FORMATTER),
                        email != null && comment.getUser().getEmail().equals(email)
                ))
                .toList(); // entity에서 찾은 댓글들의 정보를 DTO로 만들어서 반환
    }

    @Transactional
    public void writeComment(Long questionId, String content, String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        } // 이메일이 비어있으면 로그인을 안했다는 뜻

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        } // 댓글 내용이 없으면 등록 불가

                                 // email을 이용해서 작성자 정보 찾기
        QuestionBoardUser user = questionBoardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

                                 // 문제 아이디를 이용해서 문제 정보 찾기
        Questions question = questionBoardRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다."));

        QuestionComment comment = new QuestionComment(
                question,
                user,
                content.trim() // trim으로 문자열 앞 뒤 공백 제거.
        ); // 문제, 작성자, 내용을 모아서 객체 생성.

        questionCommentRepository.save(comment); // 만든 댓글 객체를 persistence context에 연결
    }

    @Transactional
    public Long editComment(Long commentId, String content, String email) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        } // 댓글이 없으면 수정 불가

        QuestionComment comment = questionCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        // 댓글 아이디로 댓글 정보 찾기

        validateCommentWriter(comment, email); // 자기가 쓴 댓글만 수정 가능하게 제한

        comment.modify(content.trim()); // 댓글 수정하기. trim으로 앞뒤 빈칸 없애기

        return comment.getQuestion().getId(); // 그 댓글이 달린 게시물과 댓글 아이디 정보 반환
    }

    @Transactional
    public Long deleteComment(Long commentId, String email) {
        QuestionComment comment = questionCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        // 댓글 아이디로 댓글 정보 찾기

        validateCommentWriter(comment, email); // 작성자가 아니면 삭제 불가하게 제한

        Long postId = comment.getQuestion().getId(); // 댓글이 달린 게시물과 댓글 아이디 정보 저장

        questionCommentRepository.delete(comment); // persistence context에서 삭제

        return postId; // 아까 저장한 정보들 반환
    }

    private void validateCommentWriter(QuestionComment comment, String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        } // 로그인 하지 않으면 불가

        if (!comment.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("작성자만 할 수 있습니다.");
        } // 작성자가 아니면 불가
    }
}
