import java.util.Scanner;

public class SavingsAccountDemo {
    public static void main(String[] args) {
        SavingsAccount account1 = new SavingsAccount();
        SavingsAccount account2 = new SavingsAccount();
        Scanner scanner = new Scanner(System.in);

        SavingsAccount activeAccount = null;

        while (true) {
            if (activeAccount == null) {
                System.out.println("Choose an account to work with: (1) Account 1, (2) Account 2");
                int accountNumber = scanner.nextInt();
                activeAccount = (accountNumber == 1) ? account1 : account2;
            }

            System.out.println("Choose an action: (1) Deposit, (2) Ordinary Withdraw, (3) Preferred Withdraw, (4) Transfer, (5) Get Balance, (6) Switch Account, (7) Quit");
            int action = scanner.nextInt();

            if (action == 1) {
                System.out.println("Enter the amount to deposit:");
                double depositAmount = scanner.nextDouble();
                activeAccount.deposit(depositAmount);
                System.out.printf("Deposited: %.2f%n", depositAmount);
            } else if (action == 2) {
                System.out.println("Enter the amount to withdraw (Ordinary):");
                double withdrawAmount = scanner.nextDouble();
                try {
                    activeAccount.withdraw(withdrawAmount);
                    System.out.printf("Withdrew (Ordinary): %.2f%n", withdrawAmount);
                } catch (InterruptedException e) {
                    System.out.println("Withdrawal interrupted.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } else if (action == 3) {
                System.out.println("Enter the amount to withdraw (Preferred):");
                double withdrawAmount = scanner.nextDouble();
                try {
                    activeAccount.withdrawPreferred(withdrawAmount);
                    System.out.printf("Withdrew (Preferred): %.2f%n", withdrawAmount);
                } catch (InterruptedException e) {
                    System.out.println("Withdrawal interrupted.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } else if (action == 4) {
                System.out.println("Choose the target account: (1) Account 1, (2) Account 2");
                int targetAccountNumber = scanner.nextInt();
                if (activeAccount == account1 && targetAccountNumber == 1 || activeAccount == account2 && targetAccountNumber == 2) {
                    System.out.println("Cannot transfer funds to the same account. Choose a different target account.");
                    continue;
                }
                SavingsAccount targetAccount = (targetAccountNumber == 1) ? account1 : account2;

                System.out.println("Enter the amount to transfer:");
                double transferAmount = scanner.nextDouble();
                try {
                    SavingsAccount.transfer(activeAccount, targetAccount, transferAmount);
                    System.out.printf("Transferred: %.2f%n", transferAmount);
                } catch (InterruptedException e) {
                    System.out.println("Transfer interrupted.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                } else if (action == 5) {
                double balance = activeAccount.getBalance();
                System.out.printf("Current balance: %.2f%n", balance);
            } else if (action == 6) {
                activeAccount = null;
            } else if (action == 7) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid action. Please try again.");
            }
        }

        scanner.close();
    }
}
