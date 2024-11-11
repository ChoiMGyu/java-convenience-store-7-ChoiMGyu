package view;

import store.model.ReceiptContent;
import store.model.Receipt;
import store.model.product.GeneralProduct;
import store.model.product.Product;
import store.model.product.PromotionProduct;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OutputView {
    private static final String GREETING_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String NOW_ITEM_MESSAGE = "현재 보유하고 있는 상품입니다.";

    private void printLineSeperate() {
        System.out.print(System.lineSeparator());
    }

    public void printGreeting() {
        System.out.println(GREETING_MESSAGE);
        System.out.println(NOW_ITEM_MESSAGE);
        printLineSeperate();
    }

    public void printItems(List<Product> products) {
        products.stream()
                .filter(product -> product instanceof GeneralProduct || product instanceof PromotionProduct)
                .forEach(System.out::println);
        printLineSeperate();
    }

    public void printReceipt(Receipt receipt, ReceiptContent receiptContent, int promotionDiscount, int memberShipDiscount, int purchaseMoney) {
        System.out.println("==============W 편의점================");

        printPurchase(receipt);
        printGift(receipt);

        printTotalMoney(receiptContent);
        printPromotionDiscount(promotionDiscount);
        printMemberShipDiscount(memberShipDiscount);
        printPurchaseMoney(purchaseMoney);
    }

    private String numberFormatKorea(int price) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);
        return formatter.format(price);
    }

    private void printPurchase(Receipt receipt) {
        System.out.printf("%-18s%-9s%-4s\n", "상품명", "수량", "금액");
        Map<String, ReceiptContent> purchase = receipt.getPurchase();
        for (Map.Entry<String, ReceiptContent> entry : purchase.entrySet()) {
            int price = entry.getValue().calculateProductPerMoney();
            String formattedMoney = numberFormatKorea(price);
            System.out.printf("%-17s%-10d%-4s\n", entry.getKey(), entry.getValue().getQuantity(), formattedMoney);
        }
    }

    private void printGift(Receipt receipt) {
        System.out.println("=============증     정===============");
        Map<String, ReceiptContent> gift = receipt.getGift();
        for (Map.Entry<String, ReceiptContent> entry : gift.entrySet()) {
            System.out.printf("%-17s%-10d\n", entry.getKey(), entry.getValue().getQuantity());
        }
    }

    private void printTotalMoney(ReceiptContent receiptContent) {
        System.out.println("====================================");
        String convertedMoney = numberFormatKorea(receiptContent.getPrice());
        System.out.printf("%-17s%-10d%-4s\n", "총구매액", receiptContent.getQuantity(), convertedMoney);
    }

    private void printPromotionDiscount(int promotionDiscount) {
        String convertedMoney = numberFormatKorea(-promotionDiscount);
        if(promotionDiscount == 0) {
            convertedMoney = "-0";
        }
        System.out.printf("%-27s%10s\n", "행사할인", convertedMoney);
    }

    private void printMemberShipDiscount(int memberShipDiscount) {
        String convertedMoney = numberFormatKorea(-memberShipDiscount);
        if(memberShipDiscount == 0) {
            convertedMoney = "-0";
        }
        System.out.printf("%-27s%10s\n", "멤버십할인", convertedMoney);
    }

    private void printPurchaseMoney(int purchaseMoney) {
        String convertedMoney = numberFormatKorea(purchaseMoney);
        System.out.printf("%-27s%10s\n", "내실돈", convertedMoney);
        printLineSeperate();
    }
}
