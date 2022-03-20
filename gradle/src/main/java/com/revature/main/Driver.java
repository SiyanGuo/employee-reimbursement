package com.revature.main;

import com.revature.dao.UserDao;
import com.revature.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class Driver {
    public static void main(String[] args) {


       String pw = BCrypt.hashpw("word123", BCrypt.gensalt());
//        if(BCrypt.checkpw("pass123", "$2a$10$p9Kx8jxJoOGzTGZ5H6x.mddC9ycjxZovZiFnunZcIUAhMWXEPsi")){
//            System.out.println("it matches");
//        }
        System.out.println(pw);

//        UserDao dao = new UserDao();
//        try {
//            User user = dao.getUserByUsernameAndPassword("user555", "pass123");
//            System.out.println(user);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
