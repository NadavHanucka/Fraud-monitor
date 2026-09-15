# Transaction Monitoring System

## Overview
Fraud monitor is a project that shows my skills as
a data analyst, it runs a data base that has transactions
in it and creates a table for transactions marked as 
alerts for fraud by filtering with four rules.

## Tech Stack
- Java (Maven, JDBC)
- PostgreSQL
- PaySim dataset (Kaggle) - synthetic financial transaction data
- Power BI / Tableau (dashboard)

## Database Schema
The data base have three tables, accounts primary-key as account id,
transactions primary-key as transaction id, and alert primary-key as
alert id, alert has foreign-key as transaction id

## Fraud Detection Rules
| Rule | Precision | Independent Recall |
|------|-----------|---------------------|
| near_transfer_limit | 87.6% | 4.2% |
| above_avg_cash_out | 67.7% | 25.5% |
| drained_account_transfer | 69.4% | 47.9% |
| drained_account_cashout | 34.6% | 49.6% |

Rules are implemented twice - once as SQL queries (fraud_rules.sql,
run manually) and once as Java logic (RuleEngine.java),
validated to produce identical results.


## How to Run
1. Create a PostgreSQL database named `fraud_monitor`
2. Run `schema.sql` to create the tables
3. Import the PaySim dataset and build the working subset (see fraud_rules.sql)
4. Set an environment variable `DB_PASSWORD` with your PostgreSQL password
5. Run `Main.java` - this connects to the DB, applies all rules, and writes
   results into the `alerts` table


## Results
- Combined recall: 99.4% (8,161 out of 8,213 known fraud cases caught by
  at least one rule)
- Combined precision: ~45.3% (18,015 total flagged transactions)
- Key takeaway: single-condition rules cannot achieve both high precision
  and high recall on this dataset; a tiered confidence approach (using
  multiple rules with different precision/recall trade-offs) was used
  instead of chasing one "perfect" rule.