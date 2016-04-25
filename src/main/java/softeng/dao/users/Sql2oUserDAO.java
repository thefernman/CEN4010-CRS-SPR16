package softeng.dao.users;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import softeng.exc.DAOException;
import softeng.model.User;

import java.util.List;

/**
 * Created by Fernando on 4/13/2016.
 */
public class Sql2oUserDAO implements UserDAO {

    private final Sql2o sql2o;

    public Sql2oUserDAO(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(User user) throws DAOException {
        //TODO: Change sql query
        //TODO: Inform caller of already registered user
        String sql = "INSERT INTO users(email, password, type) VALUES (:email, :password, :type)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(user)
                    .executeUpdate()
                    .getKey();
//            System.out.println("adding user with id: "+id);
            user.setId(id);
        } catch (Sql2oException ex) {
            throw new DAOException(ex, "problem adding user");
        }
//        System.out.println("Users in DB:");
//        List<User> users = this.findAll();
//        for(int i=0; i < users.size(); i++){
//            System.out.println(users.get(i).getEmail());
//        }
    }

    @Override
    public List<User> findAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM users")
                    .executeAndFetch(User.class);
        }
    }

    @Override
    public User findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM users WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(User.class);
        }
    }

    public User findByEmail(String email) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM users WHERE email = :email")
                    .addParameter("email", email)
                    .executeAndFetchFirst(User.class);
        }
    }

    @Override
    public void updateUserInDB(User user) {
        String sql = "UPDATE users " +
                "SET firstName = :firstName, lastName = :lastName, address = :address, city = :city, state = :state " +
                "WHERE email = :email";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .bind(user)
                    .executeUpdate();
        }
    }

    //TODO: Fix this shit
    public boolean verifyUserLogin(String email, String password) throws DAOException {
        System.out.println("verifying login for user with email: "+email+" and password: "+password);
        try (Connection con = sql2o.open()) {
            User userLoggingIn = con.createQuery("SELECT * FROM users WHERE email = :email AND password = :password")
                    .addParameter("email", email)
                    .addParameter("password", password)
                    .executeAndFetchFirst(User.class);
            if (userLoggingIn != null) {
                return userLoggingIn.getEmail().equals(email) &&
                        userLoggingIn.getPassword().equals(password);
            } else {
                return false;
            }
        } catch (Sql2oException ex) {
            throw new DAOException(ex, "problem logging in with username " + email + " and password " + password);
        }
    }
}
