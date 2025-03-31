package app.controller;

import java.util.List;

import app.dao.PayrollDAO;
import app.model.PayStatement;
import app.model.User;

public class PayHistoryController {
    public static List<PayStatement> fetchStatements(User user) {
        if (user.isAdmin()) {
            return PayrollDAO.getAllPayStatements();
        } else {
            return PayrollDAO.getPayStatements(user.getEmpid());
        }
    }
    
}
