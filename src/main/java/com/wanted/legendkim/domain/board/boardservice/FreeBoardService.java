package com.wanted.legendkim.domain.board.boardservice;

import com.wanted.legendkim.domain.board.dao.PostRepository;
import com.wanted.legendkim.domain.board.dao.BoardUserRepository;
import com.wanted.legendkim.domain.board.dto.FreeBoardDTO;
import com.wanted.legendkim.domain.board.entity.Post;
import com.wanted.legendkim.domain.board.entity.BoardUser;
import com.wanted.legendkim.global.config.ContextConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FreeBoardService {

    private final PostRepository postRepository;
    private final BoardUserRepository boardUserRepository;

    public List<FreeBoardDTO> getPosts(String filter, String email) {
        List<Post> posts;

        if ("mine".equalsIgnoreCase(filter)) {
            BoardUser user = boardUserRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            posts = postRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        } else {
            posts = postRepository.findAllByOrderByCreatedAtDesc();
        }

        return posts.stream()
                .map(post -> new FreeBoardDTO(
                        post.getId(),
                        post.getTitle(),
                        null,
                        post.getUser().getName(),
                        post.getViewCount(),
                        post.getCreatedAt().toLocalDate().toString(),
                        false
                ))
                .toList();
    }

    @Transactional
    public FreeBoardDTO getPostDetail(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        post.increaseViewCount();

        boolean mine = email != null
                && post.getUser().getEmail().equals(email);

        return new FreeBoardDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getName(),
                post.getViewCount(),
                post.getCreatedAt().toLocalDate().toString(),
                mine
        );
    }
}