package softeng.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import softeng.dao.vehicles.Sql2oVehicleDAO;
import softeng.model.Vehicle;

import static org.junit.Assert.assertEquals;

/**
 * Created by Fernando on 4/23/2016.
 */
public class Sql2oVehicleDAOTest {
    private Sql2oVehicleDAO vehicleDAO;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init_test.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        vehicleDAO = new Sql2oVehicleDAO(sql2o);
        //Keep connection open through entire test so that it isn't wiped out
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addedVehicleAreReturnedFromFindAll() throws Exception {
        Vehicle vehicle = newTestVehicle();
        vehicleDAO.add(vehicle);
        assertEquals(1, vehicleDAO.findAll().size());
    }

    private Vehicle newTestVehicle() {
        return new Vehicle("Compact", 2001, "Honda", "Civic");
    }
}
