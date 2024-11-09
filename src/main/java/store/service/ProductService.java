package store.service;

import store.dto.SaleStrategyDto;
import store.model.Receipt;
import store.model.Store;
import store.model.product.PromotionProduct;
import store.model.promotion.PromotionType;

public class ProductService {
    public void purchaseProductGeneral(Store store, Receipt receipt, String productName, int purchase) {
        receipt.addPurchase(productName, store.getGenenralProduct().get(productName).getPrice(), purchase);
        store.updateStoreGeneral(productName, purchase);
    }

    public boolean confirmPromotion(Store store, String productName) {
        return store.hasPromotion(productName);
    }

    public boolean confirmAddPromotion(Store store, String productName, int purchase) {
        if (store.hasPromotion(productName) && store.enoughQuantity(productName, purchase) > 0) {
            return store.checkAddOne(productName, purchase);
        }
        return false;
    }

    public SaleStrategyDto confirmOnePromotion(Store store, String productName, int purchase) {
        PromotionProduct product = store.getPromotionProduct().get(productName);
        int productQuantityStatus = store.enoughQuantity(productName, purchase);
        boolean answer = product != null && product.getPromotionType() == PromotionType.BUY_ONE_GET_ONE;

        return new SaleStrategyDto(productQuantityStatus, answer);
    }

    public int calculatePartial(Store store, String productName, int purchase) {
        return store.calculatePartial(productName, purchase);
    }
}
