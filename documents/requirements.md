FR:
-user must be able to set a treshold amt
-default: 50,000
-minimum alert: 1000 max: 10,000,000
-cannot set tresholds for other users

Modify:
-user must be able to modify treshold
-validation(auth)
-applies to future transactions ONLY

AC:
-User can GET their alert preferences - alert yes/no
-User can GET their alert preference mode (if alert: yes) : SMS, Email (AND, OR)
-Default is false for SMS, Email
-User can PUT to change boolean on SMS, Email to true
-User can POST their threshold amt
-User can PUT their treshold amt
-DEBIT transactions trigger alert
-CREDIT transactions do not trigger alert
-Email should not block transaction 
- Transaction completes even if SMS/Email fails 
-  Proper authorization (can only access own alerts) 