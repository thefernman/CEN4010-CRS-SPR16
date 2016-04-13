package softeng.dao.specials;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import softeng.exc.DAOException;
import softeng.model.Special;

import java.util.List;

/**
 * Created by Fernando on 4/13/2016.
 */
public class Sql2oSpecialDAO implements SpecialDAO {

    private final Sql2o sql2o;

    public Sql2oSpecialDAO(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Special special) throws DAOException {
        //TODO: Change sql query..
        String sql = "INSERT INTO specials(name, url) VALUES (:name, :url)";
        try (Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql)
                    .bind(special)
                    .executeUpdate()
                    .getKey();
            special.setId(id);
        } catch (Sql2oException ex) {
            throw new DAOException(ex, "problem adding special");
        }
    }

    @Override
    public List<Special> findAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM specials")
                    .executeAndFetch(Special.class);
        }
    }

    @Override
    public Special findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM specials WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Special.class);
        }
    }
}
