package store.service;

import store.model.ReceiptContent;
import store.model.Receipt;

import java.util.Map;

public class DiscountService {
    private static final int NO_MEMBERSHIP_DISCOUNT = 0;

    public ReceiptContent calculateTotalMoney(Receipt receipt) {
        int totalCount = receipt.calculateTotalQuantity();
        int totalMoney = receipt.calculateTotalMoney();

        return new ReceiptContent(totalCount, totalMoney);
    }

    public int calculatePromotionDiscount(Receipt receipt) {
        int totalPromotion = receipt.calculateEventDiscount();

        return totalPromotion;
    }


    public int calculateMemberShipDiscount(Receipt receipt, boolean answer) {
        if (answer) {
            int totalMoney = 0;

            totalMoney = receipt.calculateNotDuplicateDiscount();

            int memberShipDiscount = receipt.calculateMemberShipDiscount(totalMoney);
            return memberShipDiscount;
        }
        return NO_MEMBERSHIP_DISCOUNT;
    }


    public int calculatePurchase(Receipt receipt, boolean answer) {
        int totalMoney = receipt.calculateTotalMoney();
        int promotionDiscount = receipt.calculateEventDiscount();

        int notDuplicate = receipt.calculateNotDuplicateDiscount();
        int memberShipDiscount = receipt.calculateMemberShipDiscount(notDuplicate);
        int purchaseMoney;

        if (answer) {
            purchaseMoney = receipt.calculatePurchase(totalMoney, promotionDiscount, memberShipDiscount);
            return purchaseMoney;
        }
        return receipt.calculatePurchase(totalMoney, promotionDiscount, NO_MEMBERSHIP_DISCOUNT);
    }
}
