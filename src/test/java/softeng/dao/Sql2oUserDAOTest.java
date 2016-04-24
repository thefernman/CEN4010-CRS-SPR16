package softeng.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import softeng.dao.users.Sql2oUserDAO;
import softeng.model.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Fernando on 4/23/2016.
 */
public class Sql2oUserDAOTest {

    private Sql2oUserDAO userDAO;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init_test.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        userDAO = new Sql2oUserDAO(sql2o);
        //Keep connection open through entire test so that it isn't wiped out
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addUserSetId() throws Exception {
        User user = newTestUser();
        int originalCourseId = user.getId();
        userDAO.add(user);
        assertNotEquals(originalCourseId, user.getId());
    }

    @Test
    public void addedUsersAreReturnedFromFindAll() throws Exception {
        User user = newTestUser();
        userDAO.add(user);
        assertEquals(1, userDAO.findAll().size());
    }

    @Test
    public void noUserReturnEmptyList() throws Exception {
        assertEquals(0, userDAO.findAll().size());
    }

    private User newTestUser() {
        return new User("abc@abc.com", "abc123");
    }
}
