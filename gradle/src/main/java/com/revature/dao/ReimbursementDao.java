package com.revature.dao;

import com.revature.model.Reimbursement;
import com.revature.model.User;
import com.revature.utility.ConnectionUtility;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementDao {

    public List<Reimbursement> getAllReimbursements() throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            List<Reimbursement> reimbursements = new ArrayList<>();
            String sql = "select r.*, u.first_name as author_first, u.last_name as author_last, u2.first_name as resolver_first, u2.last_name as resolver_last " +
                    "from reimbursement_full r " +
                    "left join users u " +
                    "on r.author_id = u.id " +
                    "left join users u2 " +
                    "on r.resolver_id = u2.id " +
                    "order by r.submitted_at desc";
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                //author
                int authorId = rs.getInt("author_id");
                String authorFName = rs.getString("author_first");
                String authorLName = rs.getString("author_last");

                User author = new User(authorId, authorFName, authorLName);

                //resolver
                int resolverId = rs.getInt("resolver_id");
                String resolverFName = rs.getString("resolver_first");
                String resolverLName = rs.getString("resolver_last");

                User resolver = new User(resolverId, resolverFName, resolverLName);

                //reimbursement
                int id = rs.getInt("id");
                BigDecimal amount = rs.getBigDecimal("amount");
                String description = rs.getString("description");
                String type = rs.getString("type");
                String submittedAt = rs.getString("submitted_at");
                String status = rs.getString("status");
                String resolvedAt = rs.getString("resolved_at");
                String receipt = rs.getString("receipt");

                Reimbursement r = new Reimbursement(id, amount, description, type, submittedAt, status, resolvedAt, receipt, author, resolver);

                reimbursements.add(r);
            }
            return reimbursements;
        }
    }

    public Reimbursement resolveReimbursement(String status, int managerId, int reimbursementId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            con.setAutoCommit(false);
            String sql = "update reimbursement " +
                    "set status_id = (select rs.id " +
                    "from reimb_status rs " +
                    "where rs.status = ? ), " +
                    "resolved_at = current_timestamp, " +
                    "resolver_id= ? " +
                    "where id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, managerId);
            pstmt.setInt(3, reimbursementId);

            pstmt.executeUpdate();

            String sql2 = "select r.*, u.first_name as author_first, u.last_name as author_last, u2.first_name as resolver_first, u2.last_name as resolver_last " +
                    "from reimbursement_full r " +
                    "left join users u " +
                    "on r.author_id = u.id " +
                    "left join users u2 " +
                    "on r.resolver_id = u2.id " +
                    "where r.id = ? " +
                    "order by r.submitted_at desc";

            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setInt(1, reimbursementId);
            ResultSet rs = pstmt2.executeQuery();

            rs.next();
            //author
            int authorId = rs.getInt("author_id");
            String authorFName = rs.getString("author_first");
            String authorLName = rs.getString("author_last");

            User author = new User(authorId, authorFName, authorLName);

            //resolver
            int resolverId = rs.getInt("resolver_id");
            String resolverFName = rs.getString("resolver_first");
            String resolverLName = rs.getString("resolver_last");

            User resolver = new User(resolverId, resolverFName, resolverLName);

            //reimbursement
            int id = rs.getInt("id");
            BigDecimal amount = rs.getBigDecimal("amount");
            String description = rs.getString("description");
            String type = rs.getString("type");
            String submittedAt = rs.getString("submitted_at");
            String status2 = rs.getString("status");
            String resolvedAt = rs.getString("resolved_at");
            String receipt = rs.getString("receipt");

            Reimbursement r = new Reimbursement(id, amount, description, type, submittedAt, status2, resolvedAt, receipt, author, resolver);


            con.commit();
            return r;
        }
    }

    public int checkReimbursement(int reimbursementId) throws SQLException {
        try(Connection con = ConnectionUtility.getConnection()){
            String sql = "select status_id " +
                    "from reimbursement " +
                    "where id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, reimbursementId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int statusId = rs.getInt("status_id");
                return statusId;
            }

        }
        return -1;
    }

}


