package softeng;

import com.google.gson.Gson;
import com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.sql2o.Sql2o;
import softeng.controller.ReservationController;
import softeng.controller.UserSessionController;
import softeng.controller.VehicleController;
import softeng.dao.reservations.ReservationDAO;
import softeng.dao.reservations.Sql2oReservationDAO;
import softeng.dao.specials.SpecialDAO;
import softeng.dao.specials.Sql2oSpecialDAO;
import softeng.dao.users.Sql2oUserDAO;
import softeng.dao.users.UserDAO;
import softeng.dao.vehicles.Sql2oVehicleDAO;
import softeng.dao.vehicles.VehicleDAO;
import softeng.model.Reservation;
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

        //TODO: Figure out mysql driver stuff.. eventually remove these DAOs and replace them with controllers

        //SpecialController specCont = new VehicleController();
        VehicleController vehCont = new VehicleController();
        ReservationController resvCont = new ReservationController();
        UserSessionController sessCont = new UserSessionController();

        List<Reservation> list = resvCont.findAllReservations();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).toString());
        }


        //In case we use json objects..
        Gson gson = new Gson();

        /*
            Home Page Route
         */
        get("/", (request, response) -> {
            return new ModelAndView(sessCont.getSessionModel(request), "index.hbs"); //returned model map may have zero entries
        }, new HandlebarsTemplateEngine());

        /*
            Registration Route
         */
        get("/registration", (request, response) -> {
            return new ModelAndView(sessCont.getSessionModel(request), "registration.hbs"); //returned model map may have zero entries
        }, new HandlebarsTemplateEngine());

        post("/registration", (request, response) -> {
            //System.out.println("page: /registration\nurl: "+request.url()+"\nsession_is_new: "+request.session().isNew());
            Map<String, Object> model = null;
            if(sessCont.registerUser(request,request.queryParams("email"), request.queryParams("password"))){
                model = sessCont.getSessionModel(request);
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
            if(sessCont.loginUser(request,request.queryParams("email"),request.queryParams("password"))) {
                model = sessCont.getSessionModel(request);
            }
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        get("/sign-out", (request, response) -> {
            for (String attr : request.session().attributes()){
                System.out.println("removing attr: " + request.session().attribute(attr));
                request.session().removeAttribute(attr);
            }
            return new ModelAndView(sessCont.getSessionModel(request), "index.hbs");
        }, new HandlebarsTemplateEngine());
        /*
        View Vehicles
         */
        get("/viewvehicles", (request, response) -> {
            Map<String, Object> model = sessCont.getSessionModel(request);
            model.put("vehicles", vehCont.getAllVehicles());
            return new ModelAndView(model, "viewvehicles.hbs");
        }, new HandlebarsTemplateEngine());

        post("/displayvehicles", (request, response) -> {
            Map<String, Object> model = sessCont.getSessionModel(request);
            String type = request.queryParams("selection");
            List<Vehicle> typeSelection = vehCont.getUnreservedVehicleByType(type);
            model.put("vehicle", typeSelection);
            return new ModelAndView(model, "displayvehicles.hbs");
        }, new HandlebarsTemplateEngine());

        post("/checkout", (request, response) -> {
            Map<String, Object> model = sessCont.getSessionModel(request);
            int id = Integer.parseInt(request.queryParams("selection"));
            vehCont.markAsReserved(vehCont.getVehicleById(id));
            model.put("vehicle", vehCont.getVehicleById(id));
            return new ModelAndView(model, "checkout.hbs");
        }, new HandlebarsTemplateEngine());

        post("/confirmation", (request, response) -> {
            Map<String, Object> model = sessCont.getSessionModel(request);
            int id = Integer.parseInt(request.queryParams("confirmation"));
            System.out.println(model.toString());

            if (model.get("user") == null) {
                System.out.println("User is not Logged in");
            }else{
                User curruser = (User)model.get("user");
                System.out.println(curruser.getId());
                model.put("vehicle", vehCont.getVehicleById(id));
                resvCont.reserveVehicle(id, curruser.getId());
                return new ModelAndView(model, "confirmation.hbs");
            }
            return new ModelAndView(null, "index.hbs");
        }, new HandlebarsTemplateEngine());

        /*
        View Specials
         */
//        get("/specials", (request, response) -> {
//            Map<String, Object> model = sessCont.getSessionModel(request);
//            model.put("specials", specCont.findAll());
//            return new ModelAndView(model, "specials.hbs");
//        }, new HandlebarsTemplateEngine());

        get("/editprofile", (request, response) -> {
            return new ModelAndView(sessCont.getSessionModel(request), "editProfile.hbs");
        }, new HandlebarsTemplateEngine());

        post("/editprofile", (request, response) -> {
            sessCont.updateUserProfile(request);
            response.redirect("/editprofile");
            return null;
        });

        get("/editvehicle", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int vehicle_id = Integer.parseInt(request.queryParams("vehicle_id"));

            Vehicle toBeEdited = vehCont.getVehicleById(vehicle_id);
            System.out.println("from get editvehicle: " + toBeEdited);
            model.put("vehicle", toBeEdited);
            return new ModelAndView(model, "editVehicle.hbs");
        }, new HandlebarsTemplateEngine());

        //add dummy vehicles to database
        //vehCont.populateDBWithDummyCars();
    }
}
