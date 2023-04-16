# Savings-Account-Management-System
A savings account management system that can handle several transactions at once without repeating them, including deposit, withdrawal, and transfer procedures.

## Features
- Depositing and withdrawing money
- Transferring money across savings accounts
- Preferred withdrawals get priority over standard withdrawals
- Test suite to verify functionality and concurrency management
- Concurrent access to the savings account with suitable synchronization

## Prerequisites
Java Development Kit (JDK) 8 or later

## Usage
1. Clone the repo first
2. Launch a terminal and find the project directory there.
Compile the Java source code:
  ```Terminal
javac SavingsAccount.java SavingsAccountDemo.java SavingsAccountTest.java
```
3. To interact with the Savings Account Management System, launch the SavingsAccountDemo:
  ```Terminal
  java SavingsAccountDemo
  ```
## Testing

Run the following command in the terminal to run the test suite:

```Terminal
java SavingsAccountTest
```
To ensure the proper functioning of the Savings Account Management System, this will perform a number of tests, including concurrency tests.


