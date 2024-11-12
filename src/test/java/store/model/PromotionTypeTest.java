package store.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.dto.FreeProductDto;
import store.model.promotion.PromotionType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PromotionTypeTest {

    @Test
    @DisplayName("구매 개수(buy)와 증정 개수(get)를 통해 PromotionType 객체를 만들 수 있다")
    public void 프로모션_타입생성() {
        // given
        int buyOneGetOne = 1;
        int buyTwoGetOne = 2;

        // when & then
        assertEquals(PromotionType.BUY_ONE_GET_ONE, PromotionType.of(buyOneGetOne, 1));
        assertEquals(PromotionType.BUY_TWO_GET_ONE, PromotionType.of(buyTwoGetOne, 1));
        assertEquals(PromotionType.NO_PROMOTION, PromotionType.of(0, 0));
        assertEquals(PromotionType.NO_PROMOTION, PromotionType.of(3, 1));
    }

    @ParameterizedTest
    @DisplayName("2+1 행사, 구매 수량이 주어지면 증정 개수와 1개를 추가할 것인지 구할 수 있다")
    @CsvSource({"1, 0, false", "2, 0, true", "3, 1, false", "4, 1, false", "5, 1, true"})
    public void 사용자_구매수량_알면_증정개수와_1개추가여부_투플원(int quantity, int expectedFree, boolean expectedAddOne) {
        // given
        PromotionType promotionType = PromotionType.BUY_TWO_GET_ONE;

        // when
        FreeProductDto result = promotionType.calculateFreeProducts(quantity);

        // then
        assertEquals(expectedFree, result.getFreeProduct());
        assertEquals(expectedAddOne, result.getAddOne());
    }

    @ParameterizedTest
    @DisplayName("1+1 행사, 구매 수량이 주어지면 증정 개수와 1개를 추가할 것인지 구할 수 있다")
    @CsvSource({"1, 0, true", "2, 1, false", "3, 1, true", "4, 2, false", "5, 2, true"})
    public void 사용자_구매수량_알면_증정개수와_1개추가여부_원플원(int quantity, int expectedFree, boolean expectedAddOne) {
        // given
        PromotionType promotionType = PromotionType.BUY_ONE_GET_ONE;

        // when
        FreeProductDto result = promotionType.calculateFreeProducts(quantity);

        // then
        assertEquals(expectedFree, result.getFreeProduct());
        assertEquals(expectedAddOne, result.getAddOne());
    }
}
