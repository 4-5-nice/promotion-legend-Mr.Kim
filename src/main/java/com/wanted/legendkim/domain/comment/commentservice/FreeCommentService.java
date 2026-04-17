package com.wanted.legendkim.domain.comment.commentservice;

import com.wanted.legendkim.domain.freeboard.dao.FreeBoardUserRepository;
import com.wanted.legendkim.domain.freeboard.dao.FreeBoardPostRepository;
import com.wanted.legendkim.domain.freeboard.entity.FreeBoardUser;
import com.wanted.legendkim.domain.freeboard.entity.FreeBoardPost;
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
public class FreeCommentService {

    private final FreeCommentRepository freeCommentRepository;
    private final FreeBoardPostRepository postRepository;
    private final FreeBoardUserRepository boardUserRepository;

    private static final DateTimeFormatter COMMENT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    @Transactional
    public void writeComment(Long postId, String content, String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }

        FreeBoardPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        FreeBoardUser user = boardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        FreeComment freeComment = new FreeComment(post, user, content);

        freeCommentRepository.save(freeComment);
    }

    public List<FreeCommentDTO> getComments(Long postId) {
        return freeCommentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(comment -> new FreeCommentDTO(
                        comment.getId(),
                        comment.getUser().getName(),
                        comment.getContent(),
                        comment.getCreatedAt().format(COMMENT_DATE_FORMATTER)
                ))
                .toList();
    }
}
