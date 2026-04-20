package com.wanted.legendkim.domain.enrollment.dto;
// 수강신청 Post 요청의 JSON 바디를 담는 입력 전용 DTO이다.
import lombok.*;
// lombok의 5형제를 넣어뒀다.
@NoArgsConstructor
@AllArgsConstructor
@Getter

/* comment.
    우리는 강의를 배울 때 Request 의 경우에는 외부에서 입력을 한다.
    이는 Jackson 역직렬화를 위해 setter 를 허용한다. -> 이 줄의 설명은 아래에 코멘트. 를 달겠다.
    하지만 Response 의 경우에는 내부 생성이며 생성 이후에 바뀔 이유가
    없다. 따라서 우리는 이를 불면으로 지정해 외부에서 수정하는 것을 방어할 수 있다.
 */

/* comment.
    Jackson 역직렬화란?
    JSON 은 글자, 자바는 객체이다. Jackson 이라는 것이 이 둘 사이를
    번역해준다. Jackson 이 빈 객체에 값을 넣으려면 Setter가 있어야
    값을 넣을 수 있기 때문에 Request 의 경우에는 사용한다.
 */

@Setter
@ToString
public class EnrollmentRequest {

    private Long userId;
    private Long courseId;

}
