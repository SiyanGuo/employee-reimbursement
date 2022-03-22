package com.revature.service;

import com.revature.dao.ReimbursementDao;
import com.revature.exception.EmployeetNotFoundException;
import com.revature.exception.ReimbursementNotFoundException;
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

    public Reimbursement resolveReimbursement(String status, int managerId, String reimbursementId) throws SQLException, ReimbursementNotFoundException {
       try {

           int id = Integer.parseInt(reimbursementId);
           status = status.toUpperCase();

           //validate request param
           if (!status.equals("APPROVED") && !status.equals("DENIED")) {
               throw new IllegalArgumentException("Choose to approve or deny the reimbursement. Invalid input: " + status + " was provided");
           }

           //check if reimbursement exists
           int statusId = this.reimbursementDao.checkReimbursement(id);
           if (statusId == -1) {
               throw new ReimbursementNotFoundException("Reimbursement with id " + id + " was not found");
           }

           //check if it is PENDING
           if (statusId != 1) {
               throw new IllegalArgumentException("Reimbursement has been resolved. Changes are not allowed");
           }

           Reimbursement reimbursement = this.reimbursementDao.resolveReimbursement(status, managerId, id);
           return reimbursement;

       } catch (NumberFormatException e) {
           throw new IllegalArgumentException("Reimbursement id must be an int value");
       }

    }

    public List<Reimbursement> getSpecificEmployeeReimbursements (String userId) throws SQLException, ReimbursementNotFoundException {
        try {
            int employeeId = Integer.parseInt(userId);
            List<Reimbursement> reimbursements;
            reimbursements = this.reimbursementDao.getSpecificEmployeeReimbursements(employeeId);

            if (reimbursements == null) {
                throw new ReimbursementNotFoundException("No reimbursements found for employee with id " + employeeId );
            }
            return reimbursements;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Employee id must be an int value");
        }

    }

}
