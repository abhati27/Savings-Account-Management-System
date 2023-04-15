import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SavingsAccount {
    private double balance;
    private final Lock lock;
    private final Condition sufficientFundsCondition;
    private final Condition ordinaryWithdrawalCondition;
    private int preferredWithdrawalCount;

    public SavingsAccount() {
        this.balance = 0;
        this.lock = new ReentrantLock();
        this.sufficientFundsCondition = lock.newCondition();
        this.ordinaryWithdrawalCondition = lock.newCondition();
        this.preferredWithdrawalCount = 0;
    }

    public void deposit(double amount) {
        lock.lock();
        try {
            if (amount < 0) {
                throw new IllegalArgumentException("Amount must be non-negative.");
            }
            balance += amount;
            sufficientFundsCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double amount) throws InterruptedException, IllegalArgumentException {
        lock.lock();
        try {
            if (amount < 0) {
                throw new IllegalArgumentException("Amount must be non-negative.");
            }
            while (preferredWithdrawalCount > 0) {
                ordinaryWithdrawalCondition.await();
            }
            if (balance < amount) {
                throw new IllegalArgumentException("Insufficient funds. Withdrawal unsuccessful.");
            }
            balance -= amount;
        } finally {
            lock.unlock();
        }
    }

    public void withdrawPreferred(double amount) throws InterruptedException, IllegalArgumentException {
        lock.lock();
        try {
            if (amount < 0) {
                throw new IllegalArgumentException("Amount must be non-negative.");
            }
            preferredWithdrawalCount++;
            while (balance < amount) {
                sufficientFundsCondition.await();
            }
            balance -= amount;
            preferredWithdrawalCount--;
            ordinaryWithdrawalCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public static void transfer(SavingsAccount fromAccount, SavingsAccount toAccount, double amount) throws InterruptedException, IllegalArgumentException {
        fromAccount.lock.lock();
        toAccount.lock.lock();
        try {
            if (amount < 0) {
                throw new IllegalArgumentException("Amount must be non-negative.");
            }
            if (fromAccount.getBalance() < amount) {
                throw new IllegalArgumentException("Insufficient funds in the source account. Transfer unsuccessful.");
            }
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
        } finally {
            toAccount.lock.unlock();
            fromAccount.lock.unlock();
        }
    }
}
