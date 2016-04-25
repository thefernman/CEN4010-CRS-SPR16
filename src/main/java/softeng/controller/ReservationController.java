package softeng.controller;

import org.sql2o.Sql2o;
import softeng.dao.reservations.ReservationDAO;
import softeng.dao.reservations.Sql2oReservationDAO;
import softeng.model.Reservation;

import java.util.List;

/**
 * Created by Jason on 4/24/2016.
 */
public class ReservationController {

    private ReservationDAO reservDAO;

    public ReservationController(Sql2o sql2o) {
        this.reservDAO = new Sql2oReservationDAO(sql2o);
    }

    public List<Reservation> findAllReservations(){
        return reservDAO.findAll();
    }

    public List<Reservation> getReservationByUserId(int id){
        return reservDAO.findByUserId(id);
    }

    public void reserveVehicle(int vehId, int userId){
        Reservation reserve = new Reservation(vehId, userId, "tuesday");
        try{
            reservDAO.add(reserve);
        }catch(Exception e){
            System.out.println("Error adding reservation");
        }
    }

    public List<Reservation> getAllReservations(){
       return reservDAO.findAll();
    }
}
