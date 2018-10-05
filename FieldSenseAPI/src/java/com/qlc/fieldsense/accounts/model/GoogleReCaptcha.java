/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.accounts.model;

/**
 *
 * @author Jyoti <singh.jyoti at QLC>
 */
public class GoogleReCaptcha {
    
    private String url;
    private String domainSecretKey;
    private String userResponse;
    
    public GoogleReCaptcha(){
        this.url = "";
        this.domainSecretKey = "";
        this.userResponse = "";
    }

    public String getDomainSecretKey() {
        return domainSecretKey;
    }

    public void setDomainSecretKey(String domainSecretKey) {
        this.domainSecretKey = domainSecretKey;
    }

    public String getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(String userResponse) {
        this.userResponse = userResponse;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
}
