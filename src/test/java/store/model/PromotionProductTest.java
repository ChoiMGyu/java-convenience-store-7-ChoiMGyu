package store;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.model.product.PromotionProduct;
import store.model.promotion.PromotionType;

import java.time.LocalDate;

public class PromotionProductTest {
    @ParameterizedTest
    @DisplayName("현재 날짜가 프로모션 기간에 속하고, 재고가 1개 이상인 경우 프로모션을 진행한다")
    @CsvSource({
            "true, 2024-11-07, 2024-11-15, 10, true",
            "false, 2024-11-07, 2024-11-09, 0, false",
            "false, 2024-11-10, 2024-11-12, 10, false",
            "false, 2024-11-01, 2024-11-06, 10, false"
    })
    void 프로모션_진행중인지(boolean expected, String startDate, String endDate, int quantity, boolean expectedResult) {
        // given
        PromotionProduct promotionProduct = PromotionProduct.createPromotionProduct(
                "콜라", 1000, quantity, "2+1", PromotionType.BUY_TWO_GET_ONE,
                LocalDate.parse(startDate), LocalDate.parse(endDate)
        );

        // when & then
        Assertions.assertThat(promotionProduct.hasQuantity()).isEqualTo(expectedResult);
    }
}
