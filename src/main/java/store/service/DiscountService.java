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
        //프로모션 할인을 받지 않은 상품에 대해서만 할인을 적용해야 한다
        //즉,purchase에 있는 상품들 중에 gift에 없는 것들만 할인을 적용해야 한다
        Map<String, ReceiptContent> gift = receipt.getGift();
        Map<String, ReceiptContent> purchase = receipt.getPurchase();
        if (answer) {
            int totalMoney = 0;

            // purchase에 있지만 gift에 없는 상품들만 처리
            for (Map.Entry<String, ReceiptContent> entry : purchase.entrySet()) {
                if (!gift.containsKey(entry.getKey())) {
                    totalMoney += entry.getValue().getPrice() * entry.getValue().getQuantity();
                }
            }

            int memberShipDiscount = receipt.calculateMemberShipDiscount(totalMoney);
            return memberShipDiscount;
        }
        return NO_MEMBERSHIP_DISCOUNT;
    }


    public int calculatePurchase(Receipt receipt, boolean answer) {
        int totalMoney = receipt.calculateTotalMoney();
        int promotionDiscount = receipt.calculateEventDiscount();
        Map<String, ReceiptContent> gift = receipt.getGift();
        Map<String, ReceiptContent> purchase = receipt.getPurchase();
        int tmpMoney = 0;

        // purchase에 있지만 gift에 없는 상품들만 처리
        for (Map.Entry<String, ReceiptContent> entry : purchase.entrySet()) {
            if (!gift.containsKey(entry.getKey())) {
                tmpMoney += entry.getValue().getPrice() * entry.getValue().getQuantity();
            }
        }
        int memberShipDiscount = receipt.calculateMemberShipDiscount(tmpMoney);
        int purchaseMoney;

        if (answer) {
            purchaseMoney = receipt.calculatePurchase(totalMoney, promotionDiscount, memberShipDiscount);
            return purchaseMoney;
        }
        return receipt.calculatePurchase(totalMoney, promotionDiscount, NO_MEMBERSHIP_DISCOUNT);
    }
}
