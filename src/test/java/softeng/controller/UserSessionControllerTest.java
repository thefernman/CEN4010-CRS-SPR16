package softeng.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import softeng.model.User;

import static org.junit.Assert.*;

/**
 * Created by Fernando on 4/25/2016.
 */
public class UserSessionControllerTest {

    private Connection conn;
    private UserSessionController userSessionController;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init_test.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        userSessionController = new UserSessionController(sql2o);
        //Keep connection open through entire test so that it isn't wiped out
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void registerUser() throws Exception {
        String email = "fcamp001@fiu.edu";
        String password = "fernando";
        userSessionController.createUser(email, password);
        User returned = userSessionController.findByEmail(email);

        assertEquals(email, returned.getEmail());

    }

    @Test
    public void updateUserProfile() throws Exception {

    }

    @Test
    public void updateUserInDB() throws Exception {

    }

}