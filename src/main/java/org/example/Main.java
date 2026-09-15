package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            System.out.println("Connected successfully!");
            List<Transaction> all = RuleEngine.readTransactions();
            List<Alert> alerts = RuleEngine.runAllRules(all);
            RuleEngine.writeAlerts(alerts);
            System.out.println("Alerts written to DB!");
            System.out.println(alerts.size());
            System.out.println(all.size());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}