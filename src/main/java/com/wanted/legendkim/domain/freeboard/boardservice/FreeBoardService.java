package com.wanted.legendkim.domain.freeboard.boardservice;

import com.wanted.legendkim.domain.freeboard.dao.FreeBoardPostRepository;
import com.wanted.legendkim.domain.freeboard.dao.FreeBoardUserRepository;
import com.wanted.legendkim.domain.freeboard.dto.FreeBoardDTO;
import com.wanted.legendkim.domain.freeboard.dto.FreeBoardDetailDTO;
import com.wanted.legendkim.domain.freeboard.entity.FreeBoardPost;
import com.wanted.legendkim.domain.freeboard.entity.FreeBoardUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FreeBoardService {

    private final FreeBoardPostRepository freeBoardPostRepository;
    private final FreeBoardUserRepository freeBoardUserRepository;

    public List<FreeBoardDTO> getPosts(String filter, String email) {
        List<FreeBoardPost> posts;

        if ("mine".equalsIgnoreCase(filter)) {
            FreeBoardUser user = freeBoardUserRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            posts = freeBoardPostRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        } else {
            posts = freeBoardPostRepository.findAllByOrderByCreatedAtDesc();
        }

        return posts.stream()
                .map(post -> new FreeBoardDTO(
                        post.getId(),
                        post.getTitle(),
                        post.getUser().getName(),
                        post.getViewCount(),
                        post.getCreatedAt().toLocalDate().toString()
                ))
                .toList();
    }

    @Transactional
    public FreeBoardDetailDTO getPostDetail(Long postId, String email) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        post.increaseViewCount();

        boolean mine = email != null
                && post.getUser().getEmail().equals(email);

        return new FreeBoardDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getName(),
                post.getViewCount(),
                post.getCreatedAt().toLocalDate().toString(),
                mine
        );
    }

    @Transactional
    public void writePost(String title, String content, String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        FreeBoardUser user = freeBoardUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        FreeBoardPost post = new FreeBoardPost(user, title, content);

        freeBoardPostRepository.save(post);
    }
}