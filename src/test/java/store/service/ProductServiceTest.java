package store.service;

import camp.nextstep.edu.missionutils.test.Assertions;
import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.Application;
import store.model.Receipt;
import store.model.Store;
import store.model.product.GeneralProduct;
import store.model.product.Product;
import store.model.product.PromotionProduct;
import store.model.promotion.PromotionType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductServiceTest extends NsTest {
    private Store store;
    private List<Product> productList;
    private Receipt receipt;
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productList = new ArrayList<>();

        productList.add(PromotionProduct.createPromotionProduct("콜라", 1000, 7, "탄산2+1", PromotionType.BUY_TWO_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("콜라", 1000, 10));
        productList.add(PromotionProduct.createPromotionProduct("사이다", 1000, 9, "탄산2+1", PromotionType.BUY_TWO_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("사이다", 1000, 7));
        productList.add(PromotionProduct.createPromotionProduct("오렌지주스", 1800, 9, "MD추천상품", PromotionType.BUY_ONE_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("오렌지주스", 1800, 4));
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
        productList.add(PromotionProduct.createPromotionProduct("컵라면", 1700, 6, "MD추천상품", PromotionType.BUY_ONE_GET_ONE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        productList.add(GeneralProduct.createGeneralProduct("컵라면", 1700, 10));

        store = Store.createStore(productList);
        receipt = Receipt.createReceipt();
        productService = new ProductService();
    }

    @ParameterizedTest
    @DisplayName("프로모션을 진행하는 상품(1+1)일 때, 프로모션의 재고가 홀수일 때 구매를 진행한다. ex) [오렌지주스-9]")
    @CsvSource({
            "'[오렌지주스-1]', 'Y', 'N', 'N', '오렌지주스2', '오렌지주스1'",
            "'[오렌지주스-1]', 'N', 'N', 'N', '오렌지주스1', 'null'",
            "'[오렌지주스-7]', 'Y', 'N', 'N', '오렌지주스8', '오렌지주스4'",
            "'[오렌지주스-7]', 'N', 'N', 'N', '오렌지주스7', '오렌지주스3'"
    })
    public void 원플원_재고홀수_구매(String purchase, String addOne, String memberShip, String more, String general, String gift) {
        assertSimpleTest(() -> {
            run(purchase, addOne, memberShip, more);
            assertThat(output().replaceAll("\\s", "")).contains(general);
            if (gift != null && !"null".equals(gift)) {
                assertThat(output().replaceAll("\\s", "")).contains(gift);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("프로모션을 진행하는 상품(1+1)일 때, 프로모션의 재고가 홀수일 때 구매를 진행한다. ex) [오렌지주스-9]")
    @CsvSource({
            "'[오렌지주스-8]', 'N', 'N', '오렌지주스8', '오렌지주스4'",
            "'[오렌지주스-9]', 'N', 'N', '오렌지주스9', '오렌지주스4'"
    })
    public void 원플원_재고홀수_구매_질문없음(String purchase, String memberShip, String more, String general, String gift) {
        assertSimpleTest(() -> {
            run(purchase, memberShip, more);
            assertThat(output().replaceAll("\\s", "")).contains(general);
            if (gift != null && !"null".equals(gift)) {
                assertThat(output().replaceAll("\\s", "")).contains(gift);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("프로모션을 진행하는 상품(1+1)일 때, 프로모션의 재고가 홀수일 때 구매를 진행한다. ex) [오렌지주스-9]")
    @CsvSource({
            "'[오렌지주스-10]', 'Y', 'N', 'N', '오렌지주스10', '오렌지주스4'",
            "'[오렌지주스-10]', 'N', 'N', 'N', '오렌지주스8', '오렌지주스4'"
    })
    public void 원플원_재고홀수_구매_제외프로모션(String purchase, String notPromotion, String memberShip, String more, String general, String gift) {
        assertSimpleTest(() -> {
            run(purchase, notPromotion, memberShip, more);
            assertThat(output().replaceAll("\\s", "")).contains(general);
            if (gift != null && !"null".equals(gift)) {
                assertThat(output().replaceAll("\\s", "")).contains(gift);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("프로모션을 진행하는 상품(1+1)일 때, 프로모션의 재고가 짝수일 때 구매를 진행한다. ex) 컵라면 재고 = 6")
    @CsvSource({
            "'[컵라면-1]', 'Y', 'N', 'N', '컵라면2', '컵라면1'",
            "'[컵라면-1]', 'N', 'N', 'N', '컵라면1', 'null'",
            "'[컵라면-5]', 'Y', 'N', 'N', '컵라면6', '컵라면3'",
            "'[컵라면-5]', 'N', 'N', 'N', '컵라면5', '컵라면2'"
    })
    public void 원플원_재고짝수_구매(String purchase, String addOne, String memberShip, String more, String general, String gift) {
        assertSimpleTest(() -> {
            run(purchase, addOne, memberShip, more);
            assertThat(output().replaceAll("\\s", "")).contains(general);
            if (gift != null && !"null".equals(gift)) {
                assertThat(output().replaceAll("\\s", "")).contains(gift);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("프로모션을 진행하는 상품(1+1)일 때, 프로모션의 재고가 짝수일 때 구매를 진행한다. ex) 컵라면 재고 = 6")
    @CsvSource({
            "'[컵라면-2]', 'N', 'N', '컵라면2', '컵라면1'",
            "'[컵라면-4]', 'N', 'N', '컵라면4', '컵라면2'",
            "'[컵라면-6]', 'N', 'N', '컵라면6', '컵라면3'"
    })
    public void 원플원_재고짝수_질문없음(String purchase, String memberShip, String more, String general, String gift) {
        assertSimpleTest(() -> {
            run(purchase, memberShip, more);
            assertThat(output().replaceAll("\\s", "")).contains(general);
            if (gift != null && !"null".equals(gift)) {
                assertThat(output().replaceAll("\\s", "")).contains(gift);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("프로모션을 진행하는 상품(1+1)일 때, 프로모션의 재고가 짝수일 때 구매를 진행한다. ex) 컵라면 재고 = 6")
    @CsvSource({
            "'[컵라면-7]', 'Y', 'N', 'N', '컵라면7', '컵라면3'",
            "'[컵라면-7]', 'N', 'N', 'N', '컵라면6', '컵라면3'"
    })
    public void 원플원_재고짝수_제외프로모션(String purchase, String notPromotion, String memberShip, String more, String general, String gift) {
        assertSimpleTest(() -> {
            run(purchase, notPromotion, memberShip, more);
            assertThat(output().replaceAll("\\s", "")).contains(general);
            if (gift != null && !"null".equals(gift)) {
                assertThat(output().replaceAll("\\s", "")).contains(gift);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("프로모션을 진행하는 상품(2+1)일 때, 프로모션의 재고가 홀수일 때 구매를 진행한다. ex) 사이다 재고 = 9")
    @CsvSource({
            "'[콜라-2]', 'Y', 'N', 'N', '콜라3', '콜라1'",
            "'[콜라-2]', 'N', 'N', 'N', '콜라2', 'null'",
            "'[콜라-5]', 'Y', 'N', 'N', '콜라6', '콜라2'",
            "'[콜라-5]', 'N', 'N', 'N', '콜라5', '콜라1'",
            "'[콜라-8]', 'Y', 'N', 'N', '콜라9', '콜라3'",
            "'[콜라-8]', 'N', 'N', 'N', '콜라8', '콜라2'"
    })
    public void 투플원_재고홀수_구매(String purchase, String addOne, String memberShip, String more, String general, String gift) {
        assertSimpleTest(() -> {
            run(purchase, addOne, memberShip, more);
            assertThat(output().replaceAll("\\s", "")).contains(general);
            if (gift != null && !"null".equals(gift)) {
                assertThat(output().replaceAll("\\s", "")).contains(gift);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("프로모션을 진행하는 상품(2+1)일 때, 프로모션의 재고가 홀수일 때 구매를 진행한다. ex) 사이다 재고 = 9")
    @CsvSource({
            "'[콜라-3]', 'N', 'N', '콜라3', '콜라1'",
            "'[콜라-4]', 'N', 'N', '콜라4', '콜라1'",
            "'[콜라-6]', 'N', 'N', '콜라6', '콜라2'",
            "'[콜라-7]', 'N', 'N', '콜라7', '콜라2'",
            "'[콜라-9]', 'N', 'N', '콜라9', '콜라3'"
    })
    public void 투플원_재고홀수_구매_질문없음(String purchase, String memberShip, String more, String general, String gift) {
        assertSimpleTest(() -> {
            run(purchase, memberShip, more);
            assertThat(output().replaceAll("\\s", "")).contains(general);
            if (gift != null && !"null".equals(gift)) {
                assertThat(output().replaceAll("\\s", "")).contains(gift);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("프로모션을 진행하는 상품(2+1)일 때, 프로모션의 재고가 홀수일 때 구매를 진행한다. ex) 사이다 재고 = 9")
    @CsvSource({
            "'[콜라-10]', 'Y', 'N', 'N', '콜라10', '콜라3'",
            "'[콜라-10]', 'N', 'N', 'N', '콜라9', '콜라3'",
            "'[콜라-11]', 'Y', 'N', 'N', '콜라11', '콜라3'",
            "'[콜라-11]', 'N', 'N', 'N', '콜라9', '콜라3'"
    })
    public void 투플원_재고홀수_구매_제외프로모션(String purchase, String notPromotion, String memberShip, String more, String general, String gift) {
        assertSimpleTest(() -> {
            run(purchase, notPromotion, memberShip, more);
            assertThat(output().replaceAll("\\s", "")).contains(general);
            if (gift != null && !"null".equals(gift)) {
                assertThat(output().replaceAll("\\s", "")).contains(gift);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("프르모션을 진행하는 상품(2+1)일 때, 프로모션의 재고가 짝수일 때 구매를 진행한다. ex) 콜라 재고 = 10")
    @CsvSource({
            "'[사이다-2]', 'Y', 'N', 'N', '사이다3', '사이다1'",
            "'[사이다-2]', 'N', 'N', 'N', '사이다2', 'null'",
            "'[사이다-5]', 'Y', 'N', 'N', '사이다6', '사이다2'",
            "'[사이다-5]', 'N', 'N', 'N', '사이다5', '사이다1'",
            "'[사이다-8]', 'Y', 'N', 'N', '사이다9', '사이다3'",
            "'[사이다-8]', 'N', 'N', 'N', '사이다8', '사이다2'"
    })
    public void 투플원_재고짝수_구매(String purchase, String addOne, String memberShip, String more, String general, String gift) {
        assertSimpleTest(() -> {
            run(purchase, addOne, memberShip, more);
            assertThat(output().replaceAll("\\s", "")).contains(general);
            if (gift != null && !"null".equals(gift)) {
                assertThat(output().replaceAll("\\s", "")).contains(gift);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("프르모션을 진행하는 상품(2+1)일 때, 프로모션의 재고가 짝수일 때 구매를 진행한다. ex) 사이다 재고 = 10")
    @CsvSource({
            "'[사이다-3]', 'N', 'N', '사이다3', '사이다1'",
            "'[사이다-4]', 'N', 'N', '사이다4', '사이다1'",
            "'[사이다-6]', 'N', 'N', '사이다6', '사이다2'",
            "'[사이다-7]', 'N', 'N', '사이다7', '사이다2'",
            "'[사이다-9]', 'N', 'N', '사이다9', '사이다3'"
    })
    public void 투플원_재고짝수_구매_질문없음(String purchase, String memberShip, String more, String general, String gift) {
        assertSimpleTest(() -> {
            run(purchase, memberShip, more);
            assertThat(output().replaceAll("\\s", "")).contains(general);
            if (gift != null && !"null".equals(gift)) {
                assertThat(output().replaceAll("\\s", "")).contains(gift);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("프르모션을 진행하는 상품(2+1)일 때, 프로모션의 재고가 짝수일 때 구매를 진행한다. ex) 사이다 재고 = 10")
    @CsvSource({
            "'[사이다-10]', 'Y', 'N', 'N', '사이다10', '사이다3'",
            "'[사이다-10]', 'N', 'N', 'N', '사이다9', '사이다3'"
    })
    public void 투플원_재고짝수_구매_제외프로모션(String purchase, String notPromotion, String memberShip, String more, String general, String gift) {
        assertSimpleTest(() -> {
            run(purchase, notPromotion, memberShip, more);
            assertThat(output().replaceAll("\\s", "")).contains(general);
            if (gift != null && !"null".equals(gift)) {
                assertThat(output().replaceAll("\\s", "")).contains(gift);
            }
        });
    }

    @ParameterizedTest
    @DisplayName("프로모션을 진행하지 않는 상품일 때, 구매를 진행한다.")
    @CsvSource({
            "'[물-10]','N','N','물10'"
    })
    public void 일반상품_구매(String purchase, String memberShip, String more, String general) {
        assertSimpleTest(() -> {
            run(purchase, memberShip, more);
            assertThat(output().replaceAll("\\s", "")).contains(general);
        });
    }

    @Test
    @DisplayName("제품을 구매하고 추가 구매를 진행 여부에 따라 다음을 진행한다. 재고는 최신으로 업데이트 되어 있다.")
    public void 다시구매() {

    }

    @Test
    @DisplayName("멤버십 할인은 8000원까지만 허용된다")
    public void 멤버십할인_최대() {

    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }

}

