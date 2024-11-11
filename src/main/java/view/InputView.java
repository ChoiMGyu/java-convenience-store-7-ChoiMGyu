package view;

import store.model.Store;
import view.dto.ProductDto;
import view.validator.InputValidator;

import java.util.List;
import java.util.function.Supplier;

public class InputView {
    private static final String READ_ITEM_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String MEMBERSHIP_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String MORE_PURCHASE_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    private final Supplier<String> reader;
    private final InputValidator inputValidator;

    public InputView(Supplier<String> reader, InputValidator inputValidator) {
        this.reader = reader;
        this.inputValidator = inputValidator;
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

    public List<ProductDto> readItem(Store store) {
        return repeatLoop(() -> {
            System.out.println(READ_ITEM_MESSAGE);
            String item = reader.get();
            printLineSeperate();
            List<ProductDto> items = inputValidator.validateItemInput(store, item);
            return items;
        });
    }

    public boolean readAddItem(String item, int quantity) {
        return repeatLoop(() -> {
            System.out.printf("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)", item, quantity);
            printLineSeperate();
            String addItemAnswer = reader.get();
            printLineSeperate();
            boolean answer = inputValidator.validateAnswer(addItemAnswer);
            return answer;
        });
    }

    public boolean readPartialPayment(String item, int quantity) {
        return repeatLoop(() -> {
            System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)", item, quantity);
            printLineSeperate();
            String partialPayment = reader.get();
            printLineSeperate();
            boolean answer = inputValidator.validateAnswer(partialPayment);
            return answer;
        });
    }

    public boolean readMemberDiscount() {
        return repeatLoop(() -> {
            System.out.println(MEMBERSHIP_MESSAGE);
            String discountAnswer = reader.get();
            printLineSeperate();
            boolean answer = inputValidator.validateAnswer(discountAnswer);
            return answer;
        });
    }

    public boolean readMorePurchase() {
        return repeatLoop(() -> {
            System.out.println(MORE_PURCHASE_MESSAGE);
            String morePurchaseAnswer = reader.get();
            printLineSeperate();
            boolean answer = inputValidator.validateAnswer(morePurchaseAnswer);
            return answer;
        });
    }
}
