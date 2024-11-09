package store.model.promotion;

import store.dto.FreeProductDto;

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

    public FreeProductDto calculateFreeProducts(int quantity) {
        int freeProducts = 0;
        boolean addOne = false;

        if (this == BUY_ONE_GET_ONE) {
            freeProducts += quantity / 2;
            if (quantity % 2 == 1) {
                addOne = true;
            }
            return new FreeProductDto(freeProducts, addOne);
        }

        if (this == BUY_TWO_GET_ONE) {
            freeProducts = quantity / 3;
            if (quantity % 3 == 2) {
                addOne = true;
            }
            return new FreeProductDto(freeProducts, addOne);
        }

        return new FreeProductDto(freeProducts, addOne);
    }
}
