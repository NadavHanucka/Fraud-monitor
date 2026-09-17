package org.example;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class RuleEngineTest {

    @Test
    public void nearTransferLimit_amountAtLimit_returnsAlert() {
        Transaction t = new Transaction(1, 0, "TRANSFER", new BigDecimal("9000000"),
                "origAcc", "destAcc", new BigDecimal("0"), new BigDecimal("0"),
                new BigDecimal("0"), new BigDecimal("0"), false, false);

        Alert result = RuleEngine.nearTransferLimit(t);

        assertNotNull(result);
        assertEquals("near_transfer_limit", result.getCause());
    }

    @Test
    public void nearTransferLimit_amountBelowLimit_returnsNull() {
        Transaction t = new Transaction(2, 0, "TRANSFER", new BigDecimal("8999999"),
                "origAcc", "destAcc", new BigDecimal("0"), new BigDecimal("0"),
                new BigDecimal("0"), new BigDecimal("0"), false, false);

        Alert result = RuleEngine.nearTransferLimit(t);

        assertNull(result);
    }
    @Test
    public void aboveAvgCashout_cashOutAboveAvg_returnsAlert(){
        Transaction t = new Transaction(3, 0, "CASH_OUT", new BigDecimal("8999999"),
                "origAcc", "destAcc", new BigDecimal("0"), new BigDecimal("0"),
                new BigDecimal("0"), new BigDecimal("0"), false, false);

        Alert result = RuleEngine.aboveAvgCashout(t,new BigDecimal("416006"));

        assertNotNull(result);
        assertEquals("above_avg_cash_out",result.getCause());
    }
    @Test
    public void aboveAvgCashout_cashOutBelowAvg_returnsNull(){
        Transaction t = new Transaction(3, 0, "CASH_OUT", new BigDecimal("2"),
                "origAcc", "destAcc", new BigDecimal("0"), new BigDecimal("0"),
                new BigDecimal("0"), new BigDecimal("0"), false, false);

        Alert result = RuleEngine.aboveAvgCashout(t,new BigDecimal("416006"));

        assertNull(result);
    }
    @Test
    public void aboveAvgCashout_notCashOut_returnsNull(){
        Transaction t = new Transaction(3, 0, "TRANSFER", new BigDecimal("2"),
                "origAcc", "destAcc", new BigDecimal("0"), new BigDecimal("0"),
                new BigDecimal("0"), new BigDecimal("0"), false, false);
        Alert result = RuleEngine.aboveAvgCashout(t,new BigDecimal("416006"));

        assertNull(result);
    }
    @Test
    public void drainedAccount_transferDrained_returnsTransferAlert() {
        Transaction t = new Transaction(4, 0, "TRANSFER", new BigDecimal("100"),
                "origAcc", "destAcc", new BigDecimal("5000"), new BigDecimal("0"),
                new BigDecimal("0"), new BigDecimal("0"), false, false);

        Alert result = RuleEngine.drainedAccount(t);

        assertNotNull(result);
        assertEquals("drained_account_transfer", result.getCause());
    }

    @Test
    public void drainedAccount_cashOutDrained_returnsCashOutAlert() {
        Transaction t = new Transaction(5, 0, "CASH_OUT", new BigDecimal("100"),
                "origAcc", "destAcc", new BigDecimal("5000"), new BigDecimal("0"),
                new BigDecimal("0"), new BigDecimal("0"), false, false);

        Alert result = RuleEngine.drainedAccount(t);

        assertNotNull(result);
        assertEquals("drained_account_cashout", result.getCause());
    }

    @Test
    public void drainedAccount_transferNotFullyDrained_returnsNull() {
        Transaction t = new Transaction(6, 0, "TRANSFER", new BigDecimal("100"),
                "origAcc", "destAcc", new BigDecimal("5000"), new BigDecimal("2000"),
                new BigDecimal("0"), new BigDecimal("0"), false, false);

        Alert result = RuleEngine.drainedAccount(t);

        assertNull(result);
    }

    @Test
    public void drainedAccount_oldBalanceAlreadyZero_returnsNull() {
        Transaction t = new Transaction(7, 0, "TRANSFER", new BigDecimal("100"),
                "origAcc", "destAcc", new BigDecimal("0"), new BigDecimal("0"),
                new BigDecimal("0"), new BigDecimal("0"), false, false);

        Alert result = RuleEngine.drainedAccount(t);

        assertNull(result);
    }

    @Test
    public void drainedAccount_otherType_returnsNull() {
        Transaction t = new Transaction(8, 0, "PAYMENT", new BigDecimal("100"),
                "origAcc", "destAcc", new BigDecimal("5000"), new BigDecimal("0"),
                new BigDecimal("0"), new BigDecimal("0"), false, false);

        Alert result = RuleEngine.drainedAccount(t);

        assertNull(result);
    }
}
