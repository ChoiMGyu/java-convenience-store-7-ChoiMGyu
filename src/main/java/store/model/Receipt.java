package store.model;

import store.dto.ReceiptDto;

import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Receipt {
    private Map<String, ReceiptDto> purchase;
    private Map<String, ReceiptDto> gift;
    private int memberShipDiscount = 0;

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

    public int calculatePurchase(int totalMoney, int event) {
        return totalMoney - event;
    }

    public void calculateMemberShipDiscount(int discount) {
        this.memberShipDiscount = discount;
    }

    @Override
    public String toString() {
        System.out.println("==============W 편의점================");
        ;
        System.out.printf("%-18s%-9s%-4s\n", "상품명", "수량", "금액");
        for (Map.Entry<String, ReceiptDto> entry : purchase.entrySet()) {
            int price = entry.getValue().getPrice() * entry.getValue().getQuantity();
            NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);
            String formattedMoney = formatter.format(price);
            System.out.printf("%-17s%-10d%-4s\n", entry.getKey(), entry.getValue().getQuantity(), formattedMoney);
        }
        System.out.println("=============증     정===============");
        for (Map.Entry<String, ReceiptDto> entry : gift.entrySet()) {
            System.out.printf("%-17s%-10d\n", entry.getKey(), entry.getValue().getQuantity());
        }
        System.out.println("====================================");
        NumberFormat converter = NumberFormat.getNumberInstance(Locale.KOREA);
        String convertedMoney = converter.format(calculateTotalMoney());
        System.out.printf("%-17s%-10d%-4s\n", "총구매액", calculateTotalQuantity(), convertedMoney);
        convertedMoney = converter.format(-calculateEventDiscount());
        System.out.printf("%-27s%10s\n", "행사할인", convertedMoney);
        convertedMoney = converter.format(-memberShipDiscount);
        System.out.printf("%-27s%10s\n", "멤버십할인", convertedMoney);
        convertedMoney = converter.format(calculatePurchase(calculateTotalMoney(), calculateEventDiscount()));
        System.out.printf("%-27s%10s\n", "내실돈", convertedMoney);
        return "";
    }
}
