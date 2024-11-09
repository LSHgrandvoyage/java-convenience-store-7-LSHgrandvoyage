package store;

import camp.nextstep.edu.missionutils.Console;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class InputView {
    static final int NAME_INDEX = 0;
    static final int PRICE_INDEX = 1;
    static final int QUANTITY_INDEX = 2;
    static final int PROMOTION_INDEX = 3;
    static final int INPUT_ELEMENT_SIZE = 2;
    static final int INPUT_NAME_INDEX = 0;
    static final int INPUT_QUANTITY_INDEX = 1;
    static final int BUY_INDEX = 1;
    static final int GET_INDEX = 2;
    static final int START_DATE_INDEX = 3;
    static final int END_DATE_INDEX = 4;

    static List<Product> products = new ArrayList<>();
    static List<Promotion> promotions = new ArrayList<>();

    public static List<String> readItem() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String input = Console.readLine();
        List<String> buy_item = List.of(input.split(","));
        validateInput(buy_item);
        List<String> validated_input = buy_item;
        return validated_input;
    }

    public static void welcome() {
        System.out.println("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n");
        List<String> products_info = readProducts();
        List<String> promotions_info = readPromotion();
        printProducts(products_info);
        saveProducts(products_info);
        savePromotions(promotions_info);
    }

    private static void printProducts(List<String> file_lines) {
        for (int i = 1; i < file_lines.size(); i++) {
            List<String> print_line = List.of(file_lines.get(i).split(","));
            System.out.print("- " + print_line.get(NAME_INDEX) + " ");
            System.out.print(formatPrice(Integer.parseInt(print_line.get(PRICE_INDEX))) + "원 ");
            isItZero(print_line);
            isItNull(print_line);
        }
    }

    private static void saveProducts(List<String> file_lines) {
        for (int i = 1; i < file_lines.size(); i++) {
            List<String> save_line = List.of(file_lines.get(i).split(","));
            String name = save_line.get(NAME_INDEX);
            int price = Integer.parseInt(save_line.get(PRICE_INDEX));
            int quantity = Integer.parseInt(save_line.get(QUANTITY_INDEX));
            String promotion = save_line.get(PROMOTION_INDEX);
            products.add(new Product(name, price, quantity, promotion));
        }
    }

    private static void savePromotions(List<String> file_lines){
        for(int i = 1; i < file_lines.size(); i++) {
            List<String> save_line = List.of(file_lines.get(i).split(","));
            String name = save_line.get(NAME_INDEX);
            int buy = Integer.parseInt(save_line.get(BUY_INDEX));
            int get = Integer.parseInt(save_line.get(GET_INDEX));
            String start_date = save_line.get(START_DATE_INDEX);
            String end_date = save_line.get(END_DATE_INDEX);
            promotions.add(new Promotion(name, buy, get, start_date, end_date));
        }
    }

    private static void isItZero(List<String> line){
        if(line.get(QUANTITY_INDEX).equals("0")){
            System.out.print("재고 없음 ");
        }
        if(!(line.get(QUANTITY_INDEX).equals("0"))){
            System.out.print(line.get(QUANTITY_INDEX) + "개 ");
        }
    }

    private static void isItNull(List<String> line) {
        if(line.get(PROMOTION_INDEX).equals("null")){
            System.out.print("\n");
        }
        if(!(line.get(PROMOTION_INDEX).equals("null"))){
            System.out.print(line.get(PROMOTION_INDEX) + "\n");
        }
    }

    private static List<String> readProducts() {
        Path file_path = Paths.get("src", "main", "resources", "products.md");
        try {
            List<String> file_lines = Files.readAllLines(file_path);
            return file_lines;
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] Some error is occurred in products file");
        }
    }

    private static List<String> readPromotion() {
        Path file_path = Paths.get("src", "main", "resources", "promotions.md");
        try{
            List<String> file_lines = Files.readAllLines(file_path);
            return file_lines;
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] Some error is occurred in promotions file");
        }
    }

    private static String formatPrice(int price) {
        return String.format("%,d", price);
    }

    private static void validateInput(List<String> buy_item) {
        try{
            for(String item : buy_item){
                isItRightFormat(item);
            }
        }catch (IllegalArgumentException e){
            System.out.println(e);
            readItem();
        }
    }

    private static void isItRightFormat(String item) {
        isItBrackets(item);
        item = item.replaceAll("\\[", "").replaceAll("]", "");
        List<String> item_info = List.of(item.split("-"));
        isItOneHyphen(item_info);
        isItExist(item_info);
        canBuyIt(item_info);
    }

    private static void isItBrackets(String item) {
        if(!(item.startsWith("["))){
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        if(!(item.endsWith("]"))){
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    private static void isItOneHyphen(List<String> item_info) {
        if(item_info.size() != INPUT_ELEMENT_SIZE){
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    private static void isItExist(List<String> item_info) {
        int exist_flag = 0;
        for(Product product : products){
            if(product.isItName(item_info.get(INPUT_NAME_INDEX))){
                exist_flag = 1;
            }
        }
        if(exist_flag == 0){
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
    }

    private static void canBuyIt(List<String> item_info) {
        for(Product product : products){
            if(product.isItName(item_info.get(INPUT_NAME_INDEX))){
                product.canBuy(Integer.parseInt(item_info.get(INPUT_QUANTITY_INDEX)));
            }
        }
    }

    private static boolean canPromotion(List<String> item_info) {
        for(Product product : products) {
            if(product.isItName(item_info.get(INPUT_NAME_INDEX))){
                return product.isItPromotion();
            }
        }
        return false;
    }


}
