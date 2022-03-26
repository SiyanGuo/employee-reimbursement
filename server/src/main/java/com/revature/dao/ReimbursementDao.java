package com.revature.dao;

import com.revature.dto.ReimbursementDTO;
import com.revature.model.Reimbursement;
import com.revature.model.User;
import com.revature.utility.ConnectionUtility;

import java.math.BigDecimal;
import java.sql.*;
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
                    "where r.id = ? ";

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

    public List<Reimbursement> getSpecificEmployeeReimbursements(int employeeId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "select r.*, u.first_name as author_first, u.last_name as author_last, u2.first_name as resolver_first, u2.last_name as resolver_last\n" +
                    "from reimbursement_full r " +
                    "left join users u " +
                    "on r.author_id = u.id " +
                    "left join users u2 " +
                    "on r.resolver_id = u2.id " +
                    "where r.author_id = ?" +
                    "order by r.submitted_at desc";
            List<Reimbursement> reimbursements = new ArrayList<>();
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {return null;}

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

    public Reimbursement addReimbursement(ReimbursementDTO reimbursementDTO, int employeeId) throws SQLException {

        try (Connection con = ConnectionUtility.getConnection()) {
            con.setAutoCommit(false);
            String sql = "insert into reimbursement (amount, author_id, description, type_id, submitted_at, receipt) " +
                    "values " +
                    "(?, ?, ?, " +
                    "(select rt.id from reimb_type rt where rt.type = ?), " +
                    "current_timestamp, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setBigDecimal(1, reimbursementDTO.getAmount());
            pstmt.setInt(2, employeeId);
            pstmt.setString(3, reimbursementDTO.getDescription());
            pstmt.setString(4, reimbursementDTO.getType());
            pstmt.setString(5, reimbursementDTO.getReceipt());

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int reimbursementId = rs.getInt(1);

            String sql2 = "select r.*, u.first_name as author_first, u.last_name as author_last, u2.first_name as resolver_first, u2.last_name as resolver_last\n" +
                    "from reimbursement_full r\n" +
                    "left join users u " +
                    "on r.author_id = u.id " +
                    "left join users u2 " +
                    "on r.resolver_id = u2.id " +
                    "where r.id = ? ";

            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setInt(1, reimbursementId);
            ResultSet rs2 = pstmt2.executeQuery();
            rs2.next();
            //author
            int authorId = rs2.getInt("author_id");
            String authorFName = rs2.getString("author_first");
            String authorLName = rs2.getString("author_last");

            User author = new User(authorId, authorFName, authorLName);

            //resolver
            int resolverId = rs2.getInt("resolver_id");
            String resolverFName = rs2.getString("resolver_first");
            String resolverLName = rs2.getString("resolver_last");

            User resolver = new User(resolverId, resolverFName, resolverLName);

            //reimbursement
            int id = rs2.getInt("id");
            BigDecimal amount = rs2.getBigDecimal("amount");
            String description = rs2.getString("description");
            String type = rs2.getString("type");
            String submittedAt = rs2.getString("submitted_at");
            String status2 = rs2.getString("status");
            String resolvedAt = rs2.getString("resolved_at");
            String receipt = rs2.getString("receipt");

            Reimbursement r = new Reimbursement(id, amount, description, type, submittedAt, status2, resolvedAt, receipt, author, resolver);

            con.commit();
            return r;

        }

    }
}


