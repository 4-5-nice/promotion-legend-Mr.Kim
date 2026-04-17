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

    // 사용자가 특정 직급 목록을 조회할 수 있는지
    public boolean canView(Rank requestedRank) {
        return requestedRank.level <= this.level;
    }

    // 이 직급이 requestedRank보다 상위인지
    public boolean isHigherThan(Rank requestedRank) {
        return this.level > requestedRank.level;
    }

    public static Rank fromLabel(String label) {
        for (Rank rank : values()) {
            if (rank.label.equals(label)) {
                return rank;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 직급입니다: " + label);
    }
}
