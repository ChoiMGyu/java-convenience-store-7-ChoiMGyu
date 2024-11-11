package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.model.product.GeneralProduct;
import store.model.product.Product;
import store.model.product.PromotionProduct;
import store.model.promotion.PromotionType;
import store.model.sale.PromotionOnlyStrategy;
import store.model.sale.SaleStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PromotionOnlyStrategyTest {
    private Store store;
    private List<Product> productList;
    private Receipt receipt;

    private SaleStrategy saleStrategy;

    @BeforeEach
    public void setUp() {
        productList = new ArrayList<>();

        productList.add(PromotionProduct.createPromotionProduct("콜라", 1000, 10, "탄산2+1", PromotionType.BUY_TWO_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("콜라", 1000, 10));
        productList.add(PromotionProduct.createPromotionProduct("사이다", 1000, 9, "탄산2+1", PromotionType.BUY_TWO_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("사이다", 1000, 7));
        productList.add(PromotionProduct.createPromotionProduct("오렌지주스", 1800, 9, "MD추천상품", PromotionType.BUY_ONE_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("오렌지주스", 1800, 4));
        productList.add(PromotionProduct.createPromotionProduct("컵라면", 1700, 6, "MD추천상품", PromotionType.BUY_ONE_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("컵라면", 1700, 10));

        store = Store.createStore(productList);
        saleStrategy = new PromotionOnlyStrategy();
        receipt = Receipt.createReceipt();
    }

    @ParameterizedTest
    @DisplayName("1+1 행사 상품인 경우 - 재고 홀수")
    @CsvSource({
            "오렌지주스, 8, 8, 4",
            "오렌지주스, 9, 9, 4"
    })
    public void 원플원_재고홀수(String productName, int quantity, int expectedTotalQuantity, String expectedGiftStock) {
        Integer giftStock = null;
        if (!"null".equals(expectedGiftStock)) {
            giftStock = Integer.valueOf(expectedGiftStock);
        }

        saleStrategy.execute(store, receipt, productName, quantity);

        int purchasedQuantity = receipt.getPurchase().get(productName).getQuantity();
        assertThat(purchasedQuantity).isEqualTo(expectedTotalQuantity);

        if (giftStock == null) {
            assertThat(receipt.getGift().get(productName)).isNull();
        } else {
            int giftQuantity = receipt.getGift().get(productName).getQuantity();
            assertThat(giftQuantity).isEqualTo(giftStock);
        }
    }


    @ParameterizedTest
    @DisplayName("1+1 행사 상품인 경우 - 재고 짝수")
    @CsvSource({
            "컵라면, 2, 2, 1",
            "컵라면, 4, 4, 2",
            "컵라면, 6, 6, 3"
    })
    public void 원플원_재고짝수(String productName, int quantity, int expectedTotalQuantity, String expectedGiftStock) {
        Integer giftStock = null;
        if (!"null".equals(expectedGiftStock)) {
            giftStock = Integer.valueOf(expectedGiftStock);
        }

        saleStrategy.execute(store, receipt, productName, quantity);

        int purchasedQuantity = receipt.getPurchase().get(productName).getQuantity();
        assertThat(purchasedQuantity).isEqualTo(expectedTotalQuantity);

        if (giftStock == null) {
            assertThat(receipt.getGift().get(productName)).isNull();
        } else {
            int giftQuantity = receipt.getGift().get(productName).getQuantity();
            assertThat(giftQuantity).isEqualTo(giftStock);
        }
    }


    @ParameterizedTest
    @DisplayName("2+1 행사 상품인 경우 - 재고 홀수")
    @CsvSource({
            "콜라, 3, 3, 1",
            "콜라, 4, 4, 1",
            "콜라, 6, 6, 2",
            "콜라, 7, 7, 2",
            "콜라, 9, 9, 3"
    })
    public void 투플원_재고홀수(String productName, int quantity, int expectedTotalQuantity, String expectedGiftStock) {
        Integer giftStock = null;
        if (!"null".equals(expectedGiftStock)) {
            giftStock = Integer.valueOf(expectedGiftStock);
        }

        saleStrategy.execute(store, receipt, productName, quantity);

        int purchasedQuantity = receipt.getPurchase().get(productName).getQuantity();
        assertThat(purchasedQuantity).isEqualTo(expectedTotalQuantity);

        if (giftStock == null) {
            assertThat(receipt.getGift().get(productName)).isNull();
        } else {
            int giftQuantity = receipt.getGift().get(productName).getQuantity();
            assertThat(giftQuantity).isEqualTo(giftStock);
        }
    }

    @ParameterizedTest
    @DisplayName("2+1 행사 상품인 경우 - 재고 짝수")
    @CsvSource({
            "사이다, 3, 3, 1",
            "사이다, 4, 4, 1",
            "사이다, 6, 6, 2",
            "사이다, 7, 7, 2",
            "사이다, 9, 9, 3"
    })
    public void 투플원_재고짝수(String productName, int quantity, int expectedTotalQuantity, String expectedGiftStock) {
        Integer giftStock = null;
        if (!"null".equals(expectedGiftStock)) {
            giftStock = Integer.valueOf(expectedGiftStock);
        }

        saleStrategy.execute(store, receipt, productName, quantity);

        int purchasedQuantity = receipt.getPurchase().get(productName).getQuantity();
        assertThat(purchasedQuantity).isEqualTo(expectedTotalQuantity);

        if (giftStock == null) {
            assertThat(receipt.getGift().get(productName)).isNull();
        } else {
            int giftQuantity = receipt.getGift().get(productName).getQuantity();
            assertThat(giftQuantity).isEqualTo(giftStock);
        }
    }
}
