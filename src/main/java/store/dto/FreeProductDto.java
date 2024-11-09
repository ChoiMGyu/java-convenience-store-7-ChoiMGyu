package store.dto;

public class FreeProductDto {
    private final int freeProduct;
    private final boolean addOne;

    public FreeProductDto(int freeProduct, boolean addOne) {
        this.freeProduct = freeProduct;
        this.addOne = addOne;
    }

    public int getFreeProduct() {
        return freeProduct;
    }

    public boolean getAddOne() {
        return addOne;
    }
}
