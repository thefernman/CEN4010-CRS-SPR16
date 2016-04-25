package softeng.dao.vehicles;

import softeng.exc.DAOException;
import softeng.model.Vehicle;

import java.util.List;

/**
 * Created by Fernando on 4/12/2016.
 */
public interface VehicleDAO {

    //TODO: Maybe change this to boolean?
    void add(Vehicle vehicle) throws DAOException;
    List<Vehicle> findAll();

    List<Vehicle> findAllByType(String type);

    Vehicle findById(int id);

    void updateVehicleInDB(Vehicle vehicle);
}
