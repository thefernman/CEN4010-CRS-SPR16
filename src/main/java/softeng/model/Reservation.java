package softeng.model;

/**
 * Created by Fernando on 4/12/2016.
 */
public class Reservation {

    private int id;
    private int vehicle_id;
    private int user_id;
    private String dates;

    public Reservation(int vehicle_id, int user_id, String dates) {
        this.vehicle_id = vehicle_id;
        this.user_id = user_id;
        this.dates = dates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String toString(){
        return "Id: " + id + ", vehicle_id:" + vehicle_id + ", user_id: " + user_id + ", dates: " + dates;
    }
}
