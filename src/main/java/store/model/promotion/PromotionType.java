package store.model.promotion;

import store.dto.FreeProductDto;

import java.util.Arrays;

import static store.model.promotion.PromotionTypeConstant.*;

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
            freeProducts += quantity / BUY_ONE_GET_ONE_DIVISOR.getAddOneConstant();
            if (quantity % BUY_ONE_GET_ONE_DIVISOR.getAddOneConstant() == BUY_ONE_GET_ONE_CAN_ADDONE.getAddOneConstant()) {
                addOne = true;
            }
            return new FreeProductDto(freeProducts, addOne);
        }

        if (this == BUY_TWO_GET_ONE) {
            freeProducts = quantity / BUY_TWO_GET_ONE_DIVISOR.getAddOneConstant();
            if (quantity % BUY_TWO_GET_ONE_DIVISOR.getAddOneConstant() == BUY_TWO_GET_ONE_CAN_ADDONE.getAddOneConstant()) {
                addOne = true;
            }
            return new FreeProductDto(freeProducts, addOne);
        }

        return new FreeProductDto(freeProducts, addOne);
    }

    public int getTotalBuyGet() {
        return this.buy + this.get;
    }
}
