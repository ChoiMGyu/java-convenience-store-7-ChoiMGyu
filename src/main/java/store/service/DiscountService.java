package store.service;

import store.model.Receipt;

public class DiscountService {
    private static final int MEMBERSHIP_DISCOUNT_MAX = 8000;
    private static final double MEMBERSHIP_DISCOUNT = 0.3;

    public void calculateMemberShipDiscount(Receipt receipt, boolean answer) {
        int totalMoney = receipt.calculateTotalMoney();
        if (answer) {
            int discount = (int) (totalMoney * MEMBERSHIP_DISCOUNT);
            if (discount > MEMBERSHIP_DISCOUNT_MAX) {
                discount = MEMBERSHIP_DISCOUNT_MAX;
            }
            receipt.calculateMemberShipDiscount(discount);
        }
    }
}
