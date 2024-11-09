package store.service;

import store.model.Store;

public class ProductService {
    public boolean confirmPromotion(Store store, String productName) {
        return store.hasPromotion(productName);
    }
}
