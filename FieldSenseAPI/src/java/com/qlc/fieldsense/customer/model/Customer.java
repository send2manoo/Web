/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.customer.model;

import com.qlc.fieldsense.customAnnotations.*;
import com.qlc.fieldsense.customerContact.model.CustomerContacts;
import com.qlc.fieldsense.user.model.User;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Ramesh
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {

    private int id;
    @NotEmpty
    @Length(min = 1, max = 200)
    @TrimSpaceValidation(message = "Please enter valid data")
//    @UniqueCustomerName(message = "Customer name already exists")
    private String customerName;
    @ValidSize(size = 200, message = "Size must be less than 200")
    private String customerPrintas;
    @ValidSize(size = 100, message = "Size must be less than 100")
    private String customerLocation;
    private boolean isHeadOffice;
    @ValidSize(size = 100, message = "Size must be less than 100")
    private String territory;
    @ValidSize(size = 100, message = "Size must be less than 100")
    private String industry;
    private String customerAddress1;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String customerPhone1;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String customerPhone2;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String customerPhone3;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String customerFax1;
    @ValidSize(size = 50, message = "Size must be less than 50")
    private String customerFax2;
    @ValidSize(size = 100, message = "Size must be less than 100")
//    @ValidDomain(message = "Domain must not be public")
//    @ValidEmail(message = "Email must be valid")
    private String customerEmail1;
    @ValidSize(size = 100, message = "Size must be less than 100")
//    @ValidDomain(message = "Domain must not be public")
//    @ValidEmail(message = "Email must be valid")
    private String customerEmail2;
    @ValidSize(size = 100, message = "Size must be less than 100")
//    @ValidEmail(message = "Email must be valid")
//    @ValidDomain(message = "Domain must not be public")
    private String customerEmail3;
    @ValidSize(size = 200, message = "Size must be less than 200")
    private String customerWebsiteUrl1;
    @ValidSize(size = 200, message = "Size must be less than 200")
    private String customerWebsiteUrl2;
    private Timestamp createdOn;
    private double lasknownLatitude;
    private double lasknownLangitude;
    private int customerType;
    private int customersCount;
    private List<CustomerContacts> customerContacts;
    
    //added by shivakrisna for newly added columns in customers table
    private User created_by;
    private Timestamp modifiedOn;
    private User modified_by;
    private int recordState;
    private int created_by_id_fk; // added by manohar dashboard
    /**
     *
     */
    public Customer() {
        this.id = 0;
        this.customerName = "";
        this.customerPrintas = "";
        this.customerLocation = "";
        this.isHeadOffice = false;
        this.territory = "";
        this.industry = "";
        this.customerAddress1 = "";
        this.customerPhone1 = "";
        this.customerPhone2 = "";
        this.customerPhone3 = "";
        this.customerFax1 = "";
        this.customerFax2 = "";
        this.customerEmail1 = "";
        this.customerEmail2 = "";
        this.customerEmail3 = "";
        this.customerWebsiteUrl1 = "";
        this.customerWebsiteUrl2 = "";
        this.createdOn = new Timestamp(0);
        this.lasknownLatitude = 0.0;
        this.lasknownLangitude = 0.0;
        this.customerType = 0;
        this.customerContacts = new ArrayList<CustomerContacts>();
        this.customersCount = 0;
        this.created_by=new User();
        this.modifiedOn = new Timestamp(0);
        this.modified_by=new User();
        this.recordState = 0;
        this.created_by_id_fk=0;
    }

    /**
     *
     * @return
     */
    public Timestamp getCreatedOn() {
        return createdOn;
    }

    /**
     *
     * @param createdOn
     */
    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    /**
     *
     * @return
     */
    public String getCustomerAddress1() {
        return customerAddress1;
    }

    /**
     *
     * @param customerAddress1
     */
    public void setCustomerAddress1(String customerAddress1) {
        this.customerAddress1 = customerAddress1;
    }

    /**
     *
     * @return
     */
    public String getCustomerEmail1() {
        return customerEmail1;
    }

    /**
     *
     * @param customerEmail1
     */
    public void setCustomerEmail1(String customerEmail1) {
        this.customerEmail1 = customerEmail1;
    }

    /**
     *
     * @return
     */
    public String getCustomerEmail2() {
        return customerEmail2;
    }

    /**
     *
     * @param customerEmail2
     */
    public void setCustomerEmail2(String customerEmail2) {
        this.customerEmail2 = customerEmail2;
    }

    /**
     *
     * @return
     */
    public String getCustomerEmail3() {
        return customerEmail3;
    }

    /**
     *
     * @param customerEmail3
     */
    public void setCustomerEmail3(String customerEmail3) {
        this.customerEmail3 = customerEmail3;
    }

    /**
     *
     * @return
     */
    public String getCustomerFax1() {
        return customerFax1;
    }

    /**
     *
     * @param customerFax1
     */
    public void setCustomerFax1(String customerFax1) {
        this.customerFax1 = customerFax1;
    }

    /**
     *
     * @return
     */
    public String getCustomerFax2() {
        return customerFax2;
    }

    /**
     *
     * @param customerFax2
     */
    public void setCustomerFax2(String customerFax2) {
        this.customerFax2 = customerFax2;
    }

    /**
     *
     * @return
     */
    public String getCustomerLocation() {
        return customerLocation;
    }

    /**
     *
     * @param customerLocation
     */
    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    /**
     *
     * @return
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     *
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     *
     * @return
     */
    public String getCustomerPhone1() {
        return customerPhone1;
    }

    /**
     *
     * @param customerPhone1
     */
    public void setCustomerPhone1(String customerPhone1) {
        this.customerPhone1 = customerPhone1;
    }

    /**
     *
     * @return
     */
    public String getCustomerPhone2() {
        return customerPhone2;
    }

    /**
     *
     * @param customerPhone2
     */
    public void setCustomerPhone2(String customerPhone2) {
        this.customerPhone2 = customerPhone2;
    }

    /**
     *
     * @return
     */
    public String getCustomerPhone3() {
        return customerPhone3;
    }

    /**
     *
     * @param customerPhone3
     */
    public void setCustomerPhone3(String customerPhone3) {
        this.customerPhone3 = customerPhone3;
    }

    /**
     *
     * @return
     */
    public String getCustomerPrintas() {
        return customerPrintas;
    }

    /**
     *
     * @param customerPrintas
     */
    public void setCustomerPrintas(String customerPrintas) {
        this.customerPrintas = customerPrintas;
    }

    /**
     *
     * @return
     */
    public String getCustomerWebsiteUrl1() {
        return customerWebsiteUrl1;
    }

    /**
     *
     * @param customerWebsiteUrl1
     */
    public void setCustomerWebsiteUrl1(String customerWebsiteUrl1) {
        this.customerWebsiteUrl1 = customerWebsiteUrl1;
    }

    /**
     *
     * @return
     */
    public String getCustomerWebsiteUrl2() {
        return customerWebsiteUrl2;
    }

    /**
     *
     * @param customerWebsiteUrl2
     */
    public void setCustomerWebsiteUrl2(String customerWebsiteUrl2) {
        this.customerWebsiteUrl2 = customerWebsiteUrl2;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getIndustry() {
        return industry;
    }

    /**
     *
     * @param industry
     */
    public void setIndustry(String industry) {
        this.industry = industry;
    }

    /**
     *
     * @return
     */
    public boolean isIsHeadOffice() {
        return isHeadOffice;
    }

    /**
     *
     * @param isHeadOffice
     */
    public void setIsHeadOffice(boolean isHeadOffice) {
        this.isHeadOffice = isHeadOffice;
    }

    /**
     *
     * @return
     */
    public String getTerritory() {
        return territory;
    }

    /**
     *
     * @param territory
     */
    public void setTerritory(String territory) {
        this.territory = territory;
    }

    /**
     *
     * @return
     */
    public double getLasknownLangitude() {
        return lasknownLangitude;
    }

    /**
     *
     * @param lasknownLangitude
     */
    public void setLasknownLangitude(double lasknownLangitude) {
        this.lasknownLangitude = lasknownLangitude;
    }

    /**
     *
     * @return
     */
    public double getLasknownLatitude() {
        return lasknownLatitude;
    }

    /**
     *
     * @param lasknownLatitude
     */
    public void setLasknownLatitude(double lasknownLatitude) {
        this.lasknownLatitude = lasknownLatitude;
    }

    /**
     *
     * @return
     */
    public int getCustomerType() {
        return customerType;
    }

    /**
     *
     * @param customerType
     */
    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    /**
     *
     * @return
     */
    public List<CustomerContacts> getCustomerContacts() {
        return customerContacts;
    }

    /**
     *
     * @param customerContacts
     */
    public void setCustomerContacts(List<CustomerContacts> customerContacts) {
        this.customerContacts = customerContacts;
    }

    /**
     *
     * @return
     */
    public int getCustomersCount() {
        return customersCount;
    }

    /**
     *
     * @param customersCount
     */
    public void setCustomersCount(int customersCount) {
        this.customersCount = customersCount;
    }

    /**
     *
     * @return
     */
    public User getCreated_by() {
        return created_by;
    }

    /**
     *
     * @param created_by
     */
    public void setCreated_by(User created_by) {
        this.created_by = created_by;
    }

    /**
     *
     * @return
     */
    public Timestamp getModifiedOn() {
        return modifiedOn;
    }

    /**
     *
     * @param modifiedOn
     */
    public void setModifiedOn(Timestamp modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    /**
     *
     * @return
     */
    public User getModified_by() {
        return modified_by;
    }

    /**
     *
     * @param modified_by
     */
    public void setModified_by(User modified_by) {
        this.modified_by = modified_by;
    }

    public int getRecordState() {
        return recordState;
    }

    public void setRecordState(int recordState) {
        this.recordState = recordState;
    }

    public int getCreated_by_id_fk() {
        return created_by_id_fk;
    }

    public void setCreated_by_id_fk(int created_by_id_fk) {
        this.created_by_id_fk = created_by_id_fk;
    }
    
    
}
