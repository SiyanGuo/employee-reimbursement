package com.revature.service;

import com.revature.dao.ReimbursementDao;
import com.revature.dao.UserDao;
import com.revature.dto.ReimbursementDTO;
import com.revature.exception.ReimbursementNotFoundException;
import com.revature.model.Reimbursement;
import com.revature.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReimbursementServiceTest {

    private ReimbursementDao reimbursementDao;
    private ReimbursementService reimbursementService;
    @BeforeEach
    public void setup (){
        reimbursementDao = mock(ReimbursementDao.class);
        reimbursementService= new ReimbursementService(reimbursementDao);
    }

    @Test
    public void test_GetAllReimbursements_positive() throws SQLException {
        List<Reimbursement> fakeReimbursements = new ArrayList<>();
        User author = new User(1,"Jessica","Wang");
        User resolver = new User(2,"Leo","An");
        fakeReimbursements.add(new Reimbursement(1,new BigDecimal(40.00),"Lunch","FOOD","2022-03-07","APPROVED","2022-03-18","https://storage.googleapis.com/employee_reimbursement/receipt5.png",author,resolver));
        fakeReimbursements.add(new Reimbursement(2,new BigDecimal(60.00),"Lunch","FOOD","2022-03-07","DENIED","2022-03-18","https://storage.googleapis.com/employee_reimbursement/receipt5.png",author,resolver));

        when(reimbursementDao.getAllReimbursements()).thenReturn(fakeReimbursements);
        List<Reimbursement> actual = reimbursementService.getAllReimbursements();
        List<Reimbursement> expected = new ArrayList<>(fakeReimbursements);
        Assertions.assertEquals(expected, actual);
    }

//    negative
    @Test
    public void test_getSpecificEmployee_invalidId() throws ReimbursementNotFoundException, SQLException {

        try {
        reimbursementService.getSpecificEmployeeReimbursements("abc");
        fail();

        }catch(IllegalArgumentException e) {
            String expectedMessage = "Employee id must be a valid value";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);
        }
    }

//    negative
    @Test
    public void test_getSpecificEmployee_noReimbursementsFound() throws SQLException {

        when(reimbursementDao.getSpecificEmployeeReimbursements(eq(4))).thenReturn(null);
        try {
            reimbursementService.getSpecificEmployeeReimbursements("4");
            fail();

        }catch(ReimbursementNotFoundException e) {
            String expectedMessage = "No reimbursements found for employee with id 4";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void test_getSpecificEmployee_positive () throws SQLException, ReimbursementNotFoundException {
        List<Reimbursement> fakeReimbursements = new ArrayList<>();
        User author = new User(1,"Jessica","Wang");
        User resolver = new User(2,"Leo","An");
        fakeReimbursements.add(new Reimbursement(1,new BigDecimal(40.00),"Lunch","FOOD","2022-03-07","APPROVED","2022-03-18","https://storage.googleapis.com/employee_reimbursement/receipt5.png",author,resolver));
        fakeReimbursements.add(new Reimbursement(2,new BigDecimal(60.00),"Lunch","FOOD","2022-03-07","DENIED","2022-03-18","https://storage.googleapis.com/employee_reimbursement/receipt5.png",author,resolver));

        when(reimbursementDao.getSpecificEmployeeReimbursements(eq(3))).thenReturn(fakeReimbursements);
        List<Reimbursement> actual = reimbursementService.getSpecificEmployeeReimbursements("3");
        List<Reimbursement> expected = new ArrayList<>(fakeReimbursements);
        Assertions.assertEquals(expected, actual);
    }

//    negative
    @Test
    public void test_addReimbursement_invalidType() throws SQLException {

        ReimbursementDTO fakeReimbursement = new ReimbursementDTO();
        fakeReimbursement.setAmount(new BigDecimal(90.00));
        fakeReimbursement.setType("Drink");
        fakeReimbursement.setDescription("two bottles of wine");
        fakeReimbursement.setReceipt("https://storage.googleapis.com/employee_reimbursement/receipt5.png");

        try {
            reimbursementService.addReimbursement(fakeReimbursement, "6");
            fail();

        }catch(IllegalArgumentException e) {
            String expectedMessage = "Valid reimbursement type are LODGING, TRAVEL, FOOD or OTHER. Invalid input: DRINK";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void test_addReimbursement_positive() throws SQLException {
        User author = new User(1,"Jessica","Wang");

        Reimbursement returnedReimbursement = new Reimbursement(1,new BigDecimal(90.00),"two bottles of wine","FOOD","2022-03-07","PENDING",null,"https://storage.googleapis.com/employee_reimbursement/receipt5.png",author,null);

        ReimbursementDTO fakeReimbursement = new ReimbursementDTO();
        fakeReimbursement.setAmount(new BigDecimal(90.00));
        fakeReimbursement.setType("FOOD");
        fakeReimbursement.setDescription("two bottles of wine");

        when(reimbursementDao.addReimbursement(fakeReimbursement,9)).thenReturn(returnedReimbursement);

        Reimbursement actual = reimbursementService.addReimbursement(fakeReimbursement,"9");
        Reimbursement expected = returnedReimbursement;
        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void test_resolveReimbursement_invalidStatus() throws ReimbursementNotFoundException, SQLException {
        try {
            reimbursementService.resolveReimbursement("okay", 3,"10");
            fail();

        }catch(IllegalArgumentException e) {
            String expectedMessage = "Choose to approve or deny the reimbursement. Invalid input: OKAY";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void test_resolvedReimbursement_notFound() throws ReimbursementNotFoundException, SQLException {

        when(reimbursementDao.checkReimbursement(anyInt())).thenReturn(-1);

        try {
            reimbursementService.resolveReimbursement("DENIED", 3,"10");
            fail();

        }catch(ReimbursementNotFoundException e) {
            String expectedMessage = "Reimbursement with id 10 was not found";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void test_resolvedReimbursement_notPending() throws SQLException, ReimbursementNotFoundException {
        when(reimbursementDao.checkReimbursement(anyInt())).thenReturn(2);

        try {
            reimbursementService.resolveReimbursement("DENIED", 3,"10");
            fail();

        }catch(IllegalArgumentException e) {
            String expectedMessage = "Reimbursement has been resolved. Changes are not allowed";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void test_resolvedReimbursement_invalidId() throws ReimbursementNotFoundException, SQLException {
        try {
            reimbursementService.resolveReimbursement("denied",3, "abc");
            fail();

        }catch(IllegalArgumentException e) {
            String expectedMessage = "Reimbursement id must be a valid value";
            String actualMessage = e.getMessage();
            Assertions.assertEquals(expectedMessage, actualMessage);
        }
    }

//    @Test
//    public void test_resolvedReimbursement_positive() throws SQLException, ReimbursementNotFoundException {
//
//        User author = new User(1,"Jessica","Wang");
//        User resolver = new User(3,"Leo","An");
//       // Reimbursement fakeReimbursement = new Reimbursement(1,new BigDecimal(40.00),"Lunch","FOOD","2022-03-07","APPROVED","2022-03-18","https://storage.googleapis.com/employee_reimbursement/receipt5.png",author,resolver);
//
//        when(reimbursementDao.checkReimbursement(1)).thenReturn(1);
//        when(reimbursementDao.resolveReimbursement("Approved",3,1)).thenReturn
//                (new Reimbursement(1,new BigDecimal(40.00),"Lunch","FOOD","2022-03-07","APPROVED","2022-03-18","https://storage.googleapis.com/employee_reimbursement/receipt5.png",author,resolver));
//
//        Reimbursement actual = reimbursementService.resolveReimbursement("Approved",3, "1");
//        Reimbursement expected = new Reimbursement(1,new BigDecimal(40.00),"Lunch","FOOD","2022-03-07","APPROVED","2022-03-18","https://storage.googleapis.com/employee_reimbursement/receipt5.png",author,resolver);
//        Assertions.assertEquals(expected, actual);
//    }

}
