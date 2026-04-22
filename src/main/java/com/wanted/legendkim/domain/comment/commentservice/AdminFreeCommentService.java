package com.wanted.legendkim.domain.comment.commentservice;

import com.wanted.legendkim.domain.comment.dao.FreeCommentRepository;
import com.wanted.legendkim.domain.comment.dto.FreeCommentDTO;
import com.wanted.legendkim.domain.comment.entity.FreeComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminFreeCommentService {

    private final FreeCommentRepository freeCommentRepository;

    private static final DateTimeFormatter COMMENT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    public List<FreeCommentDTO> getComments(Long postId) {
        // 게시글 아이디로 찾은 댓글들을 날짜순으로 찾기
        return freeCommentRepository.findByPostIdWithUserOrderByCreatedAtAsc(postId)
                .stream()
                .map(comment -> new FreeCommentDTO(
                        comment.getId(),
                        comment.getUser().getName(),
                        comment.getContent(),
                        comment.getCreatedAt().format(COMMENT_DATE_FORMATTER),
                        false
                ))
                .toList(); // 하나씩 DTO로 만들어서 반환
    }

    @Transactional
    public Long deleteCommentByAdmin(Long commentId) {
        FreeComment comment = freeCommentRepository.findById(commentId) // 댓글 아이디로 댓글 정보 찾기
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        Long postId = comment.getPost().getId(); // 댓글이 달린 게시글의 아이디 추출

        freeCommentRepository.delete(comment); // 댓글 삭제

        return postId;
    }
}