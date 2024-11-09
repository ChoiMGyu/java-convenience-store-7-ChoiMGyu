package store.controller;

import store.model.Store;
import store.model.file.ProductFileReader;
import store.model.file.PromotionFileReader;
import store.model.product.Product;
import store.model.promotion.Promotion;
import view.InputView;

import java.io.IOException;
import java.util.List;

public class StoreController {
    private static final int ADD_ONE = 1;

    private final InputView inputView;

    public StoreController(InputView inputView) {
        this.inputView = inputView;
    }

    public void run() {
        try {
            List<Promotion> promotions = readPromotions();
            List<Product> products = readProducts(promotions);
            Store store = Store.createStore(products);

            while (true) {
                if (!shouldContinuePurchase()) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean shouldContinuePurchase() {
        return inputView.readMorePurchase();
    }

    private List<Promotion> readPromotions() throws IOException {
        PromotionFileReader promotionFileReader = new PromotionFileReader();
        return promotionFileReader.read("src\\main\\resources\\promotions.md");
    }

    private List<Product> readProducts(List<Promotion> promotions) throws IOException {
        ProductFileReader productFileReader = new ProductFileReader();
        return productFileReader.read("src\\main\\resources\\products.md", promotions);
    }

}
