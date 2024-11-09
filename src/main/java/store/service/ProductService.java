package store.service;

import store.model.Store;
import store.model.product.PromotionProduct;
import store.model.promotion.PromotionType;

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

    public boolean confirmOnePromotion(Store store, String productName) {
        PromotionProduct product = store.getPromotionProduct().get(productName);
        boolean answer = product != null && product.getPromotionType() == PromotionType.BUY_ONE_GET_ONE;

        return answer;
    }

    public int calculatePartial(Store store, String productName, int purchase) {
        return store.calculatePartial(productName, purchase);
    }
}
