package softeng;

import com.google.gson.Gson;
import softeng.controller.ReservationController;
import softeng.controller.UserSessionController;
import softeng.controller.VehicleController;
import softeng.model.Reservation;
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

        //TODO: Figure out mysql driver stuff.. eventually remove these DAOs and replace them with controllers

        //SpecialController specCont = new VehicleController();
        VehicleController vehicleController = new VehicleController();
        ReservationController reservationController = new ReservationController();
        UserSessionController userSessionController = new UserSessionController();

        List<Reservation> list = reservationController.findAllReservations();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).toString());
        }


        //In case we use json objects..
        Gson gson = new Gson();

        /*
            Home Page Route
         */
        get("/", (request, response) -> {
            //returned model map may have zero entries
            return new ModelAndView(userSessionController.getSessionModel(request), "index.hbs");
        }, new HandlebarsTemplateEngine());

        /*
            Registration Route
         */
        get("/registration", (request, response) -> {
            return new ModelAndView(userSessionController.getSessionModel(request), "registration.hbs"); //returned model map may have zero entries
        }, new HandlebarsTemplateEngine());

        post("/registration", (request, response) -> {
            //System.out.println("page: /registration\nurl: "+request.url()+"\nsession_is_new: "+request.session().isNew());
            Map<String, Object> model = null;
            if(userSessionController.registerUser(request,request.queryParams("email"), request.queryParams("password"))){
                model = userSessionController.getSessionModel(request);
                model.put("registration_is_new",true); //if new registration, display welcome on /registration
            }
            return new ModelAndView(model, "registration.hbs");
        }, new HandlebarsTemplateEngine());

        /*
            Sign-in
         */
        //TODO: when a user logs in, return them to the page they were originally on with the state preserved instead of index.hbs
        post("/sign-in", (request, response) -> {
            //System.out.println("page: /sign-in\nSession: "+request.session());
            Map<String, Object> model = null;
            if(userSessionController.loginUser(request,request.queryParams("email"),request.queryParams("password"))) {
                model = userSessionController.getSessionModel(request);
            }
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

//        get("/sign-out", (request, response) -> {
//            for (String attr : request.session().attributes()){
//                System.out.println("removing attr: " + request.session().attribute(attr));
//                request.session().removeAttribute(attr);
//            }
//            return new ModelAndView(userSessionController.getSessionModel(request), "index.hbs");
//        }, new HandlebarsTemplateEngine());

        get("/sign-out", (request, response) -> {
            for (String attr : request.session().attributes()){
                System.out.println("removing attr: " + request.session().attribute(attr));
                request.session().removeAttribute(attr);
            }
            response.redirect("/");
            return null;
        });

        /*
        View Vehicles
         */
        get("/viewvehicles", (request, response) -> {
            Map<String, Object> model = userSessionController.getSessionModel(request);
            model.put("all_vehicles", vehicleController.getAllVehicles());
            return new ModelAndView(model, "viewvehicles.hbs");
        }, new HandlebarsTemplateEngine());

        post("/displayvehicles", (request, response) -> {
            Map<String, Object> model = userSessionController.getSessionModel(request);
            String type = request.queryParams("selection");
            List<Vehicle> typeSelection = vehicleController.getUnreservedVehicleByType(type);
            model.put("selected_vehicles", typeSelection);
            return new ModelAndView(model, "displayvehicles.hbs");
        }, new HandlebarsTemplateEngine());

        post("/viewvehicledetails", (request, response) -> {
            Map<String, Object> model = userSessionController.getSessionModel(request);
            int id = Integer.parseInt(request.queryParams("selection"));
            vehicleController.markAsReserved(vehicleController.getVehicleById(id));
            model.put("vehicle", vehicleController.getVehicleById(id));
            return new ModelAndView(model, "viewvehicledetails.hbs");
        }, new HandlebarsTemplateEngine());

        post("/reservevehicle", (request, response) -> {
            Map<String, Object> model = userSessionController.getSessionModel(request);
            int id = Integer.parseInt(request.queryParams("confirmation"));
//            request.session().attribute("reserving_vehicle_id",id);
            System.out.println(model.toString());
            System.out.println("confirmation id: "+id);
            if (model.get("user") == null) {
                System.out.println("User is not Logged in");
                model.put("error", "Please register, or log in if you already have an account");
                return new ModelAndView(model, "viewvehicledetails.hbs");
            }else{
                User curruser = (User)model.get("user");
                System.out.println(curruser.getId());
                model.put("vehicle", vehicleController.getVehicleById(id));
                request.session().attribute("vehicle", vehicleController.getVehicleById(id));
                return new ModelAndView(model, "reservevehicle.hbs");
            }
        }, new HandlebarsTemplateEngine());

        post("/confirmreservation", (request, response) -> {
            Map<String, Object> model = userSessionController.getSessionModel(request);
            System.out.println("model: "+model.toString());
            Vehicle veh = request.session().attribute("vehicle");
            int id = veh.getId();
            //int id = Integer.parseInt(request.queryParams("confirmation"));
            System.out.println("id: "+id);

            if (model.get("user") == null) {
                System.out.println("User is not Logged in");
                model.put("error", "Please register, or log in if you already have an account");
                return new ModelAndView(model, "reservevehicle.hbs");
            }else{
                User curruser = (User)model.get("user");
                System.out.println(curruser.getId());
                model.put("vehicle", vehicleController.getVehicleById(id));
                reservationController.reserveVehicle(id, curruser.getId());
                return new ModelAndView(model, "confirmreservation.hbs");
            }
        }, new HandlebarsTemplateEngine());

        /*
        View Specials
         */
//        get("/specials", (request, response) -> {
//            Map<String, Object> model = userSessionController.getSessionModel(request);
//            model.put("specials", specCont.findAll());
//            return new ModelAndView(model, "specials.hbs");
//        }, new HandlebarsTemplateEngine());

        get("/editprofile", (request, response) -> {
            return new ModelAndView(userSessionController.getSessionModel(request), "editProfile.hbs");
        }, new HandlebarsTemplateEngine());

        post("/editprofile", (request, response) -> {
            userSessionController.updateUserProfile(request);
            response.redirect("/editprofile");
            return null;
        });

        get("/editvehicle", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int vehicle_id = Integer.parseInt(request.queryParams("vehicle_id"));

            Vehicle toBeEdited = vehicleController.getVehicleById(vehicle_id);
            System.out.println("from get editvehicle: " + toBeEdited);
            model.put("vehicle", toBeEdited);
            return new ModelAndView(model, "editVehicle.hbs");
        }, new HandlebarsTemplateEngine());

        //add dummy vehicles to database
        //vehicleController.populateDBWithDummyCars();
    }
}
