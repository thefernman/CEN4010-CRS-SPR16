package softeng.controller;

import org.sql2o.Sql2o;
import softeng.dao.vehicles.Sql2oVehicleDAO;
import softeng.dao.vehicles.VehicleDAO;
import softeng.exc.DAOException;
import softeng.model.Vehicle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Fernando on 4/13/2016.
 */
public class VehicleController {

    private VehicleDAO vehicleDAO;

    public VehicleController(Sql2o sql2o) {
        vehicleDAO = new Sql2oVehicleDAO(sql2o);
    }

    public void addVehicle(Vehicle vehicle) {
        try {
            vehicleDAO.add(vehicle);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    public List<Vehicle> getAllVehicles(){
       return vehicleDAO.findAll();
    }

    public List<Vehicle> getVehicleByType(String type){
        return vehicleDAO.findAllByType(type);
    }

    public List<Vehicle> getUnreservedVehiclesByType(String type){
        return vehicleDAO.findAllByType(type)
                .stream()
                .filter(vehicle -> !vehicle.isReserved())
                .collect(Collectors.toList());
    }

    public List<Vehicle> getAllUnreservedVehicles(){
        return vehicleDAO.findAll()
                .stream()
                .filter(vehicle -> !vehicle.isReserved())
                .collect(Collectors.toList());
    }

    public Vehicle getVehicleById(int id){
        return vehicleDAO.findById(id);
    }

    public void markAsReserved(Vehicle vehicle){
        vehicle.setReserved(true);
        vehicleDAO.updateVehicleInDB(vehicle);
    }
}
