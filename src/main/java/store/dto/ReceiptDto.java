package store.dto;

public class ReceiptDto {
    private int price;
    private int quantity;

    public ReceiptDto(int price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
