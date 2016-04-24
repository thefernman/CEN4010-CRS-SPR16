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
        String sql = "INSERT INTO users(email, password) VALUES (:email, :password)";
        try (Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql)
                    .bind(user)
                    .executeUpdate()
                    .getKey();
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

    public boolean verifyUserLogin(String email, String password) {

        try (Connection con = sql2o.open()) {
            User userLoggingIn = con.createQuery("SELECT * FROM users WHERE email = :email AND password = :password")
                    .addParameter("email", email)
                    .addParameter("password", password)
                    .executeAndFetchFirst(User.class);

            return userLoggingIn.getEmail().equals(email) &&
                        userLoggingIn.getPassword().equals(password);
        }
    }
}
