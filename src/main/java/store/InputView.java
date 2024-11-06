package store;

import camp.nextstep.edu.missionutils.Console;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class InputView {
    static final int NAME_INDEX = 0;
    static final int PRICE_INDEX = 1;
    static final int QUANTITY_INDEX = 2;
    static final int PROMOTION_INDEX = 3;

    public static String readItem() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String input = Console.readLine();
        // TODO
        return input;
    }

    // TODO
    public static void welcome() {
        System.out.println("안녕하세요. W편의점 입니다.\n현재 보유하고 있는 상품입니다.\n");
        List<String> products_Info = readProducts();
        printProducts(products_Info);
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

    private static String formatPrice(int price) {
        return String.format("%,d", price);
    }
}
