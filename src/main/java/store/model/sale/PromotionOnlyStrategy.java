package store.model.sale;

import store.model.Receipt;
import store.model.Store;

public class PromotionOnlyStrategy implements SaleStrategy {
    @Override
    public void execute(Store store, Receipt receipt, String productName, int quantity, Object... options) {
        purchaseProductPromotion(store, receipt, productName, quantity);
    }

    private void purchaseProductPromotion(Store store, Receipt receipt, String productName, int quantity) {
        int free = store.getFreePromotion(productName, quantity);

        receipt.addPurchase(productName, store.getPromotionProduct().get(productName).getPrice(), quantity);
        receipt.addGift(productName, store.getPromotionProduct().get(productName).getPrice(), free);
        store.updateStorePromotion(productName, quantity);
    }
}
