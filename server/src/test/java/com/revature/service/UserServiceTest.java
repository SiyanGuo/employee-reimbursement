package com.revature.service;

import com.revature.dao.UserDao;
import com.revature.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.FailedLoginException;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

//    private UserService userService;
//    private UserDao userDao;
//    @BeforeEach
//    public void setup(){
//        userDao = mock(UserDao.class);
//        userService = new  UserService(userDao);
//    }

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    public void test_getUserByUsernameNegative() throws SQLException {

        when(userDao.getUserByUsername(eq("employee"))).thenReturn(null);
        Assertions.assertThrows(FailedLoginException.class, ()->{
        userService.login("employee","password");
        });
    }

    @Test
    public void test_checkPasswordPositive() throws SQLException, FailedLoginException {
        User fakeUser = new User (10,"employee","$2a$10$p9Kx8jxJoOGjNzTGZ5H6x.mddC9ycjxZovZiFnunZcIUAhMWXEPsi","Nana","Lu", "nl@gmail.com","EMPLOYEE");
        when(userDao.getUserByUsername(eq("employee"))).thenReturn(fakeUser);
        User actual =  userService.login("employee","pass123");
        User expected = fakeUser;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_checkPasswordNegative() throws SQLException {
        User fakeUser = new User (10,"employee","$2a$10$p9Kx8jxJoOGjNzTGZ5H6x.mddC9ycjxZovZiFnunZcIUAhMWXEPsi","Nana","Lu", "nl@gmail.com","EMPLOYEE");
        when(userDao.getUserByUsername(eq("employee"))).thenReturn(fakeUser);

        Assertions.assertThrows(FailedLoginException.class, () -> {
            userService.login("employee","password");
        });
    }
}
