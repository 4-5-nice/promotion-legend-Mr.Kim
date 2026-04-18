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
    // DB의 날짜를 화면에 보이기 위해서 문자열로 바꿔야한다.

    // 댓글 작성
    @Transactional
    public void writeComment(Long postId, String content, String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        } // 로그인 하지 않으면 작성 못함

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        } // 내용이 없으면 등록 못함

        // 게시글 아이디로 게시글 찾기
        FreeBoardPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 작성자 email로 작성자 찾기
        FreeBoardUser user = boardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 엔티티에 댓글을 달 게시물과 댓글 작성자, 댓글 내용을 저장
        FreeComment freeComment = new FreeComment(post, user, content);

        // persistence context에 연결
        freeCommentRepository.save(freeComment);
    }

    // 댓글 가져오기
    public List<FreeCommentDTO> getComments(Long postId) {
        return freeCommentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                                  // 게시글 아이디로 댓글들을 날짜순으로 조회
                .stream()
                .map(comment -> new FreeCommentDTO(
                        comment.getId(),
                        comment.getUser().getName(),
                        comment.getContent(),
                        comment.getCreatedAt().format(COMMENT_DATE_FORMATTER)
                ))
                .toList();
    } // entity를 하나씩 빼서 FreeCommentDTO 리스트로 바꿔서 반환
}
