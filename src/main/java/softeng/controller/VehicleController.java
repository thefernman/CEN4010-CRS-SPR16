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

    String datasource = "jdbc:h2:~/CarRental.db";
    Sql2o sql2o = new Sql2o(String.format("%s;INIT=RUNSCRIPT from 'classpath:db/init.sql'", datasource), "", "");
    private VehicleDAO vehDAO= new Sql2oVehicleDAO(sql2o);

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

        for (int i = 0; i < all.size(); i++) {
            System.out.println(all.get(i).getInfo());
        }

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

    public void populateDBWithDummyCars(){
        for (int i = 0; i < 5; i++) {
            Vehicle newVeh = new Vehicle("midsize", 200 + i, "manufacturer" + i , "model"+i);
            try{
                vehDAO.add(newVeh);
                System.out.println(newVeh.getModel() + " added to database");
            }catch(Exception e){
                System.out.println("Error from adding vehicle");
            }
        }

        for (int i = 0; i < 5; i++) {
            Vehicle newVeh = new Vehicle("compact", 200 + i, "manufacturer" + i , "model"+i);
            try{
                vehDAO.add(newVeh);
                System.out.println(newVeh.getModel() + " added to database");
            }catch(Exception e){
                System.out.println("Error from adding vehicle");
            }
        }

        for (int i = 0; i < 5; i++) {
            Vehicle newVeh = new Vehicle("suv", 200 + i, "manufacturer" + i , "model"+i);
            try{
                vehDAO.add(newVeh);
                System.out.println(newVeh.getModel() + " added to database");
            }catch(Exception e){
                System.out.println("Error from adding vehicle");
            }
        }

        List<Vehicle> veh = vehDAO.findAll();
        for(int i=0; i < veh.size(); i++){
            System.out.println(veh.get(i).getId());
        }
    }


}
