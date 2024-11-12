package store.model.product;

public class GeneralProduct extends Product {
    private GeneralProduct(String name, int price, int quantity) {
        super(name, price, quantity);
    }

    public static GeneralProduct createGeneralProduct(String name, int price, int quantity) {
        return new GeneralProduct(name, price, quantity);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
