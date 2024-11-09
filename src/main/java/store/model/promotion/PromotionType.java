package store.model.promotion;

import java.util.Arrays;

public enum PromotionType {
    NO_PROMOTION(0, 0),
    BUY_ONE_GET_ONE(1, 1),
    BUY_TWO_GET_ONE(2, 1);

    private final int buy;
    private final int get;

    PromotionType(int buy, int get) {
        this.buy = buy;
        this.get = get;
    }

    public static PromotionType of(int buy, int get) {
        return Arrays.stream(values())
                .filter(promotionType -> promotionType.matches(buy, get))
                .findFirst()
                .orElse(NO_PROMOTION);
    }

    private boolean matches(int buy, int get) {
        return this.buy == buy && this.get == get;
    }
}
