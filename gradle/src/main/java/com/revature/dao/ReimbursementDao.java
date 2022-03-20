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







}
