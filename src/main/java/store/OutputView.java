package store;

import java.util.List;

public class OutputView {
    public static void printBills(List<Product> original_items, List<Product> items, double discount) {
        System.out.println("==============W 편의점================");
        System.out.println(String.format("%-19s" + "%-8s" + "%-5s", "상품명", "수량", "금액"));
        printPurchasingProducts(original_items);
        System.out.println("=============증      정===============");
        printPromotionProducts(items);
        System.out.println("=====================================");
        printCalculation(items, discount);
    }

    public static void printBring(String item_name, int how_many) {
        System.out.print("현재 " + item_name + "은(는) " + how_many + "개를 무료로 더 받을 수 있습니다.");
        System.out.println("추가하시겠습니까? (Y/N)");
    }

    public static void printCantPromotion(String item_name, int how_many) {
        System.out.print("현재 " + item_name + " " + how_many + "개는 프로모션 할인이 적용되지 않습니다.");
        System.out.println("그래도 구매하시겠습니까? (Y/N)");
    }

    public static void printMembership() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
    }

    public static void printContinue() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
    }

    public static void printPurchasingProducts(List<Product> items) {
        for (Product item : items) {
            System.out.print(String.format("%-20s", item.getName()));
            System.out.print(String.format("%-10d", item.getQuantity()));
            System.out.print(String.format("%-5s", comma(item.getPrice() * item.getQuantity())));
            System.out.print("\n");
        }
    }

    public static void printPromotionProducts(List<Product> items) {
        for (Product item : items) {
            if (item.appliedPromotion()) {
                System.out.print(String.format("%-19s", item.getName()));
                System.out.print(String.format("%-13d", item.getGot_bonus()));
                System.out.print("\n");
            }
        }
    }

    public static void printCalculation(List<Product> items, double discount) {
        System.out.print(String.format("%-18s" + "%-10s", "총구매액", comma(totalQuantity(items))));
        System.out.print(String.format("%-5s", comma(totalPrice(items))));
        System.out.print("\n");
        System.out.println(String.format("%-28s" + "%-5s", "행사할인", minusComma(totalPromotion(items))));
        System.out.println(String.format("%-27s" + "%-5s", "멤버십할인", minusComma((int) discount)));
        System.out.print(String.format("%-28s", "내실돈"));
        System.out.print(String.format("%-5s", comma(finalResult(totalPrice(items), totalPromotion(items), discount))));
        System.out.print("\n");
    }

    private static String comma(int i) {
        return String.format("%,d", i);
    }

    private static String minusComma(int i) {
        return String.format("-%,d", i);
    }

    private static int totalQuantity(List<Product> items) {
        int total = 0;
        for (Product item : items) {
            total += item.getQuantity();
        }
        return total;
    }

    private static int totalPrice(List<Product> items) {
        int total = 0;
        for (Product item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    private static int totalPromotion(List<Product> items) {
        int total = 0;
        for (Product item : items) {
            if (item.appliedPromotion()) {
                total += item.getPrice() * item.getGot_bonus();
            }
        }
        return total;
    }

    private static int finalResult(int total_price, int total_promotion, double discount) {
        return total_price - total_promotion - (int) discount;
    }
}
