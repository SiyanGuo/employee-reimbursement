package com.revature.service;

import com.google.cloud.storage.*;
import com.revature.dao.ReimbursementDao;
import com.revature.exception.EmployeetNotFoundException;
import com.revature.exception.InvalidImageException;
import com.revature.exception.ReimbursementNotFoundException;
import com.revature.exception.UploadFailedException;
import com.revature.model.Reimbursement;
import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

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

    public String uploadToCloudStorage(InputStream fileInputStream) throws InvalidImageException, IOException, UploadFailedException {
        Tika tika = new Tika();
        String mimeType = tika.detect(fileInputStream);
        if (!mimeType.equals("image/jpeg") && !mimeType.equals("image/png") && !mimeType.equals("image/gif")) {
            throw new InvalidImageException("Image must be a JPEG, PNG, or GIF");
        }
        String fileName = UUID.randomUUID().toString();
        String projectId = "global-song-344220";
        String bucketName = "employee_reimbursement";
        Storage storage =
                StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(mimeType).build();
        Blob blob = storage.create(blobInfo, fileInputStream);
       if(blob.getMediaLink()==null){
           throw new UploadFailedException("Upload failed");
       }
        String publicUrl = "https://storage.googleapis.com/employee_reimbursement/"+fileName;
        return publicUrl;
    }
}
