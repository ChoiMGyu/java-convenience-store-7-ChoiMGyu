package store.model;

public class ReceiptTitle {
    private String name;
    private boolean isPartial;

    public ReceiptTitle(String name, boolean isPartial) {
        this.name = name;
        this.isPartial = isPartial;
    }

    public String getName() {
        return name;
    }

    public boolean getIsPartial() {
        return isPartial;
    }
}
