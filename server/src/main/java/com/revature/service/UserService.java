package com.revature.service;

import com.revature.dao.UserDao;
import com.revature.dto.UserResponseDTO;
import com.revature.model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.security.auth.login.FailedLoginException;
import java.sql.SQLException;

public class UserService {
    private UserDao userDao;

    public UserService() { this.userDao = new UserDao(); }

    public User login(String username, String password) throws SQLException, FailedLoginException {

        // hash password
        //String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = this.userDao.getUserByUsername(username);
        if (user == null ) {
            throw new FailedLoginException("Username is not recognized");
        } else if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new FailedLoginException("Password is not valid");
        }
        return user;
    }

}
