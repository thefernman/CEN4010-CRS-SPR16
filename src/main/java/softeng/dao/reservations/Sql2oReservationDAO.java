package softeng.dao.reservations;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import softeng.exc.DAOException;
import softeng.model.Reservation;

import java.util.List;

/**
 * Created by Fernando on 4/13/2016.
 */
public class Sql2oReservationDAO implements ReservationDAO {

    private final Sql2o sql2o;

    public Sql2oReservationDAO(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Reservation reserve) throws DAOException {
        //TODO: Change sql query..
        String sql = "INSERT INTO reservations(" +
                "vehicle_id, user_id, dates) VALUES (:vehicle_id, :user_id, :dates)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(reserve)
                    .executeUpdate()
                    .getKey();
            reserve.setId(id);
        } catch (Sql2oException ex) {
            throw new DAOException(ex, "problem adding reservations");
        }
    }

    @Override
    public List<Reservation> findAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM reservations")
                    .executeAndFetch(Reservation.class);
        }
    }

    @Override
    public List<Reservation> findByUserId(int userId) {
        try (Connection con = sql2o.open()) {
            //TODO: Check sql query..
            return con.createQuery("SELECT * FROM reservations WHERE user_id = :userId")
                    .addColumnMapping("USER_ID", "userId")
                    .addParameter("userId", userId)
                    .executeAndFetch(Reservation.class);
        }
    }
}
