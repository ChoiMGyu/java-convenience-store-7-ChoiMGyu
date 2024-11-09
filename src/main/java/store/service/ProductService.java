package store.service;

import store.model.Store;

public class ProductService {
    public boolean confirmPromotion(Store store, String productName) {
        return store.hasPromotion(productName);
    }

    public boolean confirmAddPromotion(Store store, String productName, int purchase) {
        if (store.hasPromotion(productName) && store.enoughQuantity(productName, purchase) > 0) {
            return store.checkAddOne(productName, purchase);
        }
        return false;
    }

    public int calculatePartial(Store store, String productName, int purchase) {
        return store.calculatePartial(productName, purchase);
    }
}
