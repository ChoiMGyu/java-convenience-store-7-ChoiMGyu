package store.controller;

import store.dto.SaleStrategyDto;
import store.model.Receipt;
import store.model.Store;
import store.model.file.ProductFileReader;
import store.model.file.PromotionFileReader;
import store.model.product.Product;
import store.model.promotion.Promotion;
import store.model.sale.*;
import store.service.DiscountService;
import store.service.ProductService;
import view.InputView;
import view.OutputView;
import view.dto.ProductDto;

import java.io.IOException;
import java.util.List;

public class StoreController {
    private static final int ADD_ONE = 1;

    private final ProductService productService;
    private final DiscountService discountService;
    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(ProductService productService, DiscountService discountService, InputView inputView, OutputView outputView) {
        this.productService = productService;
        this.discountService = discountService;
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
                processProducts(store, productDtos, receipt);

                boolean answerDiscount = inputView.readMemberDiscount();
                applyDiscount(receipt, answerDiscount);

                outputView.printReceipt(receipt);

                if (!shouldContinuePurchase()) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void processProducts(Store store, List<ProductDto> productDtos, Receipt receipt) {
        for (ProductDto productDto : productDtos) {
            String productName = productDto.getName();
            int purchaseCount = productDto.getQuantity();

            if (!productService.confirmPromotion(store, productName)) {
                productService.purchaseProductGeneral(store, receipt, productName, productDto.getQuantity());
            }

            if (productService.confirmPromotion(store, productName)) {
                SaleStrategy saleStrategy = chooseStrategy(store, productName, purchaseCount);
                applySaleStrategy(store, receipt, productName, purchaseCount, saleStrategy);
            }
        }
    }

    private void applySaleStrategy(Store store, Receipt receipt, String productName, int purchaseCount, SaleStrategy saleStrategy) {
        if (saleStrategy instanceof AddOneSaleStrategy) {
            boolean answer = inputView.readAddItem(productName, ADD_ONE);
            saleStrategy.execute(store, receipt, productName, purchaseCount, answer);
        } else if (saleStrategy instanceof PromotionOnlyStrategy) {
            saleStrategy.execute(store, receipt, productName, purchaseCount);
        } else if (saleStrategy instanceof OriginalPurchaseStrategy) {
            boolean answer = inputView.readPartialPayment(productName, productService.calculatePartial(store, productName, purchaseCount));
            saleStrategy.execute(store, receipt, productName, purchaseCount, productService.calculatePartial(store, productName, purchaseCount), answer);
        }
    }

    private SaleStrategy chooseStrategy(Store store, String productName, int purchaseCount) {
        if (!productService.confirmPromotion(store, productName)) {
            return new GeneralPurchaseStrategy();
        }

        if (productService.confirmAddPromotion(store, productName, purchaseCount)) {
            return new AddOneSaleStrategy();
        }

        SaleStrategyDto strategyDto = productService.confirmOnePromotion(store, productName, purchaseCount);

        if (strategyDto.getProductQuantityStatus() == 0) {
            if (strategyDto.getOnePromotion() || purchaseCount % 2 == 1) {
                return new PromotionOnlyStrategy();
            }
            return new OriginalPurchaseStrategy();
        }

        if (strategyDto.getProductQuantityStatus() == -1) {
            return new OriginalPurchaseStrategy();
        }

        return new PromotionOnlyStrategy();
    }

    private void applyDiscount(Receipt receipt, boolean answerDiscount) {
        discountService.calculateMemberShipDiscount(receipt, answerDiscount);
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
