package com.revature.service;

import com.revature.dao.UserDao;
import com.revature.model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.security.auth.login.FailedLoginException;
import java.sql.SQLException;

public class UserService {
    private UserDao userDao;

    public UserService() { this.userDao = new UserDao(); }

    public User login(String username, String password) throws SQLException, FailedLoginException, ClassNotFoundException {

        User user = this.userDao.getUserByUsername(username);
        if (user == null ) {
            throw new FailedLoginException("Username is not recognized");
        } else if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new FailedLoginException("Password is not valid");
        }
        return user;
    }

    public User signUp(User user) throws SQLException, ClassNotFoundException {

        User userExists = this.userDao.getUserByUsername(user.getUsername());

        if(userExists!=null){
            throw new IllegalArgumentException("The username already exists");
        }
        String role = user.getUserRole().toUpperCase();
        user.setUserRole(role);
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        return this.userDao.signUp(user);
    }
}
