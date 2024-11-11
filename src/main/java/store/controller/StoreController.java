package store.controller;

import store.dto.SaleStrategyDto;
import store.model.Receipt;
import store.model.ReceiptContent;
import store.model.Store;
import store.model.StoreConstant;
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

            storeProcess(products, store);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void storeProcess(List<Product> products, Store store) {
        while (true) {
            printMenu(products);
            List<ProductDto> productDtos = inputView.readItem(store);

            startStore(productDtos, store);
            if (!shouldContinuePurchase()) break;
        }
    }

    private void startStore(List<ProductDto> productDtos, Store store) {
        Receipt receipt = Receipt.createReceipt();
        processProducts(store, productDtos, receipt);

        boolean answerDiscount = inputView.readMemberDiscount();
        printReceipt(receipt, answerDiscount);
    }

    private void printMenu(List<Product> products) {
        outputView.printGreeting();
        outputView.printItems(products);
    }

    private void processProducts(Store store, List<ProductDto> productDtos, Receipt receipt) {
        for (ProductDto productDto : productDtos) {
            if (!productService.confirmPromotion(store, productDto.getName())) {
                productService.purchaseProductGeneral(store, receipt, productDto.getName(), productDto.getQuantity());
            }
            if (productService.confirmPromotion(store, productDto.getName())) {
                SaleStrategy saleStrategy = chooseStrategy(store, productDto.getName(), productDto.getQuantity());
                applySaleStrategy(store, receipt, productDto.getName(), productDto.getQuantity(), saleStrategy);
            }
        }
    }

    private void applySaleStrategy(Store store, Receipt receipt, String productName, int purchaseCount, SaleStrategy saleStrategy) {
        if (saleStrategy instanceof AddOneSaleStrategy) {
            applyAddOneSaleStrategy(store, receipt, productName, purchaseCount, saleStrategy);
            return;
        }
        if (saleStrategy instanceof PromotionOnlyStrategy) {
            applyPromotionOnlySaleStrategy(store, receipt, productName, purchaseCount, saleStrategy);
            return;
        }
        if (saleStrategy instanceof OriginalPurchaseStrategy) {
            applyOriginalPurchaseStrategy(store, receipt, productName, purchaseCount, saleStrategy);
        }
    }

    private void applyAddOneSaleStrategy(Store store, Receipt receipt, String productName, int purchaseCount, SaleStrategy saleStrategy) {
        boolean answer = inputView.readAddItem(productName, ADD_ONE);
        saleStrategy.execute(store, receipt, productName, purchaseCount, answer);
    }

    private void applyPromotionOnlySaleStrategy(Store store, Receipt receipt, String productName, int purchaseCount, SaleStrategy saleStrategy) {
        saleStrategy.execute(store, receipt, productName, purchaseCount);
    }

    private void applyOriginalPurchaseStrategy(Store store, Receipt receipt, String productName, int purchaseCount, SaleStrategy saleStrategy) {
        boolean answer = inputView.readPartialPayment(productName, productService.calculatePartial(store, productName, purchaseCount));
        int partialCount = productService.calculatePartial(store, productName, purchaseCount);
        saleStrategy.execute(store, receipt, productName, purchaseCount, partialCount, answer);
    }


    private SaleStrategy chooseStrategy(Store store, String productName, int purchaseCount) {
        if (!productService.confirmPromotion(store, productName)) {
            return new GeneralPurchaseStrategy();
        }

        if (productService.confirmAddPromotion(store, productName, purchaseCount)) {
            return new AddOneSaleStrategy();
        }

        SaleStrategyDto strategyDto = productService.confirmOnePromotion(store, productName, purchaseCount);

        if (strategyDto.getProductQuantityStatus() == StoreConstant.QUANTITY_MORE.getMessage()) {
            if (strategyDto.getOnePromotion() || purchaseCount % 2 == 1) {
                return new PromotionOnlyStrategy();
            }
            return new OriginalPurchaseStrategy();
        }

        if (strategyDto.getProductQuantityStatus() == StoreConstant.PURCHASE_MORE.getMessage()) {
            return new OriginalPurchaseStrategy();
        }

        return new PromotionOnlyStrategy();
    }

    private void printReceipt(Receipt receipt, boolean answerDiscount) {
        ReceiptContent receiptContent = discountService.calculateTotalMoney(receipt);
        int promotionDiscount = discountService.calculatePromotionDiscount(receipt);
        int memberShipDiscount = discountService.calculateMemberShipDiscount(receipt, answerDiscount);
        int purchaseMoney = discountService.calculatePurchase(receipt, answerDiscount);

        outputView.printReceipt(receipt, receiptContent, promotionDiscount, memberShipDiscount, purchaseMoney);
    }

    private boolean shouldContinuePurchase() {
        return inputView.readMorePurchase();
    }

    private List<Promotion> readPromotions() throws IOException {
        PromotionFileReader promotionFileReader = new PromotionFileReader();

        String path = getClass().getClassLoader().getResource("promotions.md").getPath();
        return promotionFileReader.read(path);
    }

    private List<Product> readProducts(List<Promotion> promotions) throws IOException {
        ProductFileReader productFileReader = new ProductFileReader();

        String path = getClass().getClassLoader().getResource("products.md").getPath();
        return productFileReader.read(path, promotions);
    }

}
