package store.model;

public enum StoreConstant {
    QUANTITY_PURCHASE_SAME(0),
    QUANTITY_MORE(1),
    PURCHASE_MORE(-1),
    PROMOTION_NOT_EXIST(0);

    private int message;

    StoreConstant(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }
}
