
--Those rules are for documentation only, its the same logic as in RuleEngine

-- Rule 1: near_transfer_limit
INSERT INTO alerts (transaction_id, cause)
SELECT transaction_id, 'near_transfer_limit' AS cause
FROM transactions
WHERE amount >= 9000000;

-- Rule 2: above_avg_cash_out
INSERT INTO alerts (transaction_id, cause)
SELECT transaction_id, 'above_avg_cash_out'
FROM transactions
WHERE type = 'CASH_OUT'
  AND amount > (SELECT avg(amount) FROM transactions WHERE type = 'CASH_OUT');

-- Rule 3: drained_account_transfer
INSERT INTO alerts (transaction_id, cause)
SELECT transaction_id, 'drained_account_transfer'
FROM transactions
WHERE oldbalanceorg > 0 AND newbalanceorg = 0 AND type = 'TRANSFER';

-- Rule 4: drained_account_cashout
INSERT INTO alerts (transaction_id, cause)
SELECT transaction_id, 'drained_account_cashout'
FROM transactions
WHERE oldbalanceorg > 0 AND newbalanceorg = 0 AND type = 'CASH_OUT';