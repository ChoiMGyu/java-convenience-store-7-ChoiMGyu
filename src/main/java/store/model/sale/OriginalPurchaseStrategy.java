package store.model.sale;

import store.model.Receipt;
import store.model.Store;

public class OriginalPurchaseStrategy implements SaleStrategy {
    @Override
    public void execute(Store store, Receipt receipt, String productName, int quantity, Object... options) {
        int partial = (int) options[0];
        boolean answer = (boolean) options[1];
        purchaseProductOriginal(store, receipt, productName, quantity, partial, answer);
    }

    private void purchaseProductOriginal(Store store, Receipt receipt, String productName, int quantity, int partial, boolean answer) {
        if (answer) {
            int free = store.getFreePromotion(productName, quantity - partial);
            int promotionQuantity = store.getPromotionProduct().get(productName).getQuantity();
            int notFree = quantity - promotionQuantity;

            receipt.addPurchase(productName, store.getPromotionProduct().get(productName).getPrice(), quantity);
            receipt.addGift(productName, store.getPromotionProduct().get(productName).getPrice(), free);
            store.updateStoreGeneral(productName, notFree);
            store.updateStorePromotion(productName, promotionQuantity);
        } else {
            quantity -= partial;
            int free = store.getFreePromotion(productName, quantity);
            int notFree = quantity - free;

            receipt.addPurchase(productName, store.getPromotionProduct().get(productName).getPrice(), quantity);
            receipt.addGift(productName, store.getPromotionProduct().get(productName).getPrice(), free);
            store.updateStorePromotion(productName, notFree);
        }
    }
}
