package store.model.file;

import store.model.product.GeneralProduct;
import store.model.product.Product;
import store.model.product.PromotionProduct;
import store.model.promotion.Promotion;
import store.model.promotion.PromotionType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductFileReader {
    private static final String PRODUCT_DELIMITER = ",";
    private static final String EMPTY_PROMOTION = "null";
    private static final int PRODUCT_NAME_IDX = 0;
    private static final int PRODUCT_PRICE_IDX = 1;
    private static final int PRODUCT_QUANTITY_IDX = 2;
    private static final int PRODUCT_PROMOTION_IDX = 3;

    public List<Product> read(String filePath, List<Promotion> promotions) throws IOException {
        Map<String, List<Product>> productMap = readProductsFromFile(filePath, promotions);
        return productMap.values().stream()
                .flatMap(this::addOutOfStockProductIfNeeded)
                .collect(Collectors.toList());
    }

    private Map<String, List<Product>> readProductsFromFile(String filePath, List<Promotion> promotions) throws IOException {
        Map<String, List<Product>> productMap = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Skip header
            reader.lines()
                    .map(line -> createProduct(line, promotions))
                    .filter(Objects::nonNull)
                    .forEach(product -> addProductToMap(productMap, product));
        }
        return productMap;
    }

    private Product createProduct(String line, List<Promotion> promotions) {
        String[] productArgs = splitProductLine(line);
        String name = productArgs[PRODUCT_NAME_IDX];
        int price = parsePrice(productArgs[PRODUCT_PRICE_IDX]);
        int quantity = parseQuantity(productArgs[PRODUCT_QUANTITY_IDX]);
        String promotionName = productArgs[PRODUCT_PROMOTION_IDX];

        return createProductBasedOnPromotion(name, price, quantity, promotionName, promotions);
    }

    private String[] splitProductLine(String line) {
        return line.split(PRODUCT_DELIMITER);
    }

    private int parsePrice(String price) {
        return Integer.parseInt(price);
    }

    private int parseQuantity(String quantity) {
        return Integer.parseInt(quantity);
    }

    private void addProductToMap(Map<String, List<Product>> productMap, Product product) {
        productMap.computeIfAbsent(product.getName(), k -> new ArrayList<>()).add(product);
    }

    private Product createProductBasedOnPromotion(String name, int price, int quantity, String promotionName, List<Promotion> promotions) {
        if (isPromotionProduct(promotionName)) {
            return createPromotionProduct(name, price, quantity, promotionName, promotions);
        }
        return GeneralProduct.createGeneralProduct(name, price, quantity);
    }

    private boolean isPromotionProduct(String promotionName) {
        return !EMPTY_PROMOTION.equals(promotionName);
    }

    private Product createPromotionProduct(String name, int price, int quantity, String promotionName, List<Promotion> promotions) {
        return findPromotionByName(promotionName, promotions)
                .map(promotion -> buildPromotionProduct(name, price, quantity, promotion, promotionName))
                .orElse(null);
    }

    private Optional<Promotion> findPromotionByName(String promotionName, List<Promotion> promotions) {
        return promotions.stream()
                .filter(p -> p.getName().equals(promotionName))
                .findFirst();
    }

    private Product buildPromotionProduct(String name, int price, int quantity, Promotion promotion, String promotionName) {
        PromotionType promotionType = PromotionType.of(promotion.getBuy(), promotion.getGet());
        return PromotionProduct.createPromotionProduct(name, price, quantity, promotionName, promotionType,
                promotion.getStart_date(), promotion.getEnd_date());
    }

    private Stream<Product> addOutOfStockProductIfNeeded(List<Product> productList) {
        if (productList.size() == 1 && productList.get(0) instanceof PromotionProduct) {
            Product promotionProduct = productList.get(0);
            productList.add(GeneralProduct.createGeneralProduct(promotionProduct.getName(), promotionProduct.getPrice(), 0));
        }
        return productList.stream();
    }
}
