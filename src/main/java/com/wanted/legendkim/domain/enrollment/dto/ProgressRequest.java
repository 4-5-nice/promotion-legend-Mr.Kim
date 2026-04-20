package com.wanted.legendkim.domain.enrollment.dto;
// 진행률 업데이트 요청을 담는 입력 전용 DTO
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProgressRequest {

    /* comment.
        지금은 진행률 하나지만, 향후 발전 목표로 "나중에 시청한 위치"
        같은 필드가 추가될 것을 대비해 DTO 로 생성했다. 또한 다른 API 들과
        요청 방식을 JSON 으로 통일해 패턴이 일관되게 유지된다.
     */

    private int progress;

}
