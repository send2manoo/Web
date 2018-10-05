/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.addressConverter;

/**
 *
 * @author Ramesh
 * @date 20-08-2014
 */
public class GoogleResponse {

    private Result[] results;
    private String status;
    private String error_message;

    /**
     *
     * @return
     */
    public String getError_message() {
        return error_message;
    }

    /**
     *
     * @param error_message
     */
    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
    
    /**
     *
     * @return
     */
    public Result[] getResults() {
        return results;
    }

    /**
     *
     * @param results
     */
    public void setResults(Result[] results) {
        this.results = results;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
