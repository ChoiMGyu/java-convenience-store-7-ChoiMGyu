package view;

import store.model.Receipt;
import store.model.product.GeneralProduct;
import store.model.product.Product;
import store.model.product.PromotionProduct;

import java.util.List;

public class OutputView {
    private void printLineSeperate() {
        System.out.print(System.lineSeparator());
    }

    public void printGreeting() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        printLineSeperate();
    }

    public void printItems(List<Product> products) {
        products.stream()
                .filter(product -> product instanceof GeneralProduct || product instanceof PromotionProduct)
                .forEach(System.out::println);
        printLineSeperate();
    }

    public void printReceipt(Receipt receipt) {
        System.out.println(receipt);
    }

}
