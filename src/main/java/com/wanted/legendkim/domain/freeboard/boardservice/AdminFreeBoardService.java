package com.wanted.legendkim.domain.freeboard.boardservice;

import com.wanted.legendkim.domain.comment.dao.FreeCommentRepository;
import com.wanted.legendkim.domain.freeboard.dao.FreeBoardPostRepository;
import com.wanted.legendkim.domain.freeboard.dto.FreeBoardDTO;
import com.wanted.legendkim.domain.freeboard.dto.FreeBoardDetailDTO;
import com.wanted.legendkim.domain.freeboard.entity.FreeBoardPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminFreeBoardService {

    private final FreeBoardPostRepository freeBoardPostRepository;
    private final FreeCommentRepository freeCommentRepository;

    // DB의 날짜시간 정보를 지정한 형식으로 변환시켜준다.
    private static final DateTimeFormatter DETAIL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    public List<FreeBoardDTO> getAdminPosts() {
        // 모든 게시글을 날짜순으로 조회
        return freeBoardPostRepository.findAllWithUserOrderByCreatedAtDesc()
                .stream()
                .map(post -> new FreeBoardDTO(
                        post.getId(),
                        post.getTitle(),
                        post.getUser().getName(),
                        post.getViewCount(),
                        post.getCreatedAt().format(DETAIL_DATE_FORMATTER)
                ))
                .toList(); // 엔티티 값을 하나씩 DTO로 만들어서 반환
    }

    public FreeBoardDetailDTO getAdminPostDetail(Long postId) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId) // 게시글 아이디로 게시글 정보 찾기
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        return new FreeBoardDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getName(),
                post.getViewCount(),
                post.getCreatedAt().format(DETAIL_DATE_FORMATTER),
                false
        ); // 게시글 정보를 DTO값으로 반환
    }

    @Transactional
    public void deletePostByAdmin(Long postId) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId) // 게시글 아이디로 게시글 정보 찾기
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        freeCommentRepository.deleteByPostId(postId); // 해당 게시글 아이디에 있는 댓글 삭제
        freeBoardPostRepository.delete(post); // 게시글 삭제
    }
}