package store.model.sale;

import store.model.Receipt;
import store.model.Store;
import store.model.StoreConstant;

public class PromotionOnlyStrategy implements SaleStrategy {
    @Override
    public void execute(Store store, Receipt receipt, String productName, int quantity, Object... options) {
        purchaseProductPromotion(store, receipt, productName, quantity);
    }

    private void purchaseProductPromotion(Store store, Receipt receipt, String productName, int quantity) {
        int free = store.getFreePromotion(productName, quantity);

        receipt.addPurchase(productName, false, store.getPromotionProduct().get(productName).getPrice(), quantity);
        if(free != StoreConstant.PROMOTION_NOT_EXIST.getMessage()) receipt.addGift(productName, store.getPromotionProduct().get(productName).getPrice(), free);
        store.updateStorePromotion(productName, quantity);
    }
}
