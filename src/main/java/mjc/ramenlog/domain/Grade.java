package mjc.ramenlog.domain;

import lombok.Getter;

@Getter
public enum Grade {

    RAMEN_NOOB("멘알못", 0, 3),
    RAMEN_BEGINNER("라린이", 3, 15),
    RAMEN_LOVER("라더쿠", 15, 30),
    RAMEN_LEGEND("라전드", 30, 50),
    RAMEN_MASTER("라오타", 50, -1); // 마지막 등급은 승급 없음

    private final String name;
    private final int startReviewCount;  // 해당 등급 시작 시점
    private final int endReviewCount; // 다음 등급까지 필요한 개수 (-1이면 없음)

    Grade(String name, int startReviewCount, int endReviewCount) {
        this.name = name;
        this.startReviewCount = startReviewCount;
        this.endReviewCount = endReviewCount;
    }

    // ✅ 리뷰 개수에 따라 현재 등급을 리턴
    public static Grade fromReviewCount(int reviewCount) {
        Grade result = RAMEN_NOOB;
        for (Grade grade : values()) {
            if (reviewCount >= grade.startReviewCount) {
                result = grade;
            } else {
                break;
            }
        }
        return result;
    }

    // ✅ 다음 등급까지 남은 리뷰 수 계산
    public int getRemainingToNext(int currentReviewCount) {
        if (endReviewCount == -1) return 0;
        return Math.max(0, endReviewCount - currentReviewCount);
    }

    public Grade getNextGrade() {
        int nextOrdinal = this.ordinal() + 1;
        return nextOrdinal < Grade.values().length ? Grade.values()[nextOrdinal] : this;
    }
}