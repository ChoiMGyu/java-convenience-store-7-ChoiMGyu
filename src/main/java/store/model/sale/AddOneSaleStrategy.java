package store.model.sale;

import store.model.Receipt;
import store.model.Store;
import store.model.StoreConstant;

public class AddOneSaleStrategy implements SaleStrategy {
    private static final int ADD_ONE = 1;

    @Override
    public void execute(Store store, Receipt receipt, String productName, int quantity, Object... options) {
        boolean addOne = (boolean) options[0];
        processPurchase(store, receipt, productName, quantity, addOne);
    }

    private void processPurchase(Store store, Receipt receipt, String productName, int quantity, boolean addOne) {
        int freeItems = calculateFreeItems(store, productName, quantity, addOne);
        int totalQuantity = calculateTotalQuantity(quantity, addOne);

        addPurchasedItemsToReceipt(receipt, productName, totalQuantity, store);
        addGiftItemsToReceiptIfApplicable(receipt, productName, freeItems, store);
        updateStoreInventory(store, productName, totalQuantity);
    }

    private int calculateFreeItems(Store store, String productName, int quantity, boolean addOne) {
        int freeItems = store.getFreePromotion(productName, quantity);
        if (addOne) {
            freeItems += ADD_ONE;
        }
        return freeItems;
    }

    private int calculateTotalQuantity(int quantity, boolean addOne) {
        int totalQuantity = quantity;
        if (addOne) {
            totalQuantity += ADD_ONE;
        }
        return totalQuantity;
    }

    private void addPurchasedItemsToReceipt(Receipt receipt, String productName, int totalQuantity, Store store) {
        int productPrice = store.getPromotionProduct().get(productName).getPrice();
        receipt.addPurchase(productName, productPrice, totalQuantity);
    }

    private void addGiftItemsToReceiptIfApplicable(Receipt receipt, String productName, int freeItems, Store store) {
        if (freeItems != StoreConstant.PROMOTION_NOT_EXIST.getMessage()) {
            int productPrice = store.getPromotionProduct().get(productName).getPrice();
            receipt.addGift(productName, productPrice, freeItems);
        }
    }

    private void updateStoreInventory(Store store, String productName, int totalQuantity) {
        store.updateStorePromotion(productName, totalQuantity);
    }
}
