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
        int promotionDiscount = calculatePromotionDiscount(receipt);
        int memberShipDiscount = calculateApplicableMemberShipDiscount(receipt, answer);
        return computeFinalPurchaseAmount(receipt, totalMoney, promotionDiscount, memberShipDiscount);
    }

    private int calculateApplicableMemberShipDiscount(Receipt receipt, boolean answer) {
        if (answer) {
            int nonDuplicateDiscount = receipt.calculateNotDuplicateDiscount();
            return receipt.calculateMemberShipDiscount(nonDuplicateDiscount);
        }
        return NO_MEMBERSHIP_DISCOUNT;
    }

    private int computeFinalPurchaseAmount(Receipt receipt, int totalMoney, int promotionDiscount, int memberShipDiscount) {
        return receipt.calculatePurchase(totalMoney, promotionDiscount, memberShipDiscount);
    }
}
