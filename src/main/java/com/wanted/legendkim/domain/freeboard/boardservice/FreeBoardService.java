package com.wanted.legendkim.domain.freeboard.boardservice;

import com.wanted.legendkim.domain.comment.dao.FreeCommentRepository;
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
    private final FreeCommentRepository freeCommentRepository;

//    public List<FreeBoardDTO> getPosts(String filter, String email) {
//        List<FreeBoardPost> posts;
//
//        if ("mine".equalsIgnoreCase(filter)) {// 대소문자 상관 없이 비교한다
//            // 내가 쓴 글만 조회
//            FreeBoardUser user = freeBoardUserRepository.findByEmail(email) // 이메일로 사용자 찾기
//                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//                                        // 없으면 오류 반환
//
//            posts = freeBoardPostRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
//                                        // 이 사용자의 글을 최신순으로 가져오기
//        } else {// 전체 글 조회
//            posts = freeBoardPostRepository.findAllByOrderByCreatedAtDesc();
//                                        // 모든 글을 최신순으로 가져오기
//        }
//
//        return posts.stream()
//                .map(post -> new FreeBoardDTO(
//                        post.getId(),
//                        post.getTitle(),
//                        post.getUser().getName(),
//                        post.getViewCount(),
//                        post.getCreatedAt().toLocalDate().toString()
//                ))
//                .toList();
//        // entity 를 하나씩 꺼내서 DTO로 변환
//    }

    @Transactional(readOnly = true)
    public List<FreeBoardDTO> getPosts(String filter, String email) {

        List<FreeBoardPost> posts;

        if ("mine".equalsIgnoreCase(filter)) {

            FreeBoardUser user = freeBoardUserRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
            posts = freeBoardPostRepository.findByUserIdWithUserOrderByCreatedAtDesc(user.getId());
        } else {
            posts = freeBoardPostRepository.findAllWithUserOrderByCreatedAtDesc();
        }

        System.out.println("========== [getPosts DTO 변환 시작] ==========");
        long mapStart = System.currentTimeMillis();

        List<FreeBoardDTO> result = posts.stream()
                .map(post -> new FreeBoardDTO(
                        post.getId(),
                        post.getTitle(),
                        post.getUser().getName(),
                        post.getViewCount(),
                        post.getCreatedAt().toLocalDate().toString()
                ))
                .toList();

        long mapEnd = System.currentTimeMillis();
        double mapMs = (mapEnd - mapStart) / 1_000_000.0;
        System.out.println("[getPosts] DTO 변환 시간 = " + mapMs + "ms");
        System.out.println("========== [getPosts DTO 변환 종료] ==========");

        return result;
    }

    @Transactional
    public FreeBoardDetailDTO getPostDetail(Long postId, String email, boolean increaseView) {

        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                                            // 게시글 아이디로 게시글 정보 가져오기
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (increaseView) {
            post.increaseViewCount(); // 조회수 1 올리기
        }

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

    @Transactional(readOnly = true)
    public FreeBoardDetailDTO getEditPost(Long postId, String email) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId) // 게시글 아이디로 게시글 정보 불러오기
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 프론트로는 버튼을 자신의 게시물에만 보이게 해놨지만 url로 접속이 가능하다.
        // 그래서 백에서도 접속을 막아야한다.
        validateWriter(post, email);

        return new FreeBoardDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getName(),
                post.getViewCount(),
                post.getCreatedAt().toLocalDate().toString(),
                true
        ); // 게시글 내용을 반환
    }

    @Transactional
    public void editPost(Long postId, String title, String content, String email) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        // 게시글 아이디로 게시글 정보 찾기

        validateWriter(post, email); // 작성자만 수정 가능하게 제한

        post.modify(title, content); // 변경사항 수정하기
    }

    @Transactional
    public void deletePost(Long postId, String email) {
        FreeBoardPost post = freeBoardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        // 게시글 아이디로 게시글 정보 찾기

        validateWriter(post, email); // 작성자만 삭제 가능하게 제한

        freeCommentRepository.deleteByPostId(postId); // 해당 게시글의 댓글 먼저 삭제
        freeBoardPostRepository.delete(post); // 게시글 삭제
    }

    private void validateWriter(FreeBoardPost post, String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        } // email이 비어있으면 로그인을 안했다는 뜻

        if (!post.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("작성자만 할 수 있습니다.");
        } // 게시물을 쓴 사람의 이메일과 접속한 사람의 이메일을 비교해서 다르면 작성자가 아니라는 뜻
    }

    @Transactional(readOnly = true)
    public List<FreeBoardDTO> getAdminPosts() {
        return freeBoardPostRepository.findAllWithUserOrderByCreatedAtDesc()
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
}