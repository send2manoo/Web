/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.dashboard.model;

import com.qlc.fieldsense.expense.model.Expense;
import com.qlc.fieldsense.user.model.User;

/**
 *
 * @author root
 */
public class ExpenseStats {
    private User user;
    private Expense expense;
    private double payment_mode;

    public ExpenseStats() {
        this.user = null;
        this.expense = null;
        this.payment_mode=0.0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public double getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(double payment_mode) {
        this.payment_mode = payment_mode;
    }
    
}
