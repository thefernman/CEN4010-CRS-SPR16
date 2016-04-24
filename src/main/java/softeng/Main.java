package softeng;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.sql2o.Sql2o;
import softeng.dao.reservations.ReservationDAO;
import softeng.dao.reservations.Sql2oReservationDAO;
import softeng.dao.specials.SpecialDAO;
import softeng.dao.specials.Sql2oSpecialDAO;
import softeng.dao.users.Sql2oUserDAO;
import softeng.dao.users.UserDAO;
import softeng.dao.vehicles.Sql2oVehicleDAO;
import softeng.dao.vehicles.VehicleDAO;
import softeng.model.User;
import softeng.model.Vehicle;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

public class Main {
    public static void main(String[] args) {

        //TODO: Maybe change the name to Route.java??
        staticFileLocation("/public");

        //TODO: Figure out mysql driver stuff..
        String datasource = "jdbc:h2:~/CarRental.db";
        Sql2o sql2o = new Sql2o(
                String.format("%s;INIT=RUNSCRIPT from 'classpath:db/init.sql'", datasource), "", "");

        UserDAO userDAO = new Sql2oUserDAO(sql2o);
        VehicleDAO vehicleDAO = new Sql2oVehicleDAO(sql2o);
        ReservationDAO reservationDAO = new Sql2oReservationDAO(sql2o);
        SpecialDAO specialDAO = new Sql2oSpecialDAO(sql2o);

//        for (int i = 0; i < 6; i++) {
//            Vehicle newVeh = new Vehicle("type" + i, 200 + i, "manufacturer" + i , "model"+i);
//            try{
//                vehicleDAO.add(newVeh);
//                System.out.println(newVeh.getModel() + " added to database");
//            }catch(Exception e){
//                System.out.println("Error from adding vehicle");
//            }
//
//        }
//
//        List<Vehicle> veh = vehicleDAO.findAll();
//        for(int i=0; i < veh.size(); i++){
//            System.out.println(veh.get(i).getModel());
//        }

        //In case we use json objects..
        Gson gson = new Gson();

        /*
            Home Page Route
         */
        get("/", (request, response) -> {
            userDAO.findAll().forEach(System.out::println);
            return new ModelAndView(null, "index.hbs");
        }, new HandlebarsTemplateEngine());

        /*
            Registration Route
         */
        get("/registration", (request, response) -> {
            return new ModelAndView(null, "registration.hbs");
        }, new HandlebarsTemplateEngine());

        post("/registration", (request, response) -> {
            System.out.println("Get a post from registration");
            String email = request.queryParams("email");
            User newUser = new User(email, request.queryParams("password"));
            System.out.println("Values received: " + email + ", " +  request.queryParams("password"));
            userDAO.add(newUser);
            request.session().attribute("email", email);
            response.redirect("/");
            return null;
        });

        /*
        Login
         */
        post("/sign-in", (request, response) -> {

            Map<String, String> model = new HashMap<>();
            String email = request.queryParams("email");
            String password = request.queryParams("password");
            response.redirect("/");
            return null;
        });
        /*
        View Vehicles
         */
        get("/viewvehicles", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("vehicles", vehicleDAO.findAll());
            return new ModelAndView(model, "viewvehicles.hbs");
        }, new HandlebarsTemplateEngine());

        post("/displayvehicles", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            Vehicle selectedVehicle = new Vehicle("car", 1990, request.queryParams("mydropdown2"), request.queryParams("mydropdown1"));
            String carModel = request.queryParams("mydropdown1");
            String manufacturer = request.queryParams("mydropdown2");
            System.out.println(carModel + "," + manufacturer);
            model.put("vehicle", selectedVehicle);
            return new ModelAndView(model, "displayvehicles.hbs");
        }, new HandlebarsTemplateEngine());
        /*
        View Specials
         */
        get("/specials", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("specials", specialDAO.findAll());
            return new ModelAndView(model, "specials.hbs");
        }, new HandlebarsTemplateEngine());
    }
}
