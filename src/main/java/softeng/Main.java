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
import softeng.model.Vehicle;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class Main {
    public static void main(String[] args) {

        staticFileLocation("/public");

        //TODO: Figure out mysql driver stuff..
        String datasource = "jdbc:";
        Sql2o sql2o = new Sql2o(
                String.format("%s;", datasource), "", "");

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

        get("/vehicles", (request, response) -> vehicleDAO.findAll()/*, gson::toJson*/);
    }
}
