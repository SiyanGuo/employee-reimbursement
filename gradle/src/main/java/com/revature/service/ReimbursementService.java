package com.revature.service;

import com.revature.dao.ReimbursementDao;
import com.revature.model.Reimbursement;

import java.sql.SQLException;
import java.util.List;

public class ReimbursementService {

   private ReimbursementDao reimbursementDao;

    public ReimbursementService(){
        this.reimbursementDao = new ReimbursementDao();
    }

    public List<Reimbursement> getAllReimbursements() throws SQLException {
        List<Reimbursement> reimbursements;
        reimbursements = this.reimbursementDao.getAllReimbursements();
        return reimbursements;
    }

}
