package view.dto;

public class ProductDto {
    private String name;
    private int quantity;

    public ProductDto(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
