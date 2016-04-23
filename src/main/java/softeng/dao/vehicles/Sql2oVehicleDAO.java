package softeng.dao.vehicles;

import softeng.exc.DAOException;
import softeng.model.Vehicle;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

/**
 * Created by Fernando on 4/13/2016.
 */
public class Sql2oVehicleDAO implements VehicleDAO {

    private final Sql2o sql2o;

    public Sql2oVehicleDAO(Sql2o sql2o) {
        //Dependency Injection....
        this.sql2o = sql2o;
    }

    @Override
    public void add(Vehicle vehicle) throws DAOException {
        //TODO: Change sql query..
        String sql = "INSERT INTO vehicles(type, year, manufacturer, model) VALUES (:type, :year, :manufacturer, :model)";
        try (Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql)
                    .bind(vehicle)
                    .executeUpdate()
                    .getKey();
            vehicle.setId(id);
        } catch (Sql2oException ex) {
            throw new DAOException(ex, "problem adding vehicle");
        }
    }

    @Override
    public List<Vehicle> findAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM vehicles")
                    .executeAndFetch(Vehicle.class);
        }
    }

    @Override
    public Vehicle findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM vehicles WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Vehicle.class);
        }
    }
}
