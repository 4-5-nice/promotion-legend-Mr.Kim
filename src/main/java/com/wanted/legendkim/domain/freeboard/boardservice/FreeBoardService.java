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

        if ("mine".equalsIgnoreCase(filter)) {// 대소문자 상관 없이 비교한다
            // 내가 쓴 글만 조회
            FreeBoardUser user = freeBoardUserRepository.findByEmail(email) // 이메일로 사용자 찾기
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
                                        // 없으면 오류 반환

            posts = freeBoardPostRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
                                        // 이 사용자의 글을 최신순으로 가져오기
        } else {// 전체 글 조회
            posts = freeBoardPostRepository.findAllByOrderByCreatedAtDesc();
                                        // 모든 글을 최신순으로 가져오기
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
        // entity 를 하나씩 꺼내서 DTO로 변환
    }

    @Transactional
    public FreeBoardDetailDTO getPostDetail(Long postId, String email) {

        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                                            // 게시글 아이디로 게시글 정보 가져오기
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        post.increaseViewCount(); // 조회수 1 올리기

        boolean mine = email != null && post.getUser().getEmail().equals(email);
        // 로그인한 사용자의 email과 게시글 작성자의 email을 비교해서
        // 게시글을 조회한 사람이 게시글의 작성자인지 아닌지 판단한다.
        // 게시글 작성자에게만 수정, 삭제 버튼을 띄우기 위한 코드이다.

        return new FreeBoardDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getName(),
                post.getViewCount(),
                post.getCreatedAt().toLocalDate().toString(),
                mine
        ); // 게시글의 정보를 반환

    }

    @Transactional
    public void writePost(String title, String content, String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        } // 로그인하지 않으면 글 작성 못하게하기 controller에서 담아서 보낸 email을 보고 비어있으면 로그인 하지 않은것.

        FreeBoardUser user = freeBoardUserRepository.findByEmail(email) // 이메일을 이용해서 사용자 찾기
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        FreeBoardPost post = new FreeBoardPost(user, title, content);
        // 엔티티에 작성자 정보와 입력 받은 제목, 내용 저장

        freeBoardPostRepository.save(post); // 만든 게시글을 persistence context에 연결한다.
    }
}