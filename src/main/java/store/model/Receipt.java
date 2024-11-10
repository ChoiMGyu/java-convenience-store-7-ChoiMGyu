package store.model;

import store.dto.ReceiptDto;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Receipt {
    private static final int MEMBERSHIP_DISCOUNT_MAX = 8000;
    private static final double MEMBERSHIP_DISCOUNT = 0.3;

    private Map<String, ReceiptDto> purchase;
    private Map<String, ReceiptDto> gift;

    private Receipt(Map<String, ReceiptDto> purchase, Map<String, ReceiptDto> gift) {
        this.purchase = purchase;
        this.gift = gift;
    }

    public static Receipt createReceipt() {
        Map<String, ReceiptDto> purchase = new LinkedHashMap<>();
        Map<String, ReceiptDto> gift = new LinkedHashMap<>();
        return new Receipt(purchase, gift);
    }

    public void addPurchase(String name, int price, int quantity) {
        purchase.put(name, new ReceiptDto(price, quantity));
    }

    public void addGift(String name, int price, int quantity) {
        gift.put(name, new ReceiptDto(price, quantity));
    }

    public int calculateTotalQuantity() {
        int totalCount = 0;
        for (Map.Entry<String, ReceiptDto> entry : purchase.entrySet()) {
            totalCount += entry.getValue().getQuantity();
        }
        return totalCount;
    }

    public int calculateTotalMoney() {
        int totalMoney = 0;
        for (Map.Entry<String, ReceiptDto> entry : purchase.entrySet()) {
            int price = entry.getValue().getPrice() * entry.getValue().getQuantity();
            totalMoney += price;
        }
        return totalMoney;
    }

    public int calculateEventDiscount() {
        int discount = 0;
        for (Map.Entry<String, ReceiptDto> entry : gift.entrySet()) {
            int price = entry.getValue().getPrice() * entry.getValue().getQuantity();
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


    public int calculatePurchase(int totalMoney, int promotionDiscount, int memberShipDiscount) {
        return totalMoney - promotionDiscount - memberShipDiscount;
    }

    public Map<String, ReceiptDto> getPurchase() {
        return Collections.unmodifiableMap(
                purchase.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> new ReceiptDto(entry.getValue().getPrice(), entry.getValue().getQuantity())
                        ))
        );
    }

    public Map<String, ReceiptDto> getGift() {
        return Collections.unmodifiableMap(
                gift.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> new ReceiptDto(entry.getValue().getPrice(), entry.getValue().getQuantity())
                        ))
        );
    }
}
