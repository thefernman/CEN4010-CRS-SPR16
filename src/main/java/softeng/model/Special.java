package softeng.model;

/**
 * Created by Fernando on 4/12/2016.
 */
public class Special {

    private int id;
    private String name;
    private int discount;

    public Special(String name, int discount) {
        this.discount = discount;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
