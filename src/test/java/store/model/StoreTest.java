package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.model.product.GeneralProduct;
import store.model.product.Product;
import store.model.product.PromotionProduct;
import store.model.promotion.PromotionType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StoreTest {
    private Store store;
    private List<Product> productList;

    @BeforeEach
    public void setUp() {
        productList = new ArrayList<>();

        productList.add(PromotionProduct.createPromotionProduct("콜라", 1000, 7, "탄산2+1", PromotionType.BUY_TWO_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("콜라", 1000, 10));
        productList.add(PromotionProduct.createPromotionProduct("사이다", 1000, 8, "탄산2+1", PromotionType.BUY_TWO_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("사이다", 1000, 7));
        productList.add(PromotionProduct.createPromotionProduct("오렌지주스", 1800, 9, "MD추천상품", PromotionType.BUY_ONE_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("오렌지주스", 1800, 0));
        productList.add(PromotionProduct.createPromotionProduct("탄산수", 1200, 5, "MD추천상품", PromotionType.BUY_ONE_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("탄산수", 1200, 0));
        productList.add(GeneralProduct.createGeneralProduct("물", 500, 10));
        productList.add(GeneralProduct.createGeneralProduct("비타민워터", 1500, 6));
        productList.add(PromotionProduct.createPromotionProduct("감자칩", 1500, 5, "반짝할인", PromotionType.BUY_ONE_GET_ONE, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30)));
        productList.add(GeneralProduct.createGeneralProduct("감자칩", 1500, 5));
        productList.add(PromotionProduct.createPromotionProduct("초코바", 1200, 5, "MD추천상품", PromotionType.BUY_ONE_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("초코바", 1200, 5));
        productList.add(GeneralProduct.createGeneralProduct("에너지바", 2000, 5));
        productList.add(GeneralProduct.createGeneralProduct("정식도시락", 6400, 8));
        productList.add(PromotionProduct.createPromotionProduct("컵라면", 1700, 1, "MD추천상품", PromotionType.BUY_ONE_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("컵라면", 1700, 10));

        store = Store.createStore(productList);
    }


    @Test
    @DisplayName("스토어가 잘 만들어졌는지 확인한다")
    public void 스토어_생성() {
        assertNotNull(store, "스토어가 잘 생성되지 않았습니다.");
    }

    @ParameterizedTest
    @DisplayName("상품명에 해당하는 상품이 스토어에 있는지 확인한다")
    @CsvSource({"콜라, true", "게토레이, false"})
    public void 상품명_상점(String name, boolean result) {
        boolean existProduct = store.isExistProduct(name);
        assertEquals(existProduct, result);
    }

    @ParameterizedTest
    @DisplayName("편의점에 사용자가 구매하려는 상품은 구매 수량보다 많다")
    @CsvSource({"콜라, 16, true", "콜라, 21, false"})
    public void 구매수량_상점(String name, int quantity, boolean result) {
        boolean existQuantity = store.isExistQuantity(name, quantity);
        assertEquals(existQuantity, result);
    }

    @ParameterizedTest
    @DisplayName("상품명에 해당하는 상품은 프로모션 진행 중인지 확인한다")
    @CsvSource({"게토레이, false", "콜라, true"})
    public void 상품_프로모션_여부(String name, boolean result) {
        boolean answer = store.hasPromotion(name);
        assertEquals(answer, result);
    }

    @ParameterizedTest
    @DisplayName("프로모션을 하고 있는 상품은 구매 수량 이상인지 확인한다")
    @CsvSource({"콜라, 6, 1", "콜라, 7, 0", "콜라, 8, -1"})
    public void 프로모션_구매수량_비교(String name, int quantity, int result) {
        int answer = store.enoughQuantity(name, quantity);
        assertEquals(answer, result);
    }

    @ParameterizedTest
    @DisplayName("정가로 구매해야 하는 상품의 개수를 구한다")
    @CsvSource({"콜라, 10, 4", "사이다, 9, 3", "감자칩, 6, 2", "감자칩, 7, 3"})
    public void 정가_구매_확인(String name, int quantity, int expect) {
        int partial = store.calculatePartial(name, quantity);
        assertEquals(partial, expect);
    }

    @Test
    @DisplayName("구매 수량과 프로모션에 맞는 증정품의 개수를 반환한다")
    public void 증정품_반환() {

    }

    @ParameterizedTest
    @DisplayName("프로모션 구매 수량만큼 Map을 업데이트한다")
    @CsvSource({"콜라, 5"})
    public void 프로모션_저장소_업데이트(String name, int quantity) {
        store.updateStorePromotion(name, quantity);
        PromotionProduct product = store.getPromotionProduct().get(name);
        assertEquals(product.getQuantity(), 2);
    }

    @ParameterizedTest
    @DisplayName("일반 구매 수량만큼 Map을 업데이트한다")
    @CsvSource({"콜라, 3"})
    public void 일반_저장소_업데이트(String name, int quantity) {
        store.updateStoreGeneral(name, quantity);
        GeneralProduct generalProduct = store.getGenenralProduct().get(name);
        assertEquals(generalProduct.getQuantity(), 7);
    }
}
