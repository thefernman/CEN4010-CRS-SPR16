package softeng.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import softeng.dao.specials.Sql2oSpecialDAO;
import softeng.model.Special;

import static org.junit.Assert.assertEquals;

/**
 * Created by Fernando on 4/23/2016.
 */
public class Sql2oSpecialDAOTest {

    private Sql2oSpecialDAO specialDAO;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init_test.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        specialDAO = new Sql2oSpecialDAO(sql2o);
        //Keep connection open through entire test so that it isn't wiped out
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addedSpecialAreReturnedFromFindAll() throws Exception {
        Special special = newTestSpecial();
        specialDAO.add(special);
        assertEquals(1, specialDAO.findAll().size());
    }

    @Test
    public void noSpecialAreReturnFromFindAll() throws Exception {
        assertEquals(0, specialDAO.findAll().size());
    }

    private Special newTestSpecial() {
        return new Special("Holiday Special", 10);
    }
}
