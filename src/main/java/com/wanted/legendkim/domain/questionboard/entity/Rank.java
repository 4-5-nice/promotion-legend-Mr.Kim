package com.wanted.legendkim.domain.questionboard.entity;

public enum Rank {
    INTERN("인턴", 0),
    CONTRACT("계약직", 1),
    STAFF("정규직", 2),
    ASSISTANT_MANAGER("대리", 3),
    MANAGER("과장", 4),
    DIRECTOR("부장", 5),
    EXECUTIVE("임원", 6),
    RETIRED("정년퇴직", 7);

    private final String label;
    private final int level;

    Rank(String label, int level) {
        this.label = label;
        this.level = level;
    }

    public String getLabel() {
        return label;
    }

    public int getLevel() {
        return level;
    }

    // 사용자가 특정 직급 목록을 조회할 수 있는가
    public boolean canView(Rank requestedRank) {
        return requestedRank.level <= this.level;
        // 요청된 직급이 내 직급보다 같거나 작으면 true
    }

    // 이 직급이 requestedRank보다 상위인가
    public boolean isHigherThan(Rank requestedRank) {
        return this.level > requestedRank.level;
    }

    // 한글 직급명을 enum 값으로 변환
    public static Rank fromLabel(String dbData) {
        for (Rank rank : values()) { // enum의 직급들과 사용자의 직급을 하나씩 비교
            if (rank.label.equals(dbData)) { // 같으면
                return rank; // 그 enum 반환
            }
        }
        throw new IllegalArgumentException("지원하지 않는 직급입니다: " + dbData);
    }
}
