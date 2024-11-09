package store.model.promotion;

public enum PromotionTypeConstant {
    BUY_ONE_GET_ONE_DIVISOR(2),
    BUY_ONE_GET_ONE_CAN_ADDONE(1),
    BUY_TWO_GET_ONE_DIVISOR(3),
    BUY_TWO_GET_ONE_CAN_ADDONE(2);

    private int addOneConstant;

    PromotionTypeConstant(int addOneConstant) {
        this.addOneConstant = addOneConstant;
    }

    public int getAddOneConstant() {
        return addOneConstant;
    }
}
