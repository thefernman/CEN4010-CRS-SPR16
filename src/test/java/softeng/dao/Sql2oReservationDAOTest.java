package softeng.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import softeng.dao.reservations.Sql2oReservationDAO;
import softeng.dao.users.Sql2oUserDAO;
import softeng.dao.vehicles.Sql2oVehicleDAO;
import softeng.model.Reservation;
import softeng.model.User;
import softeng.model.Vehicle;

import static org.junit.Assert.assertEquals;

/**
 * Created by Fernando on 4/23/2016.
 */
public class Sql2oReservationDAOTest {

    private Sql2oReservationDAO reservationDAO;
    private Sql2oUserDAO userDAO;
    private Sql2oVehicleDAO vehicleDAO;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init_test.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        reservationDAO = new Sql2oReservationDAO(sql2o);
        userDAO = new Sql2oUserDAO(sql2o);
        vehicleDAO = new Sql2oVehicleDAO(sql2o);
        //Keep connection open through entire test so that it isn't wiped out
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addedUsersAreReturnedFromFindAll() throws Exception {
        User user = newTestUser();
        Vehicle vehicle = newTestVehicle();
        userDAO.add(user);
        vehicleDAO.add(vehicle);
        Reservation reservation = new Reservation(vehicle.getId(), user.getId(), "holiday dates");
        reservationDAO.add(reservation);
        assertEquals(1, reservationDAO.findAll().size());
    }

    private Vehicle newTestVehicle() {
        return new Vehicle("Compact", 2001, "Honda", "Civic");
    }

    private User newTestUser() {
        return new User("abc@abc.com", "abc123");
    }
}
