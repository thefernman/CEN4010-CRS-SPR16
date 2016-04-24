package softeng;

import com.google.gson.Gson;
import com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.sql2o.Sql2o;
import softeng.controller.UserSessionController;
import softeng.dao.reservations.ReservationDAO;
import softeng.dao.reservations.Sql2oReservationDAO;
import softeng.dao.specials.SpecialDAO;
import softeng.dao.specials.Sql2oSpecialDAO;
import softeng.dao.users.Sql2oUserDAO;
import softeng.dao.users.UserDAO;
import softeng.dao.vehicles.Sql2oVehicleDAO;
import softeng.dao.vehicles.VehicleDAO;
import softeng.model.Special;
import softeng.model.User;
import softeng.model.Vehicle;
import spark.ModelAndView;
import spark.Session;
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

        //UserDAO userDAO = new Sql2oUserDAO(sql2o);
        VehicleDAO vehicleDAO = new Sql2oVehicleDAO(sql2o);
        ReservationDAO reservationDAO = new Sql2oReservationDAO(sql2o);
        SpecialDAO specialDAO = new Sql2oSpecialDAO(sql2o);

        UserSessionController sessionController = new UserSessionController();

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
            return new ModelAndView(sessionController.getSessionModel(request), "index.hbs"); //returned model map may have zero entries
        }, new HandlebarsTemplateEngine());

        /*
            Registration Route
         */
        get("/registration", (request, response) -> {
            return new ModelAndView(sessionController.getSessionModel(request), "registration.hbs"); //returned model map may have zero entries
        }, new HandlebarsTemplateEngine());

        post("/registration", (request, response) -> {
            System.out.println("page: /registration\nurl: "+request.url()+"\nsession_is_new: "+request.session().isNew());

            User user = sessionController.createUser(request.queryParams("email"), request.queryParams("password"),"member");
            sessionController.setSessionAttributes(request,user);
            Map<String, Object> model = sessionController.getSessionModel(request);

            model.put("registration_is_new",true); //if new registration, display welcome on /registration

            return new ModelAndView(model, "registration.hbs");
        }, new HandlebarsTemplateEngine());

        /*
        Login
         */
        //TODO: when a user logs in, return them to the page they were originally on with the state preserved instead of index.hbs
        post("/sign-in", (request, response) -> {
            System.out.println("page: /sign-in\nSession: "+request.session());

            String email = request.queryParams("email");
            String password = request.queryParams("password");

            if(sessionController.loginCredentialsAreValid(email,password))
            {
                User user = sessionController.findByEmail(email);
                sessionController.setSessionAttributes(request,user);
                //model = sessionController.getSessionModel(request);

                System.out.print("Login attempted by user: ");
                System.out.println(user);

                System.out.println("with attributes: \n");
                for (String attr : request.session().attributes()){
                    System.out.println(attr+"="+request.session().attribute(attr));
                }
                System.out.println("and db users: \n");
                for (Object obj : sessionController.findAll()){
                    System.out.println(obj);
                }
                System.out.println("valid: yes");
            }
//            System.out.println("valid: no");
            return new ModelAndView(sessionController.getSessionModel(request), "index.hbs");
        }, new HandlebarsTemplateEngine());

        get("/sign-out", (request, response) -> {
            for (String attr : request.session().attributes()){
                System.out.println("removing attr: " + request.session().attribute(attr));
                request.session().removeAttribute(attr);
            }
            return new ModelAndView(sessionController.getSessionModel(request), "index.hbs");
        }, new HandlebarsTemplateEngine());
        /*
        View Vehicles
         */
        get("/viewvehicles", (request, response) -> {
            Map<String, Object> model = sessionController.getSessionModel(request);
            model.put("vehicles", vehicleDAO.findAll());
            return new ModelAndView(model, "viewvehicles.hbs");
        }, new HandlebarsTemplateEngine());

        post("/displayvehicles", (request, response) -> {
            Map<String, Object> model = sessionController.getSessionModel(request);
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
            Map<String, Object> model = sessionController.getSessionModel(request);
            model.put("specials", specialDAO.findAll());
            return new ModelAndView(model, "specials.hbs");
        }, new HandlebarsTemplateEngine());
    }
}
