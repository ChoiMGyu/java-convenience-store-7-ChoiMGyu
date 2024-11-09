package store.model.sale;

import store.model.Receipt;
import store.model.Store;

public class AddOneSaleStrategy implements SaleStrategy {
    private static final int ADD_ONE = 1;
    private static final int ZERO_COUNT_PRODUCT = 0;

    @Override
    public void execute(Store store, Receipt receipt, String productName, int quantity, Object... options) {
        boolean addOne = (boolean) options[0];
        purchaseProductAddOne(store, receipt, productName, quantity, addOne);
    }

    private void purchaseProductAddOne(Store store, Receipt receipt, String productName, int quantity, boolean addOne) {
        int free = store.getFreePromotion(productName, quantity);
        int totalQuantity = quantity;

        if (addOne) {
            totalQuantity += ADD_ONE;
            free += ADD_ONE;
        }

        receipt.addPurchase(productName, store.getPromotionProduct().get(productName).getPrice(), totalQuantity);
        if (free != ZERO_COUNT_PRODUCT) {
            receipt.addGift(productName, store.getPromotionProduct().get(productName).getPrice(), free);
        }
        store.updateStorePromotion(productName, totalQuantity);
    }
}
