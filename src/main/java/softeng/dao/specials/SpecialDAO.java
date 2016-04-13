package softeng.dao.specials;

import softeng.exc.DAOException;
import softeng.model.Special;

import java.util.List;

/**
 * Created by Fernando on 4/12/2016.
 */
public interface SpecialDAO {
    //TODO: Maybe change this to boolean?
    void add(Special special) throws DAOException;
    List<Special> findAll();
    Special findById(int id);
}
