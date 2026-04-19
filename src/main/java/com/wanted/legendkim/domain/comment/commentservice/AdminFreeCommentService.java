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
        return freeCommentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(comment -> new FreeCommentDTO(
                        comment.getId(),
                        comment.getUser().getName(),
                        comment.getContent(),
                        comment.getCreatedAt().format(COMMENT_DATE_FORMATTER),
                        false
                ))
                .toList();
    }

    @Transactional
    public Long deleteCommentByAdmin(Long commentId) {
        FreeComment comment = freeCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        Long postId = comment.getPost().getId();

        freeCommentRepository.delete(comment);

        return postId;
    }
}