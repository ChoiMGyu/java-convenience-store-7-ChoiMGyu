package store.model.product;

import view.exception.StoreException;

import java.text.NumberFormat;
import java.util.Locale;

public abstract class Product {
    protected static final int PRICE_MINIMUM = 0;
    protected static final int QUANTITY_MINIMUM = 0;
    protected static final String PRICE_MIN_ERROR = "상품은 가격이 1원 이상이어야 합니다.";
    protected static final String QUANTITY_MIN_ERROR = "상품은 한 개 이상 존재해야 합니다.";

    protected final String name;
    protected final int price;
    protected int quantity;

    public Product(String name, int price, int quantity) {
        validatePrice(price);
        validateQuantity(quantity);
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    private static void validatePrice(int price) {
        if (price < PRICE_MINIMUM) {
            throw new StoreException(PRICE_MIN_ERROR);
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity < QUANTITY_MINIMUM) {
            throw new StoreException(QUANTITY_MIN_ERROR);
        }
    }

    public void purchaseProduct(int quantity) {
        this.quantity -= quantity;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);
        String formattedPrice = formatter.format(price) + "원";

        if (quantity == QUANTITY_MINIMUM) {
            return String.format("- %s %s 재고 없음", name, formattedPrice);
        }
        return String.format("- %s %s %d개", name, formattedPrice, quantity);
    }
}


