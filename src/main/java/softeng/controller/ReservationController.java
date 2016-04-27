package softeng.controller;

import org.sql2o.Sql2o;
import softeng.dao.reservations.ReservationDAO;
import softeng.dao.reservations.Sql2oReservationDAO;
import softeng.dao.users.Sql2oUserDAO;
import softeng.dao.users.UserDAO;
import softeng.dao.vehicles.Sql2oVehicleDAO;
import softeng.model.Reservation;
import softeng.model.User;
import softeng.model.Vehicle;
import spark.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason on 4/24/2016.
 */
public class ReservationController {

    private final Sql2oVehicleDAO vehicleDAO;
    private ReservationDAO reservationDAO;
    private UserDAO userDAO;

    public ReservationController(Sql2o sql2o) {
        this.reservationDAO = new Sql2oReservationDAO(sql2o);
        this.userDAO = new Sql2oUserDAO(sql2o);
        this.vehicleDAO = new Sql2oVehicleDAO(sql2o);
    }

    public List<Reservation> findAllReservations(){
        return reservationDAO.findAll();
    }

    public List<Reservation> getReservationByUserId(int id){
        return reservationDAO.findByUserId(id);
    }

    public void reserveVehicle(int vehId, int userId){
        Reservation reserve = new Reservation(vehId, userId, "tuesday");
        try{
            reservationDAO.add(reserve);
        }catch(Exception e){
            System.out.println("Error adding reservation");
        }
    }

    public Map<String, Object> returnUsersReservationVehicles(Request request) {
        Map<String, Object> model = new HashMap<>();
        User user= request.session().attribute("user");
        int id = user.getId();
        List<Reservation> userReservation = getReservationByUserId(user.getId());

        List<Vehicle> userVehicles= new ArrayList<>();

        userReservation.forEach(veh -> userVehicles.add(vehicleDAO.findById(veh.getId())));

        model.put("vehicles", userVehicles);
        return model;
    }
}
