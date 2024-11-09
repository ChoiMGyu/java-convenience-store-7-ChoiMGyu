package store.model;

import store.dto.FreeProductDto;
import store.model.product.GeneralProduct;
import store.model.product.Product;
import store.model.product.PromotionProduct;

import java.util.*;
import java.util.stream.Stream;

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

    public boolean isExistProduct(String name) {
        return generalProduct.containsKey(name) || promotionProduct.containsKey(name);
    }

    public boolean isExistQuantity(String name, int quantity) {
        int allQuantity = Stream.of(generalProduct, promotionProduct)
                .map(map -> map.get(name))
                .filter(Objects::nonNull)
                .mapToInt(product -> product.getQuantity())
                .sum();

        return allQuantity >= quantity;
    }

    public boolean hasPromotion(String name) {
        return promotionProduct.containsKey(name) && promotionProduct.get(name).hasQuantity();
    }

    public int enoughQuantity(String name, int quantity) {
        if (promotionProduct.containsKey(name)) {
            PromotionProduct promotionProduct = this.promotionProduct.get(name);
            return Integer.compare(promotionProduct.getQuantity(), quantity);
        }
        return 0;
    }

    public boolean checkAddOne(String name, int quantity) {
        FreeProductDto freeProductDto = promotionProduct.get(name).getPromotionType().calculateFreeProducts(quantity);
        return freeProductDto.getAddOne();
    }

    public int calculatePartial(String name, int quantity) {
        int totalBuyGet = promotionProduct.get(name).getPromotionType().getTotalBuyGet();
        int promotionQuantity = promotionProduct.get(name).getQuantity();
        return quantity - (totalBuyGet * (promotionQuantity / totalBuyGet));
    }

    public int getFreePromotion(String name, int quantity) {
        FreeProductDto freeProductDto = promotionProduct.get(name).getPromotionType().calculateFreeProducts(quantity);
        return freeProductDto.getFreeProduct();
    }

    public void updateStorePromotion(String name, int quantity) {
        PromotionProduct product = promotionProduct.get(name);
        if (product != null) {
            product.purchaseProduct(quantity);
            promotionProduct.put(name, product);
        }
    }

    public void updateStoreGeneral(String name, int quantity) {
        GeneralProduct product = generalProduct.get(name);
        if (product != null) {
            product.purchaseProduct(quantity);
            generalProduct.put(name, product);
        }
    }

    public Map<String, GeneralProduct> getGenenralProduct() {
        return Collections.unmodifiableMap(generalProduct);
    }

    public Map<String, PromotionProduct> getPromotionProduct() {
        return Collections.unmodifiableMap(promotionProduct);
    }
}
