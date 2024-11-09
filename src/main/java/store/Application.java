package store;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        InputView.welcome();
        List<String> item = InputView.readItem();
        System.out.println(item);

    }
}
