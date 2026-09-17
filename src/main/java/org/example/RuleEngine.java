package org.example;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RuleEngine {
    public static BigDecimal getCashOutAvg() throws SQLException {
        BigDecimal avg;

        try(Connection conn = DatabaseConnector.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select avg(amount) as avg_amount from transactions where type='CASH_OUT'")) {

            if (rs.next()) {
                avg = rs.getBigDecimal("avg_amount");
                return avg;
            }
        }
        return null;
    }
    public static List<Transaction> readTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();

        try(Connection conn = DatabaseConnector.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM transactions")) {

            while (rs.next()) {
                transactions.add(new Transaction(rs.getInt("transaction_id"), rs.getInt("step"),
                        rs.getString("type"), rs.getBigDecimal("amount"),
                        rs.getString("orig_account_id"), rs.getString("dest_account_id"),
                        rs.getBigDecimal("oldbalanceorg"), rs.getBigDecimal("newbalanceorg"),
                        rs.getBigDecimal("oldbalancedest"), rs.getBigDecimal("newbalancedest"),
                        rs.getBoolean("isfraud"), rs.getBoolean("isflaggedfraud")));
            }
        }

        return transactions;
    }
    public static Alert nearTransferLimit(Transaction t) {
        if(t.getAmount().compareTo(new BigDecimal("9000000")) >= 0) {
            return new Alert(t.getTransactionId(),0, "near_transfer_limit");
        }
        return null;
    }
    public static Alert aboveAvgCashout(Transaction t,BigDecimal avg){
        if((t.getType().equals("CASH_OUT")) && (t.getAmount().compareTo(avg) > 0)){
            return new Alert (t.getTransactionId(),0,"above_avg_cash_out");
        }
        return null;
    }
    public static Alert drainedAccount(Transaction t) {
        if (t.getType().equals("TRANSFER") && (t.getOldbalanceorg().compareTo(new BigDecimal("0")) > 0)
                && t.getNewbalanceorg().compareTo(new BigDecimal("0")) == 0) {
            return new Alert(t.getTransactionId(),0, "drained_account_transfer");

        }
        if (t.getType().equals("CASH_OUT") && t.getOldbalanceorg().compareTo(new BigDecimal("0")) > 0
                && t.getNewbalanceorg().compareTo(new BigDecimal("0")) == 0) {
            return new Alert(t.getTransactionId(),0,  "drained_account_cashout");
        }
        return null;
    }
    public static List<Alert> runAllRules(List<Transaction> transactions){
        List<Alert> alerts=new ArrayList<>();
        Alert a;
        BigDecimal avg;
        try {
            avg=getCashOutAvg();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(int i=0;i < transactions.size();i++){
            a=nearTransferLimit(transactions.get(i));
           if(a!=null){
               alerts.add(a);
           }
           a=aboveAvgCashout(transactions.get(i),avg);
           if(a!=null){
               alerts.add(a);
           }
           a=drainedAccount(transactions.get(i));
           if(a!=null){
                alerts.add(a);
           }
        }
        return alerts;
    }
    public static void writeAlerts(List<Alert> alerts) throws SQLException {
        String sql = "INSERT INTO alerts (transaction_id, cause) VALUES (?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Alert a : alerts) {
                ps.setInt(1, a.getTransactionId());
                ps.setString(2, a.getCause());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}