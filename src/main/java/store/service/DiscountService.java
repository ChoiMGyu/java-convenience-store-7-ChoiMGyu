package store.service;

import store.dto.ReceiptDto;
import store.model.Receipt;

public class DiscountService {
    private static final int NO_MEMBERSHIP_DISCOUNT = 0;

    //총 지불해야 하는 금액
    public ReceiptDto calculateTotalMoney(Receipt receipt) {
        int totalCount = receipt.calculateTotalQuantity();
        int totalMoney = receipt.calculateTotalMoney();

        return new ReceiptDto(totalCount, totalMoney);
    }

    //프로모션 할인
    public int calculatePromotionDiscount(Receipt receipt) {
        int totalPromotion = receipt.calculateEventDiscount();

        return totalPromotion;
    }


    //멤버십 할인
    public int calculateMemberShipDiscount(Receipt receipt, boolean answer) {
        int totalMoney = receipt.calculateTotalMoney();
        if (answer) {
            int memberShipDiscount = receipt.calculateMemberShipDiscount(totalMoney);
            return memberShipDiscount;
        }
        return NO_MEMBERSHIP_DISCOUNT;
    }


    //내야 할 돈
    public int calculatePurchase(Receipt receipt, boolean answer) {
        int totalMoney = receipt.calculateTotalMoney();
        int promotionDiscount = receipt.calculateEventDiscount();
        int memberShipDiscount = receipt.calculateMemberShipDiscount(totalMoney);
        int purchase;

        if(answer) {
            purchase = receipt.calculatePurchase(totalMoney, promotionDiscount, memberShipDiscount);
            return purchase;
        }
        return receipt.calculatePurchase(totalMoney, promotionDiscount, NO_MEMBERSHIP_DISCOUNT);
    }
}
