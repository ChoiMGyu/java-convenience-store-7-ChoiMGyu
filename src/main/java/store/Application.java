package store;

import camp.nextstep.edu.missionutils.Console;
import store.controller.StoreController;
import store.service.ProductService;
import view.InputView;
import view.OutputView;
import view.validator.InputValidator;

public class Application {
    public static void main(String[] args) {
        ProductService productService = new ProductService();
        InputView inputView = new InputView(Console::readLine, new InputValidator());
        OutputView outputView = new OutputView();

        StoreController storeController = new StoreController(productService, inputView, outputView);
        storeController.run();
    }
}
