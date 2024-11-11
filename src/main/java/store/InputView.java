package store;

import camp.nextstep.edu.missionutils.Console;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputView {
    public enum ProductInfo{
        NAME(0), PRICE(1), QUANTITY(2), PROMOTION(3);
        private final int index;

        ProductInfo(int index){
            this.index = index;
        }

        public int getIndex(){
            return index;
        }
    }

    public enum InputInfo{
        NAME(0), QUANTITY(1);
        private final int index;

        InputInfo(int index){
            this.index = index;
        }

        public int getIndex(){
            return index;
        }

        public static int getSize() {
            return InputInfo.values().length;
        }
    }

    public enum PromotionInfo {
        NAME(0), BUY(1), GET(2), START_DATE(3), END_DATE(4);
        private final int index;

        PromotionInfo(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    static final double MEMBERSHIP_DISCOUNT = 0.3;

    static List<Product> products = new ArrayList<>();
    static List<Promotion> promotions = new ArrayList<>();
    static List<Product> user_products = new ArrayList<>();

    public static void welcome() {
        clearAllData();
        System.out.println("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n");
        List<String> products_info = readProducts();
        List<String> promotions_info = readPromotion();
        printProducts(products_info);
        saveProducts(products_info);
        savePromotions(promotions_info);
    }

    public static void clearAllData() {
        clearUserProducts();
        clearProducts();
        clearPromotions();
    }

    public static List<Product> readItem() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String input = Console.readLine();
        List<String> buy_item = List.of(input.split(","));
        validateInput(buy_item);
        saveUserProducts(buy_item);
        return user_products;
    }

    public static List<Product> promoting() {
        for (Product product : user_products) {
            applyPromotion(product);
        }
        return user_products;
    }

    public static boolean canApplyPromotion(Product user_product) {
        if (findPromotionProduct(user_product) != null) {
            return true;
        }
        return false;
    }

    public static double membershipping() {
        boolean yes_no = getMembership();
        if (yes_no) {
            return discount();
        }
        return 0;
    }

    public static boolean getMembership() {
        OutputView.printMembership();
        String input = Console.readLine();
        try {
            return validateYesNo(input);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            getMembership();
        }
        throw new IllegalArgumentException("[ERROR] Some error is occurred in membership process.");
    }

    public static boolean validateYesNo(String input) {
        if (input.equals("Y")) {
            return true;
        }
        if (input.equals("N")) {
            return false;
        }
        throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
    }


    private static void clearUserProducts() {
        user_products.clear();
    }

    private static void clearProducts() {
        products.clear();
    }

    private static void clearPromotions() {
        promotions.clear();
    }

    private static void applyPromotion(Product user_product){
        if(canApplyPromotion(user_product)){
            quantityLimit(user_product);
            checkBring(user_product);
        }
        if(!(canApplyPromotion(user_product)))
            normalBuy(user_product);
    }

    private static double discount() {
        int pure_total = findPureTotal();
        if (pure_total * MEMBERSHIP_DISCOUNT > 8000) {
            return 8000;
        }
        return pure_total * MEMBERSHIP_DISCOUNT;
    }

    private static int findPureTotal() {
        int pure_total = 0;
        for (Product product : user_products) {
            if (!(product.appliedPromotion()) && findNoPromotionProduct(product) != null) {
                pure_total += findNoPromotionProduct(product).getPrice() * product.getQuantity();
            }
        }
        return pure_total;
    }

    private static void quantityLimit(Product user_product) {
        if (!(findPromotionProduct(user_product).canBuy(user_product))) {
            Product p = findPromotionProduct(user_product);
            int no_promotion = user_product.getQuantity() - findMatchedPromotion(p).getMaxPromotion(p);
            OutputView.printCantPromotion(user_product.getName(), no_promotion);
            boolean yes_no = checkLimitYesNo(user_product);
            checkLimitYesNoAct(yes_no, user_product, no_promotion);
        }
    }

    private static boolean checkLimitYesNo(Product user_product) {
        try {
            boolean yes_no = validateYesNo(Console.readLine());
            return yes_no;
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            quantityLimit(user_product);
        }
        throw new IllegalArgumentException("[ERROR] Some error is occurred in promotion limit check.");
    }

    private static void checkLimitYesNoAct(boolean yes_no, Product user_product, int no_promotion) {
        Product p = findPromotionProduct(user_product);
        Product np = findNoPromotionProduct(user_product);
        if (yes_no) {
            limitYesAct(user_product, p, np);
        }
        if (!(yes_no)) {
            limitNoAct(user_product, p, no_promotion);
        }
    }

    private static void limitYesAct(Product user_product, Product p, Product np) {
        int bonus = finishPromotionQuantity(user_product, p, np);
        user_product.bonusBenefit(bonus);
        user_product.promotionOccur();
    }

    private static int finishPromotionQuantity(Product user_product, Product p, Product np){
        int promotion_quantity = p.getQuantity();
        p.minusQuantity(promotion_quantity);
        np.minusQuantity(user_product.getQuantity() - promotion_quantity);
        int bonus = findMatchedPromotion(p).realBonus(promotion_quantity);
        return bonus;
    }
    private static void limitNoAct(Product user_product, Product p, int no_promotion) {
        p.minusQuantity(user_product.getQuantity() - no_promotion);
        user_product.minusQuantity(no_promotion);
        int bonus = user_product.canGet(findMatchedPromotion(p));
        user_product.bonusBenefit(bonus);
        user_product.promotionOccur();
    }


    private static void normalBuy(Product user_product) {
        if (findPromotionProduct(user_product) != null) {
            int additional_quantity = findPromotionProduct(user_product).minusQuantity(user_product);
            if (findNoPromotionProduct(user_product) != null) {
                findNoPromotionProduct(user_product).minusQuantity(additional_quantity);
            }
        }
    }

    private static void checkBring(Product user_product) {
        if(!(user_product.appliedPromotion())){
            Promotion p = findMatchedPromotion((findPromotionProduct(user_product)));
            if(user_product.omitBonus(p) != 0) {
                checkOmitBonus(user_product, p);
                return;
            }
            checkBonus(user_product, p);
        }
    }

    private static void checkOmitBonus(Product user_product, Promotion p) {
        OutputView.printBring(user_product.getName(), user_product.omitBonus(p));
        boolean yes_no = checkBringYesNo(user_product);
        checkBringYesNoAct(yes_no, user_product, user_product.canGet(p));
    }

    private static void checkBonus(Product user_product, Promotion p) {
        user_product.bonusBenefit(user_product.canGet(p));
        user_product.promotionOccur();
    }

    private static boolean checkBringYesNo(Product user_product) {
        try {
            boolean yes_no = validateYesNo(Console.readLine());
            return yes_no;
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            checkBring(user_product);
        }
        throw new IllegalArgumentException("[ERROR] Some error is occurred in getting Y/N");
    }

    private static void checkBringYesNoAct(boolean yes_no, Product user_product, int bonus) {
        if (yes_no) { //재고처리할것
            user_product.bonusBenefit(bonus + 1);
            user_product.promotionOccur();
        }
        if (!(yes_no)) {
            user_product.bonusBenefit(bonus);
            user_product.promotionOccur();
        }
    }

    private static void saveUserProducts(List<String> buy_item) {
        int price = 0;
        for (String item : buy_item) {
            String item_name = handleName(item);
            int how_many = handleHowMany(item);
            if (findProduct(item_name) != null) {
                price = findProduct(item_name).getPrice();
            }
            user_products.add(new Product(item_name, price, how_many, null, 0, 0));
        }
    }

    private static String handleName(String item) {
        String result = List.of(item.split("-")).get(InputInfo.NAME.getIndex());
        result = result.replaceAll("\\[", "").replaceAll("]", "");
        return result;
    }

    private static int handleHowMany(String item) {
        String how_many = List.of(item.split("-")).get(InputInfo.QUANTITY.getIndex());
        how_many = how_many.replaceAll("\\[", "").replaceAll("]", "");
        return Integer.parseInt(how_many);
    }

    private static Product findProduct(String item_name) {
        for (Product product : products) {
            if (product.isItName(item_name)) {
                return product;
            }
        }
        return null;
    }

    private static Product findNoPromotionProduct(Product p) {
        for (Product product : products) {
            if(product.isItName(p) && !(product.isItPromotion())){
                return product;
            }
        }
        return null;
    }

    private static Product findPromotionProduct(Product p) {
        for (Product product : products) {
            if(product.isItName(p) && product.isItPromotion()){
                Promotion pro = findMatchedPromotion(product);
                return checkAvailablePromotion(pro, product);
            }
        }
        return null;
    }

    private static Product checkAvailablePromotion(Promotion pro, Product product) {
        if(pro != null && pro.isNowPromotion()){
            return product;
        }
        return null;
    }

    private static Promotion findMatchedPromotion(Product p) {
        for (Promotion pro : promotions) {
            if (p.isItPromotion(pro) != null) {
                return pro;
            }
        }
        return null;
    }

    private static void printProducts(List<String> file_lines) {
        for (int i = 1; i < file_lines.size(); i++) {
            List<String> print_line = List.of(file_lines.get(i).split(","));
            System.out.print("- " + print_line.get(ProductInfo.NAME.getIndex()) + " ");
            System.out.print(formatPrice(Integer.parseInt(print_line.get(ProductInfo.PRICE.getIndex()))) + "원 ");
            isItZero(print_line);
            isItNull(print_line);
        }
    }

    private static void saveProducts(List<String> file_lines) {
        for (int i = 1; i < file_lines.size(); i++) {
            List<String> save_line = List.of(file_lines.get(i).split(","));
            String name = save_line.get(ProductInfo.NAME.getIndex());
            int price = Integer.parseInt(save_line.get(ProductInfo.PRICE.getIndex()));
            int quantity = Integer.parseInt(save_line.get(ProductInfo.QUANTITY.getIndex()));
            String promotion = save_line.get(ProductInfo.PROMOTION.getIndex());
            products.add(new Product(name, price, quantity, promotion, 0, 0));
        }
    }

    private static void savePromotions(List<String> file_lines) {
        for (int i = 1; i < file_lines.size(); i++) {
            List<String> save_line = List.of(file_lines.get(i).split(","));
            String name = save_line.get(PromotionInfo.NAME.getIndex());
            int buy = Integer.parseInt(save_line.get(PromotionInfo.BUY.getIndex()));
            int get = Integer.parseInt(save_line.get(PromotionInfo.GET.getIndex()));
            String start_date = save_line.get(PromotionInfo.START_DATE.getIndex());
            String end_date = save_line.get(PromotionInfo.END_DATE.getIndex());
            promotions.add(new Promotion(name, buy, get, start_date, end_date));
        }
    }

    private static void isItZero(List<String> line) {
        if (line.get(ProductInfo.QUANTITY.getIndex()).equals("0")) {
            System.out.print("재고 없음 ");
        }
        if (!(line.get(ProductInfo.QUANTITY.getIndex()).equals("0"))) {
            System.out.print(line.get(ProductInfo.QUANTITY.getIndex()) + "개 ");
        }
    }

    private static void isItNull(List<String> line) {
        if (line.get(ProductInfo.PROMOTION.getIndex()).equals("null")) {
            System.out.print("\n");
        }
        if (!(line.get(ProductInfo.PROMOTION.getIndex()).equals("null"))) {
            System.out.print(line.get(ProductInfo.PROMOTION.getIndex()) + "\n");
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
        try {
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
        try {
            for (String item : buy_item) {
                isItRightFormat(item);
            }
        } catch (IllegalArgumentException e) {
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
        if (!(item.startsWith("["))) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        if (!(item.endsWith("]"))) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    private static void isItOneHyphen(List<String> item_info) {
        if (item_info.size() != InputInfo.getSize()) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    private static void isItExist(List<String> item_info) {
        int exist_flag = 0;
        for (Product product : products) {
            if (product.isItName(item_info.get(InputInfo.NAME.getIndex()))) {
                exist_flag = 1;
            }
        }
        if (exist_flag == 0) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
    }

    private static void canBuyIt(List<String> item_info) {
        int total_quantity = 0;
        for (Product product : products) {
            if (product.isItName(item_info.get(InputInfo.NAME.getIndex()))) {
                total_quantity += product.getQuantity();
            }
        }
        if (total_quantity < Integer.parseInt(item_info.get(InputInfo.QUANTITY.getIndex()))) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }
}
