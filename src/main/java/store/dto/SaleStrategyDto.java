package store.dto;

public class SaleStrategyDto {
    private int productQuantityStatus;
    private boolean onePromtion;

    public SaleStrategyDto(int productQuantityStatus, boolean onePromtion) {
        this.productQuantityStatus = productQuantityStatus;
        this.onePromtion = onePromtion;
    }

    public int getProductQuantityStatus() {
        return productQuantityStatus;
    }

    public boolean getOnePromotion() {
        return onePromtion;
    }
}
