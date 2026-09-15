package org.example;
import java.math.BigDecimal;

public class Transaction {
    private int transactionId;
    private int step;
    private String type;
    private BigDecimal amount;
    private String origAccountId;
    private String destAccountId;
    private BigDecimal oldbalanceorg;
    private BigDecimal newbalanceorg ;
    private BigDecimal oldbalancedest ;
    private BigDecimal newbalancedest ;
    private boolean isFraud;
    private boolean isFlaggedFraud;

    public Transaction(int transactionId, int step, String type, BigDecimal amount, String origAccountId,
                       String destAccountId, BigDecimal oldbalanceorg,BigDecimal newbalanceorg ,
                       BigDecimal oldbalancedest ,BigDecimal newbalancedest , boolean isFraud,boolean isFlaggedFraud) {
        this.transactionId = transactionId;
        this.step = step;
        this.type = type;
        this.amount = amount;
        this.origAccountId = origAccountId;
        this.destAccountId = destAccountId;
        this.oldbalanceorg = oldbalanceorg;
        this.newbalanceorg = newbalanceorg;
        this.oldbalancedest = oldbalancedest;
        this.newbalancedest = newbalancedest;
        this.isFraud = isFraud;
        this.isFlaggedFraud = isFlaggedFraud;
    }

    public int getTransactionId(){
        return this.transactionId;
    }
    public int getStep(){
        return this.step;
    }
    public String getType(){
        return this.type;
    }
    public BigDecimal getAmount(){
        return this.amount;
    }
    public String getOrigAccountid(){
        return this.origAccountId;
    }
    public String getDestAccountid(){
        return this.destAccountId;
    }
    public BigDecimal getOldbalanceorg(){
        return this.oldbalanceorg;
    }
    public BigDecimal getNewbalanceorg(){
        return this.newbalanceorg;
    }
    public BigDecimal getOldbalancedest(){
        return this.oldbalancedest;
    }
    public BigDecimal getNewbalancedest(){
        return this.newbalancedest;
    }
    public boolean IsFraud(){
        return this.isFraud;
    }
    public boolean IsFlaggedFraud(){
        return this.isFlaggedFraud;
    }


}
