package store.model.file;

import store.model.promotion.Promotion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromotionFileReader {
    private static final String PRODUCT_DELIMITER = ",";
    private static final int PRODUCT_NAME_IDX = 0;
    private static final int PRODUCT_BUY_IDX = 1;
    private static final int PRODUCT_GET_IDX = 2;
    private static final int PRODUCT_START_DATE_IDX = 3;
    private static final int PRODUCT_END_DATE_IDX = 4;

    public List<Promotion> read(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return readPromotions(reader);
        }
    }

    private List<Promotion> readPromotions(BufferedReader reader) throws IOException {
        List<Promotion> promotions = new ArrayList<>();
        reader.readLine();

        String promotionLine;
        while ((promotionLine = reader.readLine()) != null) {
            Promotion promotion = parsePromotion(promotionLine);
            if (promotion != null) {
                promotions.add(promotion);
            }
        }
        return promotions;
    }

    private Promotion parsePromotion(String promotionLine) {
        String[] promotionArgs = promotionLine.split(PRODUCT_DELIMITER);
        String name = promotionArgs[PRODUCT_NAME_IDX];
        int buy = Integer.parseInt(promotionArgs[PRODUCT_BUY_IDX]);
        int get = Integer.parseInt(promotionArgs[PRODUCT_GET_IDX]);
        LocalDate startDate = parseDate(promotionArgs[PRODUCT_START_DATE_IDX]);
        LocalDate endDate = parseDate(promotionArgs[PRODUCT_END_DATE_IDX]);

        return Promotion.createPromotion(name, buy, get, startDate, endDate);
    }

    private LocalDate parseDate(String date) {
        return LocalDate.parse(date);
    }
}
