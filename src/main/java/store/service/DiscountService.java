package store.service;

import store.dto.ReceiptDto;
import store.model.Receipt;

public class DiscountService {
    private static final int NO_MEMBERSHIP_DISCOUNT = 0;

    public ReceiptDto calculateTotalMoney(Receipt receipt) {
        int totalCount = receipt.calculateTotalQuantity();
        int totalMoney = receipt.calculateTotalMoney();

        return new ReceiptDto(totalCount, totalMoney);
    }

    public int calculatePromotionDiscount(Receipt receipt) {
        int totalPromotion = receipt.calculateEventDiscount();

        return totalPromotion;
    }


    public int calculateMemberShipDiscount(Receipt receipt, boolean answer) {
        int totalMoney = receipt.calculateTotalMoney();
        if (answer) {
            int memberShipDiscount = receipt.calculateMemberShipDiscount(totalMoney);
            return memberShipDiscount;
        }
        return NO_MEMBERSHIP_DISCOUNT;
    }


    public int calculatePurchase(Receipt receipt, boolean answer) {
        int totalMoney = receipt.calculateTotalMoney();
        int promotionDiscount = receipt.calculateEventDiscount();
        int memberShipDiscount = receipt.calculateMemberShipDiscount(totalMoney);
        int purchase;

        if (answer) {
            purchase = receipt.calculatePurchase(totalMoney, promotionDiscount, memberShipDiscount);
            return purchase;
        }
        return receipt.calculatePurchase(totalMoney, promotionDiscount, NO_MEMBERSHIP_DISCOUNT);
    }
}
