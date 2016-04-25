package softeng.controller;

import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import softeng.exc.DAOException;
import softeng.model.User;
import spark.ModelAndView;
import spark.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import softeng.dao.users.Sql2oUserDAO;
import softeng.dao.users.UserDAO;

public class UserSessionController {

    private UserDAO userDAO;

    public UserSessionController(Sql2o sql2o) {
        this.userDAO = new Sql2oUserDAO(sql2o);
    }

    public User createUser(String email, String password) throws DAOException {
        User user = new User(email, password);
        try {
            userDAO.add(user);
            return user;
        } catch (DAOException ex) {
            throw ex;
        }
    }

    public void updateUserProfile(Request request) {
        String firstName = request.queryParams("firstName");
        String lastName = request.queryParams("lastName");
        String address = request.queryParams("address");
        String city = request.queryParams("city");
        String state = request.queryParams("state");

        User toBeUpdated = request.session().attribute("user");

        toBeUpdated.setFirstName(firstName);
        toBeUpdated.setLastName(lastName);
        toBeUpdated.setAddress(address);
        toBeUpdated.setCity(city);
        toBeUpdated.setState(state);

        userDAO.updateUserInDB(toBeUpdated);
    }

    public Map<String, Object> getSessionModel(Request request) {
        Map<String, Object> model = new HashMap<>();

        model.put("user", request.session().attribute("user"));

        if (request.session().attribute("registration_is_new") != null) {
            request.session().attribute("registration_is_new", false);
        }
        model.put("registration_is_new", request.session().attribute("registration_is_new"));

        return model;
    }

    public boolean loginUser(Request request, String email, String password) {
        try {
            if (userDAO.verifyUserLogin(email, password)) {
                request.session().attribute("user", findByEmail(email));
                return true;
            }
        } catch (DAOException ex) {
            return false;
        }
        return false;
    }

    public boolean registerUser(Request request, String email, String password) {
        if (email == null || password == null || email.equals("") || password.equals("")) return false;
        try {
            request.session().attribute("user", createUser(email, password));
            return true;
        } catch (DAOException ex) {
            return false;
        }
    }

    public User findByEmail(String email) {
        try {
            return userDAO.findByEmail(email);
        } catch (Sql2oException ex) {
            System.out.println("findByEmail returned null");
            return null;
        }
    }

    public List<User> findAll() {
        try {
            return userDAO.findAll();
        } catch (Sql2oException ex) {
            return null;
        }
    }

    public void updateUserInDB(User toBeUpdated) {
        userDAO.updateUserInDB(toBeUpdated);
    }
}
