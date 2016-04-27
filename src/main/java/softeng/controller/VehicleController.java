package softeng.controller;

import org.sql2o.Sql2o;
import softeng.dao.vehicles.Sql2oVehicleDAO;
import softeng.dao.vehicles.VehicleDAO;
import softeng.exc.DAOException;
import softeng.model.Vehicle;

import java.util.List;

/**
 * Created by Fernando on 4/13/2016.
 */
public class VehicleController {

    private VehicleDAO vehDAO;

    public VehicleController(Sql2o sql2o) {
        vehDAO = new Sql2oVehicleDAO(sql2o);
    }

    public void addVehicle(Vehicle vehicle) {
        try {
            vehDAO.add(vehicle);
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    public List<Vehicle> getAllVehicles(){
       return vehDAO.findAll();
    }

    public List<Vehicle> getVehicleByType(String type){
        return vehDAO.findAllByType(type);
    }

    public List<Vehicle> getUnreservedVehicleByType(String type){
        List<Vehicle> all = vehDAO.findAllByType(type);

        all.forEach(System.out::println);

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).isReserved()){
                System.out.println(all.get(i).getInfo() + " was removed from available list");
                all.remove(i);
                i--;
            }
        }
        return all;
    }

    public Vehicle getVehicleById(int id){
        return vehDAO.findById(id);
    }

    public void markAsReserved(Vehicle veh){
        veh.setReserved(true);
        vehDAO.updateVehicleInDB(veh);
    }
}
