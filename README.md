# Savings-Account-Management-System
A concurrent Savings Account Management System that demonstrates deposit, withdraw, and transfer operations on savings accounts, as well as handling concurrent operations without duplicating it..

## Features
- Deposit and withdraw funds from a savings account
- Transfer funds between two savings accounts
- Preferred withdrawals with priority over ordinary withdrawals
- Concurrent access to the savings account with proper synchronization
- Test suite to validate the functionality and concurrency handling

## Prerequisites
Java Development Kit (JDK) 8 or later

## Usage
1. First clone the repo
2. Open a terminal and navigate to the project directory.
3. Compile the Java files:
  ```Terminal
javac SavingsAccount.java SavingsAccountDemo.java SavingsAccountTest.java
```
5. Run the SavingsAccountDemo to interact with the Savings Account Management System:

  ```Terminal
  java SavingsAccountDemo
  ```
## Testing

To run the test suite, execute the following command in the terminal:

```Terminal
java SavingsAccountTest
```
This will run a series of tests, including concurrency tests, to ensure the proper functioning of the Savings Account Management System.


