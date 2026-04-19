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

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private static final DateTimeFormatter DETAIL_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    public List<FreeBoardDTO> getAdminPosts() {
        return freeBoardPostRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(post -> new FreeBoardDTO(
                        post.getId(),
                        post.getTitle(),
                        post.getUser().getName(),
                        post.getViewCount(),
                        post.getCreatedAt().toLocalDate().toString()
                ))
                .toList();
    }

    public FreeBoardDetailDTO getAdminPostDetail(Long postId) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        return new FreeBoardDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getName(),
                post.getViewCount(),
                post.getCreatedAt().format(DETAIL_DATE_FORMATTER),
                false
        );
    }

    @Transactional
    public void deletePostByAdmin(Long postId) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        freeCommentRepository.deleteByPostId(postId);
        freeBoardPostRepository.delete(post);
    }
}