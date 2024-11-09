package store.model;

import store.model.product.GeneralProduct;
import store.model.product.Product;
import store.model.product.PromotionProduct;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Store {
    private final Map<String, GeneralProduct> generalProduct;
    private final Map<String, PromotionProduct> promotionProduct;

    private Store(Map<String, GeneralProduct> generalProduct, Map<String, PromotionProduct> promotionProduct) {
        this.generalProduct = new LinkedHashMap<>(generalProduct);
        this.promotionProduct = new LinkedHashMap<>(promotionProduct);
    }

    public static Store createStore(List<Product> products) {
        Map<String, GeneralProduct> generalProduct = new LinkedHashMap<>();
        Map<String, PromotionProduct> promotionProduct = new LinkedHashMap<>();

        for (Product product : products) {
            if (product instanceof GeneralProduct) {
                generalProduct.put(product.getName(), (GeneralProduct) product);
            }

            if (product instanceof PromotionProduct) {
                promotionProduct.put(product.getName(), (PromotionProduct) product);
            }
        }

        return new Store(generalProduct, promotionProduct);
    }
}
