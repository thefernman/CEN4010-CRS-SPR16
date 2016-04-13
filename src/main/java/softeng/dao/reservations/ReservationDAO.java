package softeng.dao.reservations;

import softeng.exc.DAOException;
import softeng.model.Reservation;

import java.util.List;

/**
 * Created by Fernando on 4/12/2016.
 */
public interface ReservationDAO {
    //TODO: Maybe change this to boolean?
    void add(Reservation reserve) throws DAOException;
    List<Reservation> findAll();
    List<Reservation> findByUserId(int userId);
}
