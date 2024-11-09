package store.controller;

import store.dto.SaleStrategyDto;
import store.model.Receipt;
import store.model.Store;
import store.model.file.ProductFileReader;
import store.model.file.PromotionFileReader;
import store.model.product.Product;
import store.model.promotion.Promotion;
import store.refactorPromotion.*;
import store.service.DiscountService;
import view.InputView;
import view.OutputView;
import view.dto.ProductDto;

import java.io.IOException;
import java.util.List;

public class StoreController {
    private static final int ADD_ONE = 1;

    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        try {
            List<Promotion> promotions = readPromotions();
            List<Product> products = readProducts(promotions);
            Store store = Store.createStore(products);

            while (true) {
                outputView.printGreeting();
                outputView.printItems(products);
                List<ProductDto> productDtos = inputView.readItem(store);

                Receipt receipt = Receipt.createReceipt();

                boolean answerDiscount = inputView.readMemberDiscount();

                outputView.printReceipt(receipt);

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
