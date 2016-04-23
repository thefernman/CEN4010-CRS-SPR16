package softeng;

import com.google.gson.Gson;
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
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

public class Main {
    public static void main(String[] args) {

        //Hello thefernman

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
            String email = request.queryParams("email");
            User newUser = new User(
                    email, request.queryParams("password"));
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

            model.put("email", email);
            response.redirect("/");
            return null;
        });
        /*
        View Vehicles
         */
        get("/vehicles", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("vehicles", vehicleDAO.findAll());
            return new ModelAndView(model, "vehicles.hbs");
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
