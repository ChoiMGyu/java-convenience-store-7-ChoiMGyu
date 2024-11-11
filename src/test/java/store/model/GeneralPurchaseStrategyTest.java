package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.model.product.GeneralProduct;
import store.model.product.Product;
import store.model.sale.GeneralPurchaseStrategy;
import store.model.sale.SaleStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GeneralPurchaseStrategyTest {
    private Store store;
    private List<Product> productList;
    private Receipt receipt;

    private SaleStrategy saleStrategy;

    @BeforeEach
    public void setUp() {
        productList = new ArrayList<>();

        productList.add(GeneralProduct.createGeneralProduct("물", 500, 10));

        store = Store.createStore(productList);
        saleStrategy = new GeneralPurchaseStrategy();
        receipt = Receipt.createReceipt();
    }

    @ParameterizedTest
    @DisplayName("프로모션을 진행하지 않는 상품을 구매한다")
    @CsvSource({
            "'물',10, 10"
    })
    public void 프로모션_X_상품구매(String productName, int quantity, int expectedTotalQuantity) {
        saleStrategy.execute(store, receipt, productName, quantity);

        assertThat(store.getGenenralProduct().get(productName).getQuantity()).isEqualTo(0);
        assertThat(receipt.getPurchase().get(productName).getQuantity()).isEqualTo(expectedTotalQuantity);
    }
}
