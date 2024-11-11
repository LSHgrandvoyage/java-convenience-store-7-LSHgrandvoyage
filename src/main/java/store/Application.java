package store;

import camp.nextstep.edu.missionutils.Console;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        boolean continue_purchase_flag = true;
        while (continue_purchase_flag) {
            InputView.welcome();
            List<Product> original_items = InputView.readItem();
            List<Product> items = InputView.promoting();
            double discount = InputView.membershipping();
            OutputView.printBills(original_items, items, discount);
            continue_purchase_flag = continuePurchase();
        }
    }

    private static boolean continuePurchase() {
        OutputView.printContinue();
        try {
            return InputView.validateYesNo(Console.readLine());
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            continuePurchase();
        }
        throw new IllegalArgumentException("[ERROR] Some error is occurred in continue purchasing process");
    }
}
