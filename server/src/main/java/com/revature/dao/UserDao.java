package com.revature.dao;

import com.revature.model.User;
import com.revature.utility.ConnectionUtility;

import java.sql.*;

public class UserDao {
    public UserDao(){
    }

    public User getUserByUsername(String username) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "select * from user_with_user_role " +
                    "where user_with_user_role.username = ? ";

            try( PreparedStatement pstmt = con.prepareStatement(sql)){
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

    public User signUp(User user) throws SQLException {
        try(Connection con = ConnectionUtility.getConnection()){
            String sql =  "insert into users (username, password, first_name, last_name, email, user_role_id) "+
        "values (?, ?, ?, ?, ?, (select ur.id from user_role ur where ur.role = ?))";
            try(PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                pstmt.setString(1,user.getUsername());
                pstmt.setString(2,user.getPassword());
                pstmt.setString(3,user.getFirstName());
                pstmt.setString(4,user.getLastName());
                pstmt.setString(5,user.getEmail());
                pstmt.setString(6,user.getUserRole());

                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                rs.next();
                int userId = rs.getInt(1);

                return new User(userId,user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getUserRole());
            }
        }
    }


}
