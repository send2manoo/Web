/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import com.qlc.fieldsense.accounts.model.Account;
import com.qlc.fieldsense.feedback.model.Feedback;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;

/**
 *
 * @author anuja
 * @purpose used to send emails
 */
public class EmailNotification {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("EmailNotification");
    FieldSenseUtils fieldSenseUtils = (FieldSenseUtils) GetApplicationContext.ac.getBean("fieldSenseUtils");

    /**
     *
     * @param userName
     * @param emailId
     * @return
     */
    public boolean registerUser(String userName, String emailId) {
        try {
            Map<String, String> data = new HashMap<String, String>();
            String htmlTemplate = readTemplate("FieldSense_reg.html");
            String subject = "Set Your FieldSense Account Password";
            String url = Constant.WEBSITE_PATH + "setPassword.html?email=" + emailId;
            String imagePath = FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH");
            data.put("Header", imagePath + "/emailImages/header.jpg");
            String username = String.valueOf(userName.charAt(0));                //added by manohar  
            String usernameupper = username.toUpperCase() + userName.substring(1);
            data.put("Username", usernameupper);
            data.put("URL", url);
            data.put("Setpwd", imagePath + "/emailImages/set_pwd.png");    // modified by manohar
            data.put("Footer", imagePath + "/emailImages/footer.jpg");
            data.put("Qlclogo", imagePath + "/emailImages/qlc-logo.gif");
            data.put("Facebook", imagePath + "/emailImages/facebook.png");
            data.put("Twitter", imagePath + "/emailImages/twitter.png");
            data.put("Linkedin", imagePath + "/emailImages/linkedin.png");
            String emailData = getEmailBody(data, htmlTemplate);
            sendMail(emailId, emailData, subject);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param userName
     * @param emailId
     * @return
     */
    public boolean createAccount(String userName, String emailId) {
        try {
            log4jLog.info("Inside EmailNotification class createAccount() method ");
            Map<String, String> data = new HashMap<String, String>();
//            String htmlTemplate = readTemplate("FieldSense_reg.html");
            String htmlTemplate = readTemplate("Welcome-email.html");//Welcome-email.html
            String subject = "Welcome to FieldSense!";
            String url = Constant.WEBSITE_PATH + "setPassword.html?email=" + emailId;
            String imagePath = FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH");
            data.put("Header", imagePath + "/emailImages/header.jpg");
            String username = String.valueOf(userName.charAt(0));                //added by manohar  
            String usernameupper = username.toUpperCase() + userName.substring(1);
            data.put("Username", usernameupper);
            data.put("URL", url);
            data.put("top_header", imagePath + "/emailImages/top_header.jpg");
            data.put("trans", imagePath + "/emailImages/trans.gif");
            data.put("add_user_list", imagePath + "/emailImages/add_user_list.png");
            data.put("dfn_report", imagePath + "/emailImages/dfn_report.png");
            data.put("add_cust_list", imagePath + "/emailImages/add_cust_list.png");

            data.put("Setpwd", imagePath + "/emailImages/set_pwd.png");   //// modified by manohar
            data.put("Footer", imagePath + "/emailImages/footer.jpg");
            data.put("Qlclogo", imagePath + "/emailImages/qlc-logo.gif");
            data.put("Facebook", imagePath + "/emailImages/facebook.png");
            data.put("Twitter", imagePath + "/emailImages/twitter.png");
            data.put("Linkedin", imagePath + "/emailImages/linkedin.png");
            String emailData = getEmailBody(data, htmlTemplate);
//            sendMail(emailId, emailData, subject);
            sendMailWithBcc(emailId, emailData, subject, Constant.BCC_EMAIL_ADDRESS1, Constant.BCC_EMAIL_ADDRESS2);
            return false;
        } catch (Exception e) {
            log4jLog.info("Exception Inside EmailNotification class createAccount() method " + e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Added by Jyoti, purpose - to send email to create new account using link
     * @param encryptedKey
     * @param emailAddress
     * @return
     */
    public boolean sendMailToCreateAccountUsingLink(String encryptedKey, String emailAddress) {
        try {
            log4jLog.info("Inside EmailNotification class sendMailToCreateAccountUsingLink() method ");
            Map<String, String> data = new HashMap<String, String>();
            String htmlTemplate = readTemplate("Fieldsense_newaccount.html");
            String subject = "Create Your FieldSense Account";
            String imagePath = FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH");
            String activateAccountLinkURL = Constant.VERIFY_LINK + "account/verifyLink/" + encryptedKey + "/link";
            data.put("activateAccountLinkURL", activateAccountLinkURL);
            data.put("top_header", imagePath + "/emailImages/top_header.jpg");
            data.put("btn_create_account", imagePath + "/emailImages/btn_create_account.png");
            data.put("footer", imagePath + "/emailImages/footer.jpg");
            data.put("qlc-logo", imagePath + "/emailImages/qlc-logo.gif");
            data.put("facebook", imagePath + "/emailImages/facebook.png");
            data.put("twitter", imagePath + "/emailImages/twitter.png");
            data.put("linkedin", imagePath + "/emailImages/linkedin.png");

            String emailData = getEmailBody(data, htmlTemplate);
//            System.out.println("emailData :- "+emailData);
            return sendMailWithBcc(emailAddress, emailData, subject, Constant.BCC_EMAIL_ADDRESS1, Constant.BCC_EMAIL_ADDRESS2);
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("Exception Inside EmailNotification class sendMailToCreateAccountUsingLink() method " + e);
            return false;
        }
    }

    /**
     * @Added by Jyoti, purpose - to send email to create new account using link manual send. remove later
     * @param encryptedKey
     * @param emailAddress
     * @return
     */
    public boolean sendMailToCreateAccountUsingLinkManual(String encryptedKey, String emailAddress) {
        try {
            log4jLog.info("Inside EmailNotification class sendMailToCreateAccountUsingLinkManual() method ");
            Map<String, String> data = new HashMap<>();
            String htmlTemplate = readTemplate("fs_account_create.html");
            String subject = "Activate Your FieldSense Account";
            String imagePath = FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH");
            String activateAccountLinkURL = Constant.VERIFY_LINK + "account/verifyLink/onManual/" + encryptedKey + "/link";
            data.put("activateAccountLinkURL", activateAccountLinkURL);
//            data.put("userName", emailAddress);
            data.put("top_header", imagePath + "/emailImages/top_header.jpg");
            data.put("footer", imagePath + "/emailImages/footer.jpg");
            data.put("qlc-logo", imagePath + "/emailImages/qlc-logo.gif");
            data.put("facebook", imagePath + "/emailImages/facebook.png");
            data.put("twitter", imagePath + "/emailImages/twitter.png");
            data.put("linkedin", imagePath + "/emailImages/linkedin.png");
            data.put("act_acc_btn", imagePath + "/emailImages/act_acc.png");

            String emailData = getEmailBody(data, htmlTemplate);
//            System.out.println("emailData :- "+emailData);
            return sendMailWithBcc(emailAddress, emailData, subject, Constant.BCC_EMAIL_ADDRESS1, Constant.BCC_EMAIL_ADDRESS2);
        } catch (Exception e) {
            e.printStackTrace();
            log4jLog.info("Exception Inside EmailNotification class sendMailToCreateAccountUsingLinkManual() method " + e);
            return false;
        }
    }

    /**
     *
     * @param feedback
     * @param emailId
     * @return
     */
    public boolean userFeedback(Feedback feedback, String emailId) {
        log4jLog.info("Inside EmailNotification userFeedback() method ");
        try {
            Map<String, String> data = new HashMap<String, String>();
            String htmlTemplate = readTemplate("FieldSense_Feedback.html");
            String subject = "FieldSense WebApp Feedback";
            String imagePath = FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH");
            data.put("Header", imagePath + "/emailImages/header.jpg");
            data.put("Feedback", feedback.getFeedback());
            data.put("Section", feedback.getSection());
            data.put("Username", feedback.getUserName());
            data.put("UserEmailAddres", feedback.getEmailId());
            data.put("Companyname", feedback.getAccountName());
            data.put("FeedbackTime", feedback.getFeedbackTime());
            data.put("Footer", imagePath + "/emailImages/footer.jpg");
            data.put("Qlclogo", imagePath + "/emailImages/qlc-logo.gif");
            data.put("Facebook", imagePath + "/emailImages/facebook.png");
            data.put("Twitter", imagePath + "/emailImages/twitter.png");
            data.put("Linkedin", imagePath + "/emailImages/linkedin.png");
            String emailData = getEmailBody(data, htmlTemplate);
            return sendMail(emailId, emailData, subject);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param userName
     * @param emailId
     * @return
     */
    public boolean forgotPassword(String userName, String emailId) {
        try {
            Map<String, String> data = new HashMap<String, String>();
//            String htmlTemplate = readTemplate("FieldSense_pwd.html");
            String htmlTemplateForAdmin = readTemplate("FieldSense_pwd_admin.html");
            String htmlTemplateForUsers = readTemplate("FieldSense_pwd_user.html");
            String subject = "FieldSense - Forgot Password";
            String url = Constant.WEBSITE_PATH + "resetPassword.html?email=" + emailId;
            String imagePath = FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH");
            data.put("Header", imagePath + "/emailImages/header.jpg");
            String User[] = userName.split(" ");          //added by manohar  
//            System.out.println("User=" + User[0]);
            String username = String.valueOf(User[0].charAt(0));
            String usernameupper = username.toUpperCase() + User[0].substring(1);
            data.put("Username", usernameupper);
            data.put("URL", url);
            data.put("Setpwd", imagePath + "/emailImages/reset_pwd.png");  // modified by manohar
            data.put("Footer", imagePath + "/emailImages/footer.jpg");
            data.put("Qlclogo", imagePath + "/emailImages/qlc-logo.gif");
            data.put("Facebook", imagePath + "/emailImages/facebook.png");
            data.put("Twitter", imagePath + "/emailImages/twitter.png");
            data.put("Linkedin", imagePath + "/emailImages/linkedin.png");
            //added by manohar
            int status;
            status = fieldSenseUtils.getUserRole(emailId);
//            System.out.println("status=" + status);
            if (status == 1 || status == 0) {
                String emailData = getEmailBody(data, htmlTemplateForAdmin);
                sendMail(emailId, emailData, subject);
                return true;
            } else {
                String emailData = getEmailBody(data, htmlTemplateForUsers);
                sendMail(emailId, emailData, subject);
                return true;
            }
            //ended by manohar
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param emailId
     * @param msgToSend
     * @param subject
     * @return
     */
    public boolean sendMail(String emailId, String msgToSend, String subject) {
        try {
            // Added-Modified-Removed code by Jyoti, 17-may-2017 Request from bappa for authentication.
            Session mailSession = null;
            mailSession = setSession(mailSession);
            // Ended By Jyoti
            Transport transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject(subject);
            message.setFrom(new InternetAddress(Constant.MAIL_SENDER_ID)); // Modified by Jyoti 12-may-2017, sender email id
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(emailId));

            //Added By Awaneesh on supports request
            //message.addRecipient(Message.RecipientType.TO,
            // new InternetAddress("support@qlc.in"));
            message.addRecipient(Message.RecipientType.CC,
                    new InternetAddress(Constant.MAIL_CC_ID)); // Modified by Jyoti 12-may-2017, Mail CC id
            //End by Awaneesh
            MimeMultipart multipart = new MimeMultipart("related");

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(msgToSend, "text/html;charset=UTF-8");

            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            transport.connect();
            transport.sendMessage(message,
                    message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (Exception e) {
            log4jLog.info(" sendMail " + e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     * @param emailId
     * @param msgToSend
     * @param subject
     * @param emailbcc1
     * @param emailbcc2
     * @return
     */
    public boolean sendMailWithBcc(String emailId, String msgToSend, String subject, String emailbcc1, String emailbcc2) {
        try {
            // Added-Modified-Removed code by Jyoti, 17-may-2017 Request from bappa for authentication.
            Session mailSession = null;
            mailSession = setSession(mailSession);
            // Ended by Jyoti
            Transport transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject(subject);
            message.setFrom(new InternetAddress(Constant.MAIL_SENDER_ID));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailId));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(emailbcc1));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(emailbcc2));
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(msgToSend, "text/html;charset=UTF-8");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            transport.connect();
//            transport.sendMessage(message,
//                    message.getRecipients(Message.RecipientType.TO));
//            System.out.println(" send message with bcc ");
            transport.send(message);
            transport.close();
        } catch (Exception e) {
            log4jLog.info(" sendMailWithBcc " + e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     * @param mailTo
     * @param mailBody
     * @param subject
     * @param file
     * @return
     */
    public boolean sendEmailWithAttachments(String mailTo, String mailBody, String subject, File file) {
        try {
            // Added-Modified-Removed code by Jyoti, 17-may-2017 Request from bappa for authentication.
            Session mailSession = null;
            setSession(mailSession);
            // Ended by Jyoti
            MimeMessage msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(Constant.MAIL_SENDER_ID));
            InternetAddress[] address = {new InternetAddress(mailTo)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);

            // create and fill the first message part
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(mailBody);

            // create the second message part
            MimeBodyPart mbp2 = new MimeBodyPart();

            // attach the file to the message
            FileDataSource fds = new FileDataSource(file);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(fds.getName());

            // create the Multipart and add its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);

            // add the Multipart to the message
            msg.setContent(mp);

            // set the Date: header
            msg.setSentDate(new Date());

            // send the message
            Transport.send(msg);

        } catch (Exception e) {
            log4jLog.info(" sendEmailWithAttachments " + e);
            e.printStackTrace();
            return false;
        }
        return true;

    }

    // Added by jyoti, 17-may-2017
    private static Session setSession(Session mailSession) {
        try {
            Properties properties = System.getProperties();
            properties.setProperty("mail.transport.protocol", Constant.MAIL_PROTOCOL);
            properties.setProperty("mail.host", Constant.MAIL_HOST_IP);
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.starttls.enable", "true");

            // creates a new session with an authenticator
            Authenticator auth = new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(Constant.MAIL_AUTH_ID, Constant.MAIL_AUTH_PASSWORD);
                }
            };
            mailSession = Session.getInstance(properties, auth);
//            mailSession.setDebug(true);
        } catch (Exception e) {
            log4jLog.info(" setSession " + e);
            e.printStackTrace();
            return null;
        }
        return mailSession;
    }
    // Ended by Jyoti

    public static String getEmailBody(Map<String, String> data, String htmlTemplate) { // private changed to public by jyoti, rechange later
        String retVal = htmlTemplate;

        for (String key : data.keySet()) {
            String value = data.get(key);

            retVal = retVal.replaceAll("@" + key, value);
           if(key =="")
            System.out.println("retVal="+retVal);
        }
        return retVal;
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static String readTemplate(String fileName) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String templatePath = Constant.EMAIL_TEMPLATE_PATH;
//        String templatePath = Constant.PROFILEPIC_UPLOAD_PATH;
//        System.out.println("fileName >> "+fileName);
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(templatePath + "/" + fileName));
            while ((sCurrentLine = br.readLine()) != null) {
                sb.append(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
            log4jLog.info("readTemplate e "+e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                log4jLog.info("readTemplate ex " + ex);
                ex.printStackTrace();
            }
        }
        return sb.toString();
    }

    //by nikhil:- mail to customer service
//    public boolean customerService(String region_state, Account account) {
//        
//        System.out.println("account.getUserEmailId():- "+account.getUserEmailId());
//        String customer_service_email = "sales."+region_state.toLowerCase()+"@qlc.in";
//
//        System.out.println("mail sent to customer service region email:- " + customer_service_email);
//          try {
//            Map<String, String> data = new HashMap<String, String>();
//            String htmlTemplate = readTemplate("FieldSense_New_Account.html");
//            String subject = "Invitation to FieldSense registration";
//            //String url = Constant.WEBSITE_PATH + "setPassword.html?email=" + emailId;
//            String imagePath = FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH");
//            data.put("Header", imagePath + "/emailImages/header.jpg");
//            String username = account.getFirstUserName();                //added by manohar  
//            data.put("Username", account.getFirstUserName());
//            data.put("UserEmailId", account.getUserEmailId());
//            data.put("UserMobileNo.", account.getUserMobileNumber());
//            data.put("CompanyName", account.getCompanyName());
//            data.put("PhoneNumber",Long.toString(account.getCompanyPhoneNumber1()));
//            data.put("Address", account.getAddress1());
//            data.put("Country", account.getCountry());
//            data.put("State", account.getState());
//            data.put("City", account.getCity());
//            data.put("Website",account.getCompanyWebsite());
//            data.put("ZipCode", account.getZipCode());
//            data.put("Industry", account.getIndustry());
//            data.put("plan", account.getPlan());
//            data.put("No.Ofusers", String.valueOf(account.getUserLimit()));
//            data.put("PaymentCycle", account.getPaymentCycle());
//            //do changes
//            int TotalAmmount;
//            if(account.getUserLimit()>25)
//            {    
//                if(account.getCountry().equalsIgnoreCase("India"))
//                {
//                    TotalAmmount = account.getUserLimit()*250;
//                    data.put("TotalAmount", String.valueOf(TotalAmmount)+" Rs");
//                }else
//                {
//                    TotalAmmount = account.getUserLimit()*5;
//                    data.put("TotalAmount", String.valueOf(TotalAmmount)+"$");
//                }
//            data.put("TotalAmount:", "15000");
//            }else
//            {
//                if(account.getCountry().equalsIgnoreCase("India"))
//                {
//                                      data.put("TotalAmount", "0 Rs");
//                }else
//                {
//                   data.put("TotalAmount", "0 $");
//                }
//            }
//            //data.put("UserEmail");
//            data.put("topHeader", imagePath + "/emailImages/top_header.jpg");
//            data.put("Setpwd", imagePath + "/emailImages/set_pwd.png");    // modified by manohar
//            data.put("Footer", imagePath + "/emailImages/footer.jpg");
//            data.put("Qlclogo", imagePath + "/emailImages/qlc-logo.gif");
//            data.put("Facebook", imagePath + "/emailImages/facebook.png");
//            data.put("Twitter", imagePath + "/emailImages/twitter.png");
//            data.put("Linkedin", imagePath + "/emailImages/linkedin.png");
//            String emailData = getEmailBody(data, htmlTemplate);
//            sendMail("niks.bhosale129@gmail.com", emailData, subject);
//            return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
     public Object customerService(String region_state, Account account) {
        
//        System.out.println("account.getUserEmailId():- "+account.getUserEmailId());
//         System.out.println("region_state :- inside method :- "+region_state);
        String customer_service_email = "sales."+region_state.toLowerCase()+"@qlc.in";
        account.setSalesEmailId(customer_service_email);
//        System.out.println("mail sent to customer service region email:- " + customer_service_email);
          try {
            Map<String, String> data = new HashMap<String, String>();
            String htmlTemplate = readTemplate("FieldSense_New_Account.html");
//            System.out.println("htmlTemplate="+htmlTemplate);

            String subject = "FieldSense New Account";
            //String url = Constant.WEBSITE_PATH + "setPassword.html?email=" + emailId;
            String imagePath = FieldSenseUtils.getPropertyValue("PROFILEPIC_GET_PATH");
//              System.out.println("imagepath="+imagePath);
            data.put("Header", imagePath + "/emailImages/header.jpg");
            String username = account.getFirstUserName();                //added by manohar  
            data.put("Username", account.getFirstUserName());
            data.put("UserEmailId", account.getUserEmailId());
            data.put("UserMobileNo", account.getUserMobileNumber());
            data.put("CompanyName", account.getCompanyName());
            data.put("PhoneNumber",Long.toString(account.getCompanyPhoneNumber1()));
            data.put("Address", account.getAddress1());
            data.put("Country", account.getCountry());
            data.put("State", account.getState());
            data.put("City", account.getCity());
            data.put("Website",account.getCompanyWebsite());
            data.put("ZipCode", account.getZipCode());
            data.put("Industry", account.getIndustry());
            data.put("plan", account.getPlan());
            data.put("No.Ofusers", String.valueOf(account.getUserLimit()));
            data.put("PaymentCycle", account.getPaymentCycle());
            //do changes
            int TotalAmmount;
            if(account.getUserLimit()>25 & account.getPaymentCycle().equalsIgnoreCase("Monthly") )
            {    
                if(account.getCountry().equalsIgnoreCase("India"))
                {
                    TotalAmmount = account.getUserLimit()*250;
                    data.put("TotalAmount", String.valueOf(TotalAmmount)+" Rs");
                }else
                {
                    TotalAmmount = account.getUserLimit()*5;
                    data.put("TotalAmount", String.valueOf(TotalAmmount)+"$");
                }
            //data.put("TotalAmount:", "15000");
            }else if(account.getUserLimit()>25 & account.getPaymentCycle().equalsIgnoreCase("Annual") )
            {    
                if(account.getCountry().equalsIgnoreCase("India"))
                {
                    TotalAmmount = account.getUserLimit()*2500;
                    data.put("TotalAmount", String.valueOf(TotalAmmount)+" Rs");
                }else
                {
                    TotalAmmount = account.getUserLimit()*50;
                    data.put("TotalAmount", String.valueOf(TotalAmmount)+"$");
                }
            //data.put("TotalAmount:", "15000");
            }
            
            else
            {
                if(account.getCountry().equalsIgnoreCase("India"))
                {
                                      data.put("TotalAmount", "0 Rs");
                }else
                {
                   data.put("TotalAmount", "0 $");
                }
            }
            //data.put("UserEmail");
            data.put("topHeader", imagePath + "/emailImages/top_header.jpg");
            data.put("Setpwd", imagePath + "/emailImages/set_pwd.png");    // modified by manohar
            data.put("Footer", imagePath + "/emailImages/footer.jpg");
            data.put("Qlclogo", imagePath + "/emailImages/qlc-logo.gif");
            data.put("Facebook", imagePath + "/emailImages/facebook.png");
            data.put("Twitter", imagePath + "/emailImages/twitter.png");
            data.put("Linkedin", imagePath + "/emailImages/linkedin.png");
            String emailData = getEmailBody(data, htmlTemplate);
//              System.out.println("emailData="+emailData);
            boolean flag=sendMail(customer_service_email, emailData, subject);
            boolean flag1=sendMail("info@qlc.in", emailData, subject);
//            boolean flag=sendMail("dk.sk874@gmail.com", emailData, subject);
//              System.out.println("flag="+flag + "flag1 for info@qlc.in >"+flag1);
            
            return account;
        } catch (Exception e) {
            e.printStackTrace();
            return "mail not sent";
        }
    }

}
