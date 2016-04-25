package softeng.controller;

import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import softeng.exc.DAOException;
import softeng.model.User;
import spark.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import softeng.dao.users.Sql2oUserDAO;
import softeng.dao.users.UserDAO;

public class UserSessionController {

    private UserDAO userDAO;
    private Sql2o sql2o;

    public UserSessionController(){
        String datasource = "jdbc:h2:~/CarRental.db";
        sql2o = new Sql2o(
                String.format("%s;INIT=RUNSCRIPT from 'classpath:db/init.sql'", datasource), "", "");

        userDAO = new Sql2oUserDAO(sql2o);
    }
    public User createUser(String email, String password, String type) throws DAOException {
        User user = new User(email, password, type);
        try {
            userDAO.add(user);
        } catch (Sql2oException ex){

        }
        return user;
    }
    public void setSessionAttributes(Request request, User user){
        System.out.println("Session: "+request.session());
        System.out.println("Registering new user with email="+user.getEmail()+" and password="+request.queryParams("password"));

        request.session().attribute("user_email", user.getEmail());
        request.session().attribute("user_type", user.getType());
        request.session().attribute("user_id", user.getId());
        request.session().attribute("user_is_admin", user.isAdmin());
        request.session().attribute("user_is_member", user.isMember());
    }
    public Map<String,Object> getSessionModel(Request request){
        Map<String, Object> model = new HashMap<>();

        model.put("user_email", request.session().attribute("user_email"));
        model.put("user_type", request.session().attribute("user_type"));
        model.put("user_id", request.session().attribute("user_id"));
        model.put("user_is_admin", request.session().attribute("user_is_admin"));
        model.put("user_is_member", request.session().attribute("user_is_member"));

        if(request.session().attribute("registration_is_new") != null){
            request.session().attribute("registration_is_new",false);
        }
        model.put("registration_is_new",request.session().attribute("registration_is_new"));

        return model;
    }
    public boolean loginCredentialsAreValid(String email, String password) throws DAOException {
        boolean validity = false;
        try {
            validity = userDAO.verifyUserLogin(email,password);
        } catch (Sql2oException ex){

        }
        return validity;
    }
    public User findByEmail(String email){
        return userDAO.findByEmail(email);
    }
    public List<User> findAll(){
        return userDAO.findAll();
    }
}
