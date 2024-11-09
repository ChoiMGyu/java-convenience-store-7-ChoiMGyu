package store.model.promotion;

import view.exception.StoreException;

import java.time.LocalDate;

public class Promotion {
    private static final int BUY_VALUE_ONE = 1;
    private static final int BUY_VALUE_TWO = 2;
    private static final int GET_VALUE_ONE = 1;
    private static final String BUY_VALUE_ERROR = "프로모션은 1개 또는 2개를 구입했을 때만 적용될 수 있습니다.";
    private static final String GET_VALUE_ERROR = "프로모션의 무료 증정은 1개만 가능합니다.";
    private static final String LOCALDATE_AFTER_ERROR = "프로모션의 시작 날짜는 종료 날짜보다 앞서야 합니다.";

    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate start_date;
    private final LocalDate end_date;

    private Promotion(String name, int buy, int get, LocalDate start_date, LocalDate end_date) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public static Promotion createPromotion(String name, int buy, int get, LocalDate start_date, LocalDate end_date) {
        validateBuy(buy);
        validateGet(get);
        validateLocalDate(start_date, end_date);
        return new Promotion(name, buy, get, start_date, end_date);
    }

    private static void validateBuy(int buy) {
        if (!(buy == BUY_VALUE_ONE || buy == BUY_VALUE_TWO)) {
            throw new StoreException(BUY_VALUE_ERROR);
        }
    }

    private static void validateGet(int get) {
        if (get != GET_VALUE_ONE) {
            throw new StoreException(GET_VALUE_ERROR);
        }
    }

    private static void validateLocalDate(LocalDate start_date, LocalDate end_date) {
        if (start_date.isAfter(end_date)) {
            throw new StoreException(LOCALDATE_AFTER_ERROR);
        }
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }
}
