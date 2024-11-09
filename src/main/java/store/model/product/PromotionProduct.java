package store.model.product;

import camp.nextstep.edu.missionutils.DateTimes;
import store.model.promotion.PromotionType;

import java.time.LocalDate;

public class PromotionProduct extends Product {
    private final String promotionName;
    private final PromotionType promotionType;
    private final LocalDate start_date;
    private final LocalDate end_date;

    private PromotionProduct(String name, int price, int quantity, String promotionName, PromotionType promotionType, LocalDate start_date, LocalDate end_date) {
        super(name, price, quantity);
        this.promotionName = promotionName;
        this.promotionType = promotionType;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public static PromotionProduct createPromotionProduct(String name, int price, int quantity, String promotionName, PromotionType promotionType, LocalDate start_date, LocalDate end_date) {
        return new PromotionProduct(name, price, quantity, promotionName, promotionType, start_date, end_date);
    }

    public boolean hasQuantity() {
        LocalDate localDate = DateTimes.now().toLocalDate();
        return localDate.isAfter(start_date) && localDate.isBefore(end_date) && quantity > QUANTITY_MINIMUM;
    }

    @Override
    public String toString() {
        return super.toString() + " " + promotionName;
    }
}
