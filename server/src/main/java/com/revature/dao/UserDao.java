package com.revature.dao;

import com.revature.model.User;
import com.revature.utility.ConnectionUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public UserDao(){
    }

    public User getUserByUsername(String username) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "select * from user_with_user_role " +
                    "where user_with_user_role.username = ? ";

            try( PreparedStatement pstmt = con.prepareStatement(sql);){
                pstmt.setString(1, username);;

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt("user_id");
                    String un = rs.getString("username");
                    String pw = rs.getString("password");
                    String fName = rs.getString("first_name");
                    String lName = rs.getString("last_name");
                    String email = rs.getString("email");
                    String role = rs.getString("user_role");

                    return new User(id, un, pw, fName, lName, email, role);
                }

                return null;
            }
        }
    }
}
