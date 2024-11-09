package store.model.sale;

import store.model.Receipt;
import store.model.Store;

public interface SaleStrategy {
    void execute(Store store, Receipt receipt, String productName, int quantity, Object... options);
}
