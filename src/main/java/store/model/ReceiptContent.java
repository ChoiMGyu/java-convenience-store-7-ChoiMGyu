package store.model;

public class ReceiptContent {
    private int price;
    private int quantity;

    public ReceiptContent(int price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public int calculateProductPerMoney() {
        return price * quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
