package store.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Receipt {
    private static final int MEMBERSHIP_DISCOUNT_MAX = 8000;
    private static final double MEMBERSHIP_DISCOUNT = 0.3;

    private Map<ReceiptTitle, ReceiptContent> purchase;
    private Map<ReceiptTitle, ReceiptContent> gift;

    private Receipt(Map<ReceiptTitle, ReceiptContent> purchase, Map<ReceiptTitle, ReceiptContent> gift) {
        this.purchase = purchase;
        this.gift = gift;
    }

    public static Receipt createReceipt() {
        Map<ReceiptTitle, ReceiptContent> purchase = new LinkedHashMap<>();
        Map<ReceiptTitle, ReceiptContent> gift = new LinkedHashMap<>();
        return new Receipt(purchase, gift);
    }

    public void addPurchase(String name, boolean isPartial, int price, int quantity) {
        purchase.put(new ReceiptTitle(name, isPartial), new ReceiptContent(price, quantity));
    }

    public void addGift(String name, int price, int quantity) {
        gift.put(new ReceiptTitle(name, false), new ReceiptContent(price, quantity));
    }

    public int calculateTotalQuantity() {
        int totalCount = 0;
        for (Map.Entry<ReceiptTitle, ReceiptContent> entry : purchase.entrySet()) {
            totalCount += entry.getValue().getQuantity();
        }
        return totalCount;
    }

    public int calculateTotalMoney() {
        int totalMoney = 0;
        for (Map.Entry<ReceiptTitle, ReceiptContent> entry : purchase.entrySet()) {
            int price = entry.getValue().calculateProductPerMoney();
            totalMoney += price;
        }
        return totalMoney;
    }

    public int calculateEventDiscount() {
        int discount = 0;
        for (Map.Entry<ReceiptTitle, ReceiptContent> entry : gift.entrySet()) {
            int price = entry.getValue().calculateProductPerMoney();
            discount += price;
        }
        return discount;
    }

    public int calculateMemberShipDiscount(int totalMoney) {
        int memberShipDiscount = (int) (totalMoney * MEMBERSHIP_DISCOUNT);
        if (memberShipDiscount > MEMBERSHIP_DISCOUNT_MAX) {
            memberShipDiscount = MEMBERSHIP_DISCOUNT_MAX;
        }
        return memberShipDiscount;
    }

    public int calculateNotDuplicateDiscount() {
        int totalMoney = 0;
        for (Map.Entry<ReceiptTitle, ReceiptContent> entry : purchase.entrySet()) {
            if (entry.getKey().getIsPartial()) {
                totalMoney += entry.getValue().calculateProductPerMoney();
            }
        }
        return totalMoney;
    }


    public int calculatePurchase(int totalMoney, int promotionDiscount, int memberShipDiscount) {
        return totalMoney - promotionDiscount - memberShipDiscount;
    }

    public Map<ReceiptTitle, ReceiptContent> getPurchase() {
        return Collections.unmodifiableMap(
                purchase.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> new ReceiptContent(entry.getValue().getPrice(), entry.getValue().getQuantity())
                        ))
        );
    }

    public Map<ReceiptTitle, ReceiptContent> getGift() {
        return Collections.unmodifiableMap(
                gift.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> new ReceiptContent(entry.getValue().getPrice(), entry.getValue().getQuantity())
                        ))
        );
    }

}
