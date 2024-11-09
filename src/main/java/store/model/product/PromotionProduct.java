package store.model.product;

import java.time.LocalDate;

public class PromotionProduct extends Product {
    private final String promotionName;
    private final String promotionType;
    private final LocalDate start_date;
    private final LocalDate end_date;

    private PromotionProduct(String name, int price, int quantity, String promotionName, String promotionType, LocalDate start_date, LocalDate end_date) {
        super(name, price, quantity);
        this.promotionName = promotionName;
        this.promotionType = promotionType;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public static PromotionProduct createPromotionProduct(String name, int price, int quantity, String promotionName, String promotionType, LocalDate start_date, LocalDate end_date) {
        return new PromotionProduct(name, price, quantity, promotionName, promotionType, start_date, end_date);
    }

    @Override
    public String toString() {
        return super.toString() + " " + promotionName;
    }
}
