package store;


public class Product {
    private String name;
    private int price;
    private int quantity;
    private String promotion;

    Product(String name, int price, int quantity, String promotion){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public boolean isItName(String input) {
        return input.equals(name);
    }

    public void canBuy(int input) {
        if(input > quantity){
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    public boolean isItPromotion() {
        return promotion != null;
    }

    public String getName() {
        return this.name;
    }

}

