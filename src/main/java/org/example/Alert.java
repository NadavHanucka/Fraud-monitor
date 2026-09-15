package org.example;

public class Alert {
    private int transactionId;
    private int alertId;
    private String cause;

    public Alert(int transactionId,int alertId,String cause){
        this.transactionId=transactionId;
        this.alertId=alertId;
        this.cause=cause;
    }
    public int getTransactionId(){
        return this.transactionId;
    }
    public int getAlertId(){
        return this.alertId;
    }
    public String getCause(){
        return this.cause;
    }
}
