package view;

import java.util.function.Supplier;

public class InputView {

    private final Supplier<String> reader;

    public InputView(Supplier<String> reader) {
        this.reader = reader;
    }

    private <T> T repeatLoop(Supplier<T> inputFunction) {
        try {
            return inputFunction.get();
        } catch (IllegalArgumentException e) {
            printErrorMessage(e.getMessage());
            return repeatLoop(inputFunction);
        }
    }

    private void printErrorMessage(String errorMessage) {
        System.out.println(errorMessage);
    }

    private void printLineSeperate() {
        System.out.print(System.lineSeparator());
    }

    public String readItem() {
        return repeatLoop(() -> {
            System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
            String item = reader.get();
            printLineSeperate();
            return item;
        });
    }

    public String readAddItem(String item, int quantity) {
        return repeatLoop(() -> {
            System.out.printf("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)", item, quantity);
            printLineSeperate();
            String addItemAnswer = reader.get();
            printLineSeperate();
            return addItemAnswer;
        });
    }

    public String readPartialPayment(String item, int quantity) {
        return repeatLoop(() -> {
            System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)", item, quantity);
            printLineSeperate();
            String partialPayment = reader.get();
            printLineSeperate();
            return partialPayment;
        });
    }

    public String readMemberDiscount() {
        return repeatLoop(() -> {
            System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
            String discountAnswer = reader.get();
            printLineSeperate();
            return discountAnswer;
        });
    }

    public String readMorePurchase() {
        return repeatLoop(() -> {
            System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
            String morePurchaseAnswer = reader.get();
            printLineSeperate();
            return morePurchaseAnswer;
        });
    }
}
