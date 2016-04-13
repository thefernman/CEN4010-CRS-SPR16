package softeng.dao.users;

import softeng.exc.DAOException;
import softeng.model.User;

import java.util.List;

/**
 * Created by Fernando on 4/12/2016.
 */
public interface UserDAO {
    //TODO: Maybe change this to boolean?
    void add(User user) throws DAOException;
    List<User> findAll();
    User findById(int id);
}
