package com.revature.service;

import com.revature.dao.ReimbursementDao;
import com.revature.dto.ReimbursementDTO;
import com.revature.exception.ReimbursementNotFoundException;

import com.revature.model.Reimbursement;
import java.sql.SQLException;
import java.util.List;


public class ReimbursementService {

   private ReimbursementDao reimbursementDao;

    public ReimbursementService(){
        this.reimbursementDao = new ReimbursementDao();
    }

    public ReimbursementService(ReimbursementDao mockedObject) { this.reimbursementDao = mockedObject;}

    public List<Reimbursement> getAllReimbursements() throws SQLException, ClassNotFoundException {
        List<Reimbursement> reimbursements;
        reimbursements = this.reimbursementDao.getAllReimbursements();
        return reimbursements;
    }

    public Reimbursement resolveReimbursement(String status, int managerId, String reimbursementId) throws SQLException, ReimbursementNotFoundException, ClassNotFoundException {
       try {

           int id = Integer.parseInt(reimbursementId);
           status = status.toUpperCase();

           //validate request param
           if (!status.equals("APPROVED") && !status.equals("DENIED")) {
               throw new IllegalArgumentException("Choose to approve or deny the reimbursement. Invalid input: " + status );
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
           throw new IllegalArgumentException("Reimbursement id must be a valid value");
       }

    }

    public List<Reimbursement> getSpecificEmployeeReimbursements (String userId) throws SQLException, ReimbursementNotFoundException, ClassNotFoundException {
        try {
            int employeeId = Integer.parseInt(userId);
            List<Reimbursement> reimbursements;
            reimbursements = this.reimbursementDao.getSpecificEmployeeReimbursements(employeeId);

            if (reimbursements == null) {
                throw new ReimbursementNotFoundException("No reimbursements found for employee with id " + employeeId );
            }
            return reimbursements;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Employee id must be a valid value");
        }

    }

    public Reimbursement addReimbursement(ReimbursementDTO reimbursementDTO, String userId) throws SQLException, ClassNotFoundException {
        int employeeId = Integer.parseInt(userId);

        String type = reimbursementDTO.getType();
        type = type.toUpperCase();
        if (!type.equals("LODGING") && !type.equals("TRAVEL") && !type.equals("FOOD") && !type.equals("OTHER")  ) {
            throw new IllegalArgumentException("Valid reimbursement type are LODGING, TRAVEL, FOOD or OTHER. Invalid input: " + type );
        }
        reimbursementDTO.setType(type);
        Reimbursement reimbursement = this.reimbursementDao.addReimbursement(reimbursementDTO, employeeId);

       return reimbursement;
    }
}
