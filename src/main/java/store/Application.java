package store;

import camp.nextstep.edu.missionutils.Console;
import store.controller.StoreController;
import view.InputView;
import view.OutputView;
import view.validator.InputValidator;

public class Application {
    public static void main(String[] args) {
        InputView inputView = new InputView(Console::readLine, new InputValidator());
        OutputView outputView = new OutputView();

        StoreController storeController = new StoreController(inputView, outputView);
        storeController.run();
    }
}
