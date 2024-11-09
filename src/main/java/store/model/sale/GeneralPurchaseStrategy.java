package store.model.sale;

import store.model.Receipt;
import store.model.Store;

public class GeneralPurchaseStrategy implements SaleStrategy {
    @Override
    public void execute(Store store, Receipt receipt, String productName, int quantity, Object... options) {
        purchaseProductGeneral(store, receipt, productName, quantity);
    }

    private void purchaseProductGeneral(Store store, Receipt receipt, String productName, int quantity) {
        receipt.addPurchase(productName, store.getGenenralProduct().get(productName).getPrice(), quantity);
        store.updateStoreGeneral(productName, quantity);
    }
}
