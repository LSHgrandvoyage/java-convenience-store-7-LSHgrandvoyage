package store;


public class Product {
    private final String name;
    private final int price;
    private int quantity;
    private final String promotion;
    private int promotion_flag;
    private int got_bonus;

    Product(String name, int price, int quantity, String promotion, int promotion_flag, int got_bonus) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
        this.promotion_flag = promotion_flag;
        this.got_bonus = got_bonus;
    }

    public boolean isItName(String input) {
        return input.equals(name);
    }

    public boolean isItName(Product p) {
        return p.name.equals(this.name);
    }

    public boolean isItPromotion() {
        return !(promotion.equals("null"));
    }

    public Promotion isItPromotion(Promotion p) {
        if (p.matchPromotion(this.promotion)) {
            return p;
        }
        return null;
    }

    public boolean canBuy(Product p) {
        if (quantity < p.quantity) {
            return false;
        }
        return true;
    }

    public int canGet(Promotion p) {
        int bonus = quantity - p.realBuy(quantity);
        return bonus;
    }

    public int omitBonus(Promotion p) {
        if (p.isItOmit(quantity)) {
            return 1;
        }
        return 0;
    }

    public void minusQuantity(int i) {
        quantity = quantity - i;
    }

    public int minusQuantity(Product p) {
        if (quantity - p.quantity < 0) {
            quantity = 0;
            return p.quantity - quantity;
        }
        quantity = quantity - p.quantity;
        return 0;
    }

    public void bonusBenefit(int i) {
        got_bonus += i;
    }

    public void promotionOccur() {
        promotion_flag = 1;
    }

    public boolean appliedPromotion() {
        return promotion_flag == 1;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getGot_bonus() {
        return got_bonus;
    }
}

