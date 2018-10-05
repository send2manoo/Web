/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.expense.model;

import com.qlc.fieldsense.expense.dao.ExpenseDao;
import com.qlc.fieldsense.team.dao.TeamDao;
import com.qlc.fieldsense.utils.Constant;
import com.qlc.fieldsense.utils.FieldSenseUtils;
import com.qlc.fieldsense.utils.GetApplicationContext;
import com.qlc.fieldsense.utils.PhotoToIconCreator;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Ramesh
 * @date 23-02-2014
 */
public class ExpenseManager {

    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");
    ExpenseDao expenseDao = (ExpenseDao) GetApplicationContext.ac.getBean("expenseDaoImpl");
    TeamDao teamDao = (TeamDao) GetApplicationContext.ac.getBean("teamDaoImpl"); // Added by jyoti
     /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to create expenses
     */
    public Object createExpense(Expense expense, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                //expense.setExpenseTime(fieldSenseUtils.converDateToTimestamp(expense.getStrexpenseTime()));
                int expenseId = expenseDao.insertExpense(expense, accountId);
                if (expenseId != 0) {
                    File oldfile = new File(Constant.IMAGE_UPLOAD_PATH + expense.getExpenseImage().getImageURL());
                    PhotoToIconCreator photoToIconCreator = new PhotoToIconCreator();
                    try {
                        photoToIconCreator.uploadExpenseImage(oldfile, "expense_" + accountId + "_" + userId + "_" + expenseId);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    String imageName = Constant.IMAGE_UPLOAD_PATH + "expense" + "_" + accountId + "_" + userId + "_" + expenseId + ".png";
                    File newfile = new File(imageName);
                    oldfile.renameTo(newfile);
                    expense = expenseDao.selectExpense(expenseId, accountId);                   
                   return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense created successfully .", " Expense ", expense);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense creation failed . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
     /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to create expenses
     */
    public Object createExpenseArray(Expense expense, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                //expense.setExpenseTime(fieldSenseUtils.converDateToTimestamp(expense.getStrexpenseTime()));
                int expenseId = expenseDao.insertExpense(expense, accountId);
                if (expenseId != 0) {
                    for(int i=0;i<expense.getExpenseImageArray().size();i++){
                        String expenseString = expense.getExpenseImageArray().get(i).getImageURL();
                    File oldfile = new File(Constant.IMAGE_UPLOAD_PATH + expenseString);
                    PhotoToIconCreator photoToIconCreator = new PhotoToIconCreator();
                    try {
                        photoToIconCreator.uploadExpenseImage(oldfile, "expense_" + accountId + "_" + userId + "_" + expenseId+"_"+expense.getExpenseImageArray().get(i).getId());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    String imageName = Constant.IMAGE_UPLOAD_PATH + "expense" + "_" + accountId + "_" + userId + "_" + expenseId +"_"+expense.getExpenseImageArray().get(i).getId()+ ".png";
                    File newfile = new File(imageName);
                    oldfile.renameTo(newfile);
                    }
                    expense = expenseDao.selectExpense(expenseId, accountId);                   
                   return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense created successfully .", " Expense ", expense);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense creation failed . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    
      public Object updateExpenseWithBase64(Expense expense, String userToken) {
               if (fieldSenseUtils.isTokenValid(userToken)) {
//                   System.out.println("I am un createExpenseWithBase64hi");
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int userId = fieldSenseUtils.userIdForToken(userToken);
                //expense.setExpenseTime(fieldSenseUtils.converDateToTimestamp(expense.getStrexpenseTime()));
               // int expenseId = expenseDao.insertExpense(expense, accountId);
                if (expenseDao.updateExpense(expense, accountId)) {
//                    System.out.println("Helllloo oo oo o o oo o o o o o oo o o o o  o null please"+expense.getImageBase64()+ "HELLO " + expense.getImageBase64().equals("") +"hellllllll dqwd "+ expense.getImageBase64().equals(" "));
                    if(  expense.getImageBase64().equalsIgnoreCase("") | expense.getImageBase64().equalsIgnoreCase(" ") ) {
//                        System.out.println("No image");
                    }else{
//                          System.out.println("Helllloo oNECTX ONE "+expense.getImageBase64()+ "HELLO ");
                      BufferedImage bufferImage =  decodeToImage(expense.getImageBase64());
                      try {
                        //BufferedImage bi = getMyImage();  // retrieve image
//                           System.out.println("Gort image");
                            File outputfile = new File(Constant.IMAGE_UPLOAD_PATH +"expense" +"_" + accountId + "_" + userId + "_" + expense.getId() +".png");
                            File outputfile1 = new File(Constant.IMAGE_UPLOAD_PATH +"expense" +"_" + accountId + "_" + userId + "_" + expense.getId() +"_640X480"+".png");
                            outputfile.delete();
                            outputfile.delete();
                            outputfile.createNewFile();
                            outputfile1.createNewFile();
//                            System.out.println("File path  "+outputfile.getCanonicalPath() + "file name "+outputfile.getName()+" file 1 "+outputfile.exists() +"File 2"+outputfile1.exists());
                            if(outputfile.exists() && outputfile1.exists()){
                            ImageIO.write(bufferImage, "png", outputfile);
                            ImageIO.write(bufferImage, "png", outputfile1);
                            }
                            } catch (IOException e) {
                                 e.printStackTrace();
                            }
                    }
//                    File oldfile = new File(Constant.IMAGE_UPLOAD_PATH + expense.getExpenseImage().getImageURL());
//                    PhotoToIconCreator photoToIconCreator = new PhotoToIconCreator();
//                    try {
//                        photoToIconCreator.uploadExpenseImage(oldfile, "expense_" + accountId + "_" + userId + "_" + expenseId);
//                    } catch (IOException ex) {
//                        java.util.logging.Logger.getLogger(ExpenseManager.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    String imageName = Constant.IMAGE_UPLOAD_PATH + "expense" + "_" + accountId + "_" + userId + "_" + expenseId + ".png";
//                    File newfile = new File(imageName);
//                    oldfile.renameTo(newfile);
                    expense = expenseDao.selectExpense(expense.getId(), accountId);                   
                   return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense created successfully .", " Expense ", expense);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense creation failed . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param expenseId
     * @param userToken
     * @return details of expense based on expense id 
     */
    public Object selectExpense(int expenseId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.isExpenseValid(expenseId, accountId)) {
                    Expense expense = new Expense();
                    expense = expenseDao.selectExpense(expenseId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Expense ", expense);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid expenseId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    /**
     * 
     * @param expenseId
     * @param userToken
     * @return details of expense based on expense id 
     */
    public Object selectExpenseAudit(int expenseId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.isExpenseValid(expenseId, accountId)) {
                    List<Expense> expense = new ArrayList<Expense>();
                    expense = expenseDao.selectExpenseAudit(expenseId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Expense ", expense);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid expenseId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userId
     * @param startDate
     * @param endDate
     * @param userToken
     * @return details of expenses of particular user in specific period
     */
    public Object selectUserExpense(int userId, String startDate, String endDate, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (fieldSenseUtils.isUserValid(userId)) {
                    List<Expense> userExpenseList = new ArrayList<Expense>();
                   // Timestamp start = fieldSenseUtils.converDateToTimestamp(startDate + " 00:00:00");
                   // Timestamp end = fieldSenseUtils.converDateToTimestamp(endDate + " 00:00:00");
                    userExpenseList = expenseDao.selectUserExpense(userId, startDate, endDate, accountId);
//                    System.out.println("Got the data ");
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Expense ", userExpenseList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

     /**
     * 
     * @param customerId
     * @param userToken
     * @return details of expenses occurred related to particular customer based on customer id
     */
    public Object selectCustomerExpense(int customerId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (fieldSenseUtils.isCustomerValid(customerId, accountId)) {
                    List<Expense> customerExpenseList = new ArrayList<Expense>();
                    customerExpenseList = expenseDao.selectCustomerExpense(customerId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Expense ", customerExpenseList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid customerId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userId
     * @param customerId
     * @param userToken
     * @return  expense details of particular user for particular customer
     */
    public Object selectUserCustomerExpense(int userId, int customerId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (fieldSenseUtils.isUserValid(userId)) {
                    if (fieldSenseUtils.isCustomerValid(customerId, accountId)) {
                        List<Expense> userCustomerExpenseList = new ArrayList<Expense>();
                        userCustomerExpenseList = expenseDao.selectUserCustomerExpense(userId, customerId, accountId);
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Expense ", userCustomerExpenseList);
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid customerId . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

     /**
     * 
     * @param expenseTypeId
     * @param userToken
     * @return all expense details of specific type based on expense type
     */
    public Object selctTypeOfExpense(int expenseTypeId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                List<Expense> typeOfExpenseList = new ArrayList<Expense>();
                typeOfExpenseList = expenseDao.selectSpecificTypeExpense(expenseTypeId, expenseTypeId);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", " Expense ", typeOfExpenseList);
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to update expense details
     */
    public Object updateExpense(Expense expense, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.isExpenseValid(expense.getId(), accountId)) {
                    //expense.setExpenseTime(fieldSenseUtils.converDateToTimestamp(expense.getStrexpenseTime()));
                    if (expenseDao.updateExpense(expense, accountId)) {
                        if (expense.getExpenseImage().getId() != 0) {                            
                            String imageName = Constant.IMAGE_UPLOAD_PATH + "expense" + "_" + accountId + "_" + expense.getUser().getId() + "_" + expense.getId() + ".png";
                            String imageName2 = Constant.IMAGE_UPLOAD_PATH + "expense" + "_" + accountId + "_" + expense.getUser().getId() + "_" + expense.getId() + "_640X480.png";
                            if (expense.getExpenseImage().getImageURL().isEmpty()) {
                                File file = new File(imageName);
                                file.delete();
                                File file2 = new File(imageName2);
                                file2.delete();
                            } else {
                                File oldfile = new File(Constant.IMAGE_UPLOAD_PATH + expense.getExpenseImage().getImageURL());
                                PhotoToIconCreator photoToIconCreator = new PhotoToIconCreator();
                                try {
                                    photoToIconCreator.uploadExpenseImage(oldfile, "expense_" + accountId + "_" + expense.getUser().getId() + "_" + expense.getId());
                                } catch (IOException ex) {
                                    java.util.logging.Logger.getLogger(ExpenseManager.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                File newfile = new File(imageName);
                                oldfile.renameTo(newfile);
                            }
                        }                        
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense updated successfully .", " ", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense updation failed try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid expenseId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to update expense details
     */
    public Object updateExpenseForImage(Expense expense, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
//            System.out.println("Upadte Expense for umage");
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.isExpenseValid(expense.getId(), accountId)) {
                    //expense.setExpenseTime(fieldSenseUtils.converDateToTimestamp(expense.getStrexpenseTime()));
                    if (expenseDao.updateExpense(expense, accountId)) {
                      //  if (expense.getExpenseImage().getId() != 0) {
                              for(int i=0;i<expense.getExpenseImageArray().size();i++){
                            String imageName = Constant.IMAGE_UPLOAD_PATH + "expense" + "_" + accountId + "_" + expense.getUser().getId() + "_" + expense.getId() +"_"+expense.getExpenseImageArray().get(i).getId()+ ".png";
                            String imageName2 = Constant.IMAGE_UPLOAD_PATH + "expense" + "_" + accountId + "_" + expense.getUser().getId() + "_" + expense.getId() +"_"+expense.getExpenseImageArray().get(i).getId()+ "_640X480.png";
//                                  System.out.println("expense.getExpenseImageArray().get(i).getImageURL()"+expense.getExpenseImageArray().get(i).getImageURL());
                            if(expense.getExpenseImageArray().get(i).getImageURL().contains("https://")){
                                System.err.println("Image Presert");
                            }else{
                            if (expense.getExpenseImageArray().get(i).getImageURL().isEmpty()) {
                                File file = new File(imageName);
                                file.delete();
                                File file2 = new File(imageName2);
                                file2.delete();
                            } else {
                                File oldfile = new File(Constant.IMAGE_UPLOAD_PATH +expense.getExpenseImageArray().get(i).getImageURL());
                                PhotoToIconCreator photoToIconCreator = new PhotoToIconCreator();
                                try {
//                                    System.out.println("URL "+"expense_" + accountId + "_" + expense.getUser().getId() + "_" + expense.getId()+"_"+expense.getExpenseImageArray().get(i).getId());
                                    photoToIconCreator.uploadExpenseImage(oldfile, "expense_" + accountId + "_" + expense.getUser().getId() + "_" + expense.getId()+"_"+expense.getExpenseImageArray().get(i).getId());
                                } catch (IOException ex) {
                                    java.util.logging.Logger.getLogger(ExpenseManager.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                File newfile = new File(imageName);
                                oldfile.renameTo(newfile);
                            }
                        }
                              }
                      //  } //                       
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense updated successfully .", " ", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense updation failed try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid expenseId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param expenseId
     * @param id
     * @param userToken
     * @return 
     * @purpose used to delete expense details based on expense id. 
     */
    public Object deleteExpense(int expenseId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.isExpenseValid(expenseId, accountId)) {
                    if (expenseDao.deleteExpense(expenseId, accountId)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense deleted successfully .", " ", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense deletion failed. Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid expenseId . Please try again . ", "", "");
                }
           // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}      
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param expenseImage
     * @param userToken
     * @return 
     * @structure changed and not in use anymore
     */
    public Object createExpenseImage(ExpenseImage expenseImage, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int expenseImageId = expenseDao.insertImage(expenseImage, accountId);
                if (expenseImageId != 0) {
                    expenseImage = expenseDao.selectImage(expenseImageId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense imge created  successfully .", "ExpenseImage ", expenseImage);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense image creation failed . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param expenseId
     * @param userToken
     * @return 
     * @structure changed and not in use anymore
     */
    public Object selectExpenseImage(int expenseId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.isExpenseValid(expenseId, accountId)) {
                    ExpenseImage expenseImage = new ExpenseImage();
                    expenseImage = expenseDao.selectExpenseImage(expenseId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", "ExpenseImage ", expenseImage);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid ExpenseId . Please try again . ", "", "");
                }
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param expenseImage
     * @param userToken
     * @return 
     * @structure changed and not in use anymore
     */
    public Object updateExpenseImage(ExpenseImage expenseImage, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.isImageValid((int)expenseImage.getId(), accountId)) {
                    if (expenseDao.updateImage(expenseImage, accountId)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense image updated successfully .", " ", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense image updation failed try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid expenseImageId . Please try again . ", "", "");
                }
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param id
     * @param userToken
     * @return 
     * @structure changed and not in use anymore
     */
    public Object deleteExpenseImage(int id, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.isImageValid(id, accountId)) {
                    if (expenseDao.deleteImage(id, accountId)) {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Image deleted successfully .", "", "");
                    } else {
                        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " ImageId deletion failed . Please try again . ", "", "");
                    }
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid ImageId . Please try again . ", "", "");
                }
             // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userId
     * @param userToken
     * @return details of user expenses with user id
     */
    public Object selectUserExpense(int userId, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (fieldSenseUtils.isUserValid(userId)) {
                    List<Expense> userExpenseList = new ArrayList<Expense>();
                    userExpenseList = expenseDao.selectUserExpense(userId, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Expense ", userExpenseList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }
           // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to approve user expense 
     */
    public Object approveExpense(Expense expense, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.approveExpense(expense, accountId)) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense approved successfully .", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense approvel failed . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}      
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to approve user expense 
     */
    public Object approveExpenseByAccount(Expense expense, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.approveExpenseByAccount(expense, accountId,fieldSenseUtils)) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense approved successfully .", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense approval failed . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
            /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to approve user expense 
     */
    public Object approveMultiExpenseByAccount(Expense[] expense, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.approveMultiExpenseByAccount(expense, accountId,fieldSenseUtils)) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expenses approved successfully .", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expenses approval failed . Please try again . ", "", "");
                }
           // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @used to reject user expenses 
     */
    public Object rejectExpense(Expense expense, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.rejectExpense(expense, accountId)) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense rejected successfully .", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense rejection failed . Please try again . ", "", "");
                }
           // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}      
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to approve user expense 
     */
    public Object rejectExpenseByAccount(Expense expense, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.rejectExpenseByAccount(expense, accountId,fieldSenseUtils)) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense rejected successfully .", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense rejected failed . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
   /* * 
     * @param expense
     * @param userToken
     * @purpose used to approve user expense 
     */

    /**
     *
     * @param expense
     * @param userToken
     * @return
     */
    
    public Object rejectMultiExpenseByAccount(Expense[] expense, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.rejectMultiExpenseByAccount(expense, accountId,fieldSenseUtils)) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expenses rejected successfully .", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expenses rejected failed . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to approve user expense 
     */
    public Object disburseExpenseByAccount(Expense expense, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int status = fieldSenseUtils.getExpenseStatus(expense.getId(),accountId);
                if (expenseDao.disburseExpenseByAccount(expense,status, accountId,fieldSenseUtils)) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense disbursed successfully .", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense disbursed failed . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    } 
    
    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to approve user expense 
     */
    public Object disburseExpenseDefaultByAccount(Expense expense, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
             //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                int status = fieldSenseUtils.getExpenseStatus(expense.getId(),accountId);
                if (expenseDao.disburseExpenseDefaultByAccount(expense,status, accountId,fieldSenseUtils)) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense disbursed successfully .", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense disbursed failed . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
    
    /**
     * 
     * @param expense
     * @param userToken
     * @return 
     * @purpose used to approve users all expenses   
     */
    public Object approveUserAllExpenses(Expense expense, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (expenseDao.approveUserAllExpenses(expense.getUser().getId(), expense.getApprovedOrRjectedBy().getId(), accountId)) {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " Expense approved successfully .", "", "");
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Expense approvel failed . Please try again .", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}     
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * 
     * @param userId
     * @param startDate
     * @param endDate
     * @param fromDate
     * @param userToken
     * @return  details of expenses of specific user in specific period
     */
    public Object selectUserExpenseDateWaise(int userId, String startDate, String endDate, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (fieldSenseUtils.isUserValid(userId)) {
                    List<Expense> userExpenseList = new ArrayList<Expense>();
                    //Timestamp start = fieldSenseUtils.converDateToTimestamp(startDate);
                   // Timestamp end = fieldSenseUtils.converDateToTimestamp(endDate);
                    userExpenseList = expenseDao.selectUserExpense(userId, startDate, endDate, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Expense ", userExpenseList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

     /**
     * 
     * @param userId
     * @param startDate
     * @param fromDate
     * @param endDate
     * @param toDate
     * @param expenseStatus
     * @param userToken
     * @return details of expenses of specific user in specific period with specific expense status
     */
    public Object selectUserExpenseDateWaise(int userId, String startDate, String endDate, int expenseStatus, String userToken) {
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                if (fieldSenseUtils.isUserValid(userId)) {
                    List<Expense> userExpenseList = new ArrayList<Expense>();
                   // Timestamp start = fieldSenseUtils.converDateToTimestamp(startDate);
                    //Timestamp end = fieldSenseUtils.converDateToTimestamp(endDate);
                    userExpenseList = expenseDao.selectUserExpense(userId, startDate, endDate, expenseStatus, accountId);
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, " ", " Expense ", userExpenseList);
                } else {
                    return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid userId . Please try again . ", "", "");
                }
            // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}    
        } else {
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }
    
   /**
     * 
     * @param allRequestParams
     * @param userId
     * @param response
     * @param expenseStatus
     * @param expenseCategory
     * @param fromDate
     * @param toDate
     * @param userToken
     * @return details of expenses of specific user in specific period with specific expense status
     */
    public Object selectAccountExpenseDetails(@RequestParam Map<String,String> allRequestParams,String userToken,HttpServletResponse response){
        
        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
            // if (fieldSenseUtils.isUserValid(userId)) {
              //   if (fieldSenseUtils.isUserActive(userId)) {
                     int accountId = fieldSenseUtils.accountIdForToken(userToken);
                     ExpenseFilter expfilter=new ExpenseFilter();
                     expfilter.setUserid(allRequestParams.get("userid"));
                     expfilter.setStatus(allRequestParams.get("status"));
                     expfilter.setExpensecategory(allRequestParams.get("expensecategory"));
                     expfilter.setFromdate(allRequestParams.get("fromdate"));
                     expfilter.setTodate(allRequestParams.get("todate"));
                     List<Expense> expenseDetailList = expenseDao.selectAccountExpenseDetils(allRequestParams,expfilter, accountId);
                     List<Expense> exp_list=new ArrayList<Expense>();
                     double total_amounts=0;
                     for(Expense exp:expenseDetailList){
                         if(!exp.getExpenseTime().equals(Timestamp.valueOf("1111-11-11 11:11:11"))){
                                     exp_list.add(exp);
                         }
                         total_amounts+=exp.getAmountSpent();
                     } 
                     if(!exp_list.isEmpty()){
                         exp_list.get(0).setTotal_amounts(total_amounts);
                     }    
                     return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Expense report list .",exp_list, expenseDetailList.size());
                // } else {
                   //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not active plese contact admin .", "", "");
                // }
            // } else {
            //     return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not registered plese try with diffrent user .", "", "");
             //}
           // }else {
                 //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}          
       } else {
            response.setContentType( MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
        }
    }

    /**
     * @Added by jyoti, 18-02-2018
     * @param allRequestParams
     * @param userToken
     * @param response
     * @return 
     */
    public Object selectUserExpenseDetails(@RequestParam Map<String, String> allRequestParams, String userToken, HttpServletResponse response) {
        try {
            if (fieldSenseUtils.isTokenValid(userToken)) {
                int accountId = fieldSenseUtils.accountIdForToken(userToken);
                ExpenseFilter expfilter = new ExpenseFilter();
                expfilter.setUserid(allRequestParams.get("userid"));
                expfilter.setStatus(allRequestParams.get("status"));
                expfilter.setExpensecategory(allRequestParams.get("expensecategory"));
                expfilter.setFromdate(allRequestParams.get("fromdate"));
                expfilter.setTodate(allRequestParams.get("todate"));
                List<com.qlc.fieldsense.team.model.TeamMember> subordinateList = null;
                List<Expense> expenseDetailList;
                if (expfilter.getUserid().equals("All")) {
                    int loginUser = fieldSenseUtils.userIdForToken(userToken);
//                    subordinateList = teamDao.selectUserAllSubordinates(loginUser, accountId); // commented,  user type removed and added method to get only immediate subordinates of teamMember type. 
                    subordinateList = teamDao.selectUserPositionsOrderByName(loginUser, accountId);
                    expenseDetailList = expenseDao.selectUserExpenseDetails(subordinateList, allRequestParams, expfilter, accountId);
                } else {
                    expenseDetailList = expenseDao.selectUserExpenseDetails(subordinateList, allRequestParams, expfilter, accountId);
                }
                List<Expense> exp_list = new ArrayList<Expense>();
                double total_amounts = 0;
                for (Expense exp : expenseDetailList) {
                    if (!exp.getExpenseTime().equals(Timestamp.valueOf("1111-11-11 11:11:11"))) {
                        exp_list.add(exp);
                    }
                    total_amounts += exp.getAmountSpent();
                }
                if (!exp_list.isEmpty()) {
                    exp_list.get(0).setTotal_amounts(total_amounts);
                }
                return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Expense report list .", exp_list, expenseDetailList.size());

            } else {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Some exception . Please try again . ", "", "");
        }
    }
    
    /**
     * @Added by manohar
     * @param teamMember
     * @param category
     * @param status
     * @param fromDate
     * @param todate
     * @param userToken
     * @return 
     */
        public Object selectAccountExpenseDetailscsv(String teamMember, String category, String status, String fromDate, String todate, String userToken) {

        if (fieldSenseUtils.isTokenValid(userToken)) {
            //if(fieldSenseUtils.isSessionExpired(userToken)){
            // if (fieldSenseUtils.isUserValid(userId)) {
            //   if (fieldSenseUtils.isUserActive(userId)) {
            int accountId = fieldSenseUtils.accountIdForToken(userToken);
            ExpenseFilter expfilter = new ExpenseFilter();
            expfilter.setUserid(teamMember);
            expfilter.setStatus(status);
            expfilter.setExpensecategory(category);
            expfilter.setFromdate(fromDate);
            expfilter.setTodate(todate);
            List<Expense> expenseDetailList = expenseDao.selectAccountExpenseDetilsCSV(expfilter, accountId);
            List<Expense> exp_list = new ArrayList<Expense>();
            double total_amounts = 0;
            for (Expense exp : expenseDetailList) {
                if (!exp.getExpenseTime().equals(Timestamp.valueOf("1111-11-11 11:11:11"))) {
                    exp_list.add(exp);
                }
                total_amounts += exp.getAmountSpent();
            }
            if (!exp_list.isEmpty()) {
                exp_list.get(0).setTotal_amounts(total_amounts);
            }
//                 return FieldSenseUtils.generateFieldSenseResponseReport(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Expense report csv list .", exp_list, expenseDetailList.size());

            return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_SUCCESS, Constant.SUCCESS, "", "Expense report csv list .", exp_list);
                // } else {
            //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not active plese contact admin .", "", "");
            // }
            // } else {
            //     return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " User is not registered plese try with diffrent user .", "", "");
            //}
            // }else {
            //  return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Session expired . Please try again . ", "", "");
            //}          
        }
        return FieldSenseUtils.generateFieldSenseResponse(Constant.STATUS_FAILED, Constant.FAILED, " Invalid token . Please try again . ", "", "");
    }
         public BufferedImage decodeToImage(String imageString) {
 
        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
         
          public static void resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
              Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
 
        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }
}
