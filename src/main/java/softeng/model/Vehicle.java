package softeng.model;

/**
 * Created by Fernando on 4/12/2016.
 */
public class Vehicle {

    private int id;
    private String type;
    private int year;
    private String manufacturer;
    private String model;
    private boolean reserved;

    public Vehicle(String type, int year, String manufacturer, String model) {
        this.type = type;
        this.year = year;
        this.manufacturer = manufacturer;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public String getInfo(){
        return "Vehicle Info: " +id +", " + type + ", "+ year + ", "+ manufacturer + ", "+ model +", isReserved=" + reserved;
    }
}
