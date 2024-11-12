package view.validator;

import store.model.Store;
import view.dto.ProductDto;
import view.exception.StoreException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    private static final String ITEM_SHAPE_ERROR = "올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    private static final String NOT_EXIST_ITEM_ERROR = "존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String QUANTITY_OVER_ERROR = "재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";
    private static final String INCORRECT_ERROR = "잘못된 입력입니다. 다시 입력해 주세요.";
    private static final String ITEMS_REGEX = "\\[([가-힣\\w]+)-(\\d+)](,\\[([가-힣\\w]+)-(\\d+)])*";
    private static final String ITEM_REGEX = "\\[([가-힣\\w]+)-(\\d+)\\]";
    private static final String ITEM_DELIMITER = ",";
    private static final int ANSWER_FIRST = 0;
    private static final int ITEM_NAME_IDX = 1;
    private static final int ITEM_QUANTITY_IDX = 2;

    public List<ProductDto> validateItemInput(Store store, String item) {
        validateBlank(item);
        validateItemFormat(item);
        List<ProductDto> products = parseItems(item);
        validateSameProduct(products);
        validateExistingProducts(store, products);
        validateProductQuantities(store, products);
        return Collections.unmodifiableList(products);
    }

    private void validateItemFormat(String item) {
        if (!item.matches(ITEMS_REGEX)) {
            throw new StoreException(ITEM_SHAPE_ERROR);
        }
    }

    private List<ProductDto> parseItems(String item) {
        List<ProductDto> items = new ArrayList<>();
        String[] itemArray = item.split(ITEM_DELIMITER);

        for (String itemStr : itemArray) {
            ProductDto product = parseSingleItem(itemStr);
            items.add(product);
        }

        return items;
    }

    private ProductDto parseSingleItem(String itemStr) {
        Pattern pattern = Pattern.compile(ITEM_REGEX);
        Matcher matcher = pattern.matcher(itemStr.strip());

        if (matcher.matches()) {
            String name = matcher.group(ITEM_NAME_IDX);
            int quantity = Integer.parseInt(matcher.group(ITEM_QUANTITY_IDX));
            return new ProductDto(name, quantity);
        }

        throw new StoreException(ITEM_SHAPE_ERROR);
    }

    private void validateSameProduct(List<ProductDto> products) {
        long distinctCount = products.stream()
                .map(ProductDto::getName)
                .distinct()
                .count();

        if (distinctCount != products.size()) {
            throw new StoreException(INCORRECT_ERROR);
        }
    }


    private void validateExistingProducts(Store store, List<ProductDto> products) {
        products.stream()
                .filter(product -> !store.isExistProduct(product.getName()))
                .findAny()
                .ifPresent(product -> {
                    throw new StoreException(NOT_EXIST_ITEM_ERROR);
                });
    }

    private void validateProductQuantities(Store store, List<ProductDto> products) {
        products.stream()
                .filter(product -> !store.isExistQuantity(product.getName(), product.getQuantity()))
                .findFirst()
                .ifPresent(product -> {
                    throw new StoreException(QUANTITY_OVER_ERROR);
                });
    }


    public boolean validateAnswer(String answer) {
        validateBlank(answer);
        if ("YN".indexOf(answer.charAt(ANSWER_FIRST)) == -1) {
            throw new StoreException(INCORRECT_ERROR);
        }

        return answer.charAt(ANSWER_FIRST) == 'Y';
    }

    private void validateBlank(String input) {
        if (input == null || input.isBlank()) {
            throw new StoreException(INCORRECT_ERROR);
        }
    }
}
