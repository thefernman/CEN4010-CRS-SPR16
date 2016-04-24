package softeng;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Mod;
//import com.sun.org.apache.xpath.internal.operations.String;
//import com.sun.org.apache.xpath.internal.operations.String;
import org.sql2o.Sql2o;
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

        UserDAO userDAO = new Sql2oUserDAO(sql2o);
        VehicleDAO vehicleDAO = new Sql2oVehicleDAO(sql2o);
        ReservationDAO reservationDAO = new Sql2oReservationDAO(sql2o);
        SpecialDAO specialDAO = new Sql2oSpecialDAO(sql2o);

        //In case we use json objects..
        Gson gson = new Gson();

        /*
            Home Page Route
         */
        get("/", (request, response) -> {

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
            String password = request.queryParams("password");

            User newUser = new User(email, password);

            System.out.println("Values received: " + newUser.getEmail() + ", " +  newUser.getPassword());

            userDAO.add(newUser);
            Session session = request.session();
            session.attribute("email", email);

            request.session().attribute("email", session.attribute("email"));
            System.out.println("Session user: " + request.session().attribute("email"));

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
            //fetch all user attributes from db and use them to populate the session object.user attributes
            model.put("email", email);

            //if(userDAO.verifyUserLogin(email,password))
            //{
                User user = userDAO.findByEmail(email);
                System.out.println("Login attempted by user:\n");
                System.out.println(user);
                request.session(true);
                request.session().attribute("user", user);
                //request.session().attribute("usertype", user.getType());
            //}
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
            String type = request.queryParams("selection");
            List<Vehicle> typeSelection = vehicleDAO.findAllByType(type);
            model.put("vehicle", typeSelection);
            return new ModelAndView(model, "displayvehicles.hbs");
        }, new HandlebarsTemplateEngine());

        post("/checkout", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(request.queryParams("selection"));
            Vehicle carSelection = vehicleDAO.findById(id);
            model.put("vehicle", carSelection);
            return new ModelAndView(model, "checkout.hbs");
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