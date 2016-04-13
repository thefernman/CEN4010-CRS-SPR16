package softeng.model;

/**
 * Created by Fernando on 4/12/2016.
 */
public class Special {

    private int id;
    private int discount;

    public Special(int discount) {
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
