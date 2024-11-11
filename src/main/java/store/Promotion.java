package store;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Promotion {
    private static String name;
    private static int buy;
    private static int get;
    private static String start_date;
    private static String end_date;

    Promotion(String name, int buy, int get, String start_date, String end_date) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public boolean isNowPromotion() {
        LocalDate current_day = LocalDate.now();
        Date today = stringToDate(localDateToString(current_day));
        Date start = stringToDate(this.start_date);
        Date end = stringToDate(this.end_date);
        if (!(today.after(end)) && !(today.before(start))) {
            return true;
        }
        return false;
    }


    public boolean matchPromotion(String input) {
        if (input == null) {
            return false;
        }
        return input.equals(this.name);
    }

    public static int getMaxPromotion(Product product) {
        return (product.getQuantity() / (get + buy)) * (get + buy);
    }

    public static int realBuy(int how_many) {
        int multiple = get + buy;
        int real_buy = (how_many / multiple) * buy + (how_many % multiple);
        return real_buy;
    }

    public boolean isItOmit(int how_many) {
        int multiple = get + buy;
        if (how_many % multiple == buy) {
            return true;
        }
        return false;
    }

    private static String localDateToString(LocalDate local_date) {
        String date = String.valueOf(local_date);
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] Some error is occurred in date process");
        }
        return date;
    }

    private static Date stringToDate(String string_date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(string_date);
            return date;
        } catch (ParseException e) {
            throw new IllegalArgumentException("[ERROR] Some error is occurred in date calculation");
        }

    }

}
