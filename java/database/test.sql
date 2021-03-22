SELECT SUM(balance) FROM accounts WHERE user_id = 1001 GROUP BY user_id;

SELECT account_id, SUM(balance) FROM accounts WHERE user_id = 1001 GROUP BY user_id, account_id

BEGIN TRANSACTION;

UPDATE accounts SET balance = 500.00 WHERE user_id = 1001;


SELECT t.transfer_id, t.account_from, t.account_to, t.amount, s.user_id AS sender, users.username AS sender_username, r.user_id AS receiver, users.username AS receiver_username
FROM users --we're trying to pull back two account ids, two user ids, two usernames, can we alias user as well
JOIN accounts s ON s.user_id = users.user_id
JOIN accounts r ON r.user_id = users.user_id
JOIN transfers AS t ON t.account_to = r.account_id
JOIN transfers AS f ON f.account_from = s.account_id
WHERE t.account_from = 2001 OR t.account_to = 2001 OR f.account_from = 2001 OR f.account_to = 2001;

--get who received transfer username

SELECT transfer_id, transfer_type_id, account_from, account_to, amount, users.user_id AS receiver_id, users.username AS receiver_username
FROM users
JOIN accounts ON accounts.user_id = users.user_id
JOIN transfers ON account_to = accounts.account_id
WHERE account_from = 2001;

--get sent from transfer username

SELECT transfer_id, transfer_type_id, account_from, account_to, amount, users.user_id AS sender_id, users.username AS sender_username
FROM users
JOIN accounts ON accounts.user_id = users.user_id
JOIN transfers ON account_from = accounts.account_id
WHERE account_to = 2001;












SELECT t.transfer_id, tt.transfer_type_desc, ts.transfer_status_desc, t.amount, acctFrom.account_id AS fromAcct, acctFrom.user_id AS fromUser, acctTo.balance AS toBal, acctTo.account_id AS toAcct, acctTo.user_id AS toUser
FROM transfers t
JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id
JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id
JOIN accounts acctFrom ON account_from = acctFrom.account_id
JOIN accounts acctTo ON account_to = acctTo.account_id
WHERE (account_from IN (SELECT account_id FROM accounts WHERE user_id = 1001) OR account_to IN (SELECT account_id FROM accounts WHERE user_id = 1001));



SELECT t.transfer_id, tt.transfer_type_desc, ts.transfer_status_desc, t.amount, acctFrom.account_id AS from_Acct, acctFrom.user_id AS from_User, acctTo.balance AS current_To_Bal, acctTo.account_id AS to_Acct, acctTo.user_id AS to_User, initcap(userFrom.username) AS from_Username, initcap(userTo.username) AS to_Username
FROM transfers t
JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id
JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id
JOIN accounts acctFrom ON account_from = acctFrom.account_id
JOIN accounts acctTo ON account_to = acctTo.account_id
JOIN users userFrom ON userFrom.user_id = acctFrom.user_id
JOIN users userTo ON userTo.user_id = acctTo.user_id
WHERE (account_from IN (SELECT account_id FROM accounts WHERE user_id = 1001) OR account_to IN (SELECT account_id FROM accounts WHERE user_id = 1001));


SELECT transfer_id, transfer_types.transfer_type_desc, transfer_statuses.transfer_status_desc, account_from,
                account_to, amount FROM transfers
                JOIN transfer_statuses ON + transfer_statuses.transfer_status_id = transfers.transfer_status_id
                JOIN transfer_types ON transfer_types.transfer_type_id = transfers.transfer_type_id
                WHERE transfer_id = 3001;
                
                
SELECT transfers.transfer_id, ts.transfer_status_desc
FROM transfer_statuses ts
JOIN transfers ON transfers.transfer_status_id = ts.transfer_status_id
WHERE transfer_id = 3001;


SELECT t.transfer_id, tt.transfer_type_desc, ts.transfer_status_desc, t.amount,
                acctFrom.account_id AS from_Acct, acctFrom.user_id AS from_User, acctTo.balance AS current_To_Bal,
                acctTo.account_id AS to_Acct, acctTo.user_id AS to_User, initcap(userFrom.username) AS from_Username,
                initcap(userTo.username) AS to_Username
                FROM transfers t
                JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id
                JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id
                JOIN accounts acctFrom ON account_from = acctFrom.account_id
                JOIN accounts acctTo ON account_to = acctTo.account_id
                JOIN users userFrom ON userFrom.user_id = acctFrom.user_id
                JOIN users userTo ON userTo.user_id = acctTo.user_id 
                WHERE transfer_id = 3001 AND (account_from IN (SELECT account_id FROM accounts WHERE user_id = 1001) OR account_to IN
                (SELECT account_id FROM accounts WHERE user_id = 1001))





SELECT transfer_id, transfer_type_id, account_from, account_to, amount, users.user_id, users.username
FROM users
JOIN accounts ON accounts.user_id = users.user_id
JOIN transfers ON account_to = account_id
WHERE account_to = 2001 OR account_from = 2001;


--latest attempt

SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount
FROM transfers
WHERE account_to = 2001 OR account_from = 2001;


SELECT users.user_id, users.username FROM accounts 
                JOIN users ON users.user_id = accounts.user_id WHERE account_id =going 2001
                
                
                




SELECT transfer_id, transfer_type_id, account_from, account_to, amount, users.user_id, users.username
FROM users
JOIN accounts ON accounts.user_id = users.user_id
JOIN transfers AS t ON t.account_to = accounts.account_id
JOIN transfers AS f ON f.account_from = s.account_id
WHERE account_to = 2001 OR account_from = 2001;




ROLLBACK;