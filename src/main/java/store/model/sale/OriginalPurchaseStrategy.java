package store.model.sale;

import store.model.Receipt;
import store.model.Store;

public class OriginalPurchaseStrategy implements SaleStrategy {
    private static final int PROMOTION_NOT_EXIST = 0;

    @Override
    public void execute(Store store, Receipt receipt, String productName, int quantity, Object... options) {
        int partial = (int) options[0];
        boolean answer = (boolean) options[1];
        purchaseProductOriginal(store, receipt, productName, quantity, partial, answer);
    }

    private void purchaseProductOriginal(Store store, Receipt receipt, String productName, int quantity, int partial, boolean answer) {
        if (answer) {
            handlePromotionPurchase(store, receipt, productName, quantity, partial);
            return;
        }

        handleOriginalPurchase(store, receipt, productName, quantity, partial);
    }

    private void handlePromotionPurchase(Store store, Receipt receipt, String productName, int quantity, int partial) {
        int free = store.getFreePromotion(productName, quantity - partial);
        int promotionQuantity = store.getPromotionProduct().get(productName).getQuantity();
        int notFree = quantity - promotionQuantity;

        updateToReceipt(store, receipt, productName, quantity, free);
        updateToStore(store, receipt, productName, promotionQuantity, notFree);
    }

    private void handleOriginalPurchase(Store store, Receipt receipt, String productName, int quantity, int partial) {
        quantity -= partial;
        int free = store.getFreePromotion(productName, quantity);
        int notFree = quantity - free;

        updateToReceipt(store, receipt, productName, quantity, free);
        updateToStore(store, receipt, productName, PROMOTION_NOT_EXIST, notFree);
    }

    private void updateToReceipt(Store store, Receipt receipt, String productName, int quantity, int free) {
        receipt.addPurchase(productName, store.getPromotionProduct().get(productName).getPrice(), quantity);
        if(free != 0) receipt.addGift(productName, store.getPromotionProduct().get(productName).getPrice(), free);
    }

    private void updateToStore(Store store, Receipt receipt, String productName, int promotionQuantity, int notFree) {
        if(promotionQuantity != PROMOTION_NOT_EXIST) {
            store.updateStorePromotion(productName, promotionQuantity);
        }
        store.updateStoreGeneral(productName, notFree);
    }
}
