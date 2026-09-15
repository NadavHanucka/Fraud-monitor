package org.example;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RuleEngine {
    public static BigDecimal GetCashOutAvg() throws SQLException {
        BigDecimal avg;

        Connection conn = DatabaseConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select avg(amount) as avg_amount from transactions where type='CASH_OUT'");

        if(rs.next()){
            avg=rs.getBigDecimal("avg_amount");
            return avg;
        }
        return null;
    }
    public static List<Transaction> readTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();

        Connection conn = DatabaseConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM transactions");

        while (rs.next()) {
            transactions.add(new Transaction(rs.getInt("transaction_id"),rs.getInt("step"),
                    rs.getString("type"),rs.getBigDecimal("amount"),
                    rs.getString("orig_account_id"), rs.getString("dest_account_id"),
                    rs.getBigDecimal("oldbalanceorg"),rs.getBigDecimal("newbalanceorg"),
                    rs.getBigDecimal("oldbalancedest"),rs.getBigDecimal("newbalancedest"),
                    rs.getBoolean("isfraud"),rs.getBoolean("isflaggedfraud")));
        }

        return transactions;
    }
    public static Alert NearTransferLimit(Transaction t) {
        if(t.getAmount().compareTo(new BigDecimal("9000000")) >= 0) {
            return new Alert(t.getTransactionId(),0, "near_transfer_limit");
        }
        return null;
    }
    public static Alert AboveAvgCashout(Transaction t,BigDecimal avg){
        if((t.getType().equals("CASH_OUT")) && (t.getAmount().compareTo(avg) > 0)){
            return new Alert (t.getTransactionId(),0,"above_avg_cash_out");
        }
        return null;
    }
    public static Alert DrainedAccount(Transaction t) {
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
            avg=GetCashOutAvg();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(int i=0;i < transactions.size();i++){
            a=NearTransferLimit(transactions.get(i));
           if(a!=null){
               alerts.add(a);
           }
           a=AboveAvgCashout(transactions.get(i),avg);
           if(a!=null){
               alerts.add(a);
           }
           a=DrainedAccount(transactions.get(i));
           if(a!=null){
                alerts.add(a);
           }
        }
        return alerts;
    }
    public static void writeAlerts(List<Alert> alerts) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        String sql = "INSERT INTO alerts (transaction_id, cause) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        for (Alert a : alerts) {
            ps.setInt(1, a.getTransactionId());
            ps.setString(2, a.getCause());
            ps.addBatch();
        }
        ps.executeBatch();
    }
}