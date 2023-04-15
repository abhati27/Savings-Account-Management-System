import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SavingsAccountTest {

    public static void main(String[] args) throws InterruptedException {
        testDepositAndGetBalance();
        testWithdraw();
        testWithdrawPreferred();
        testTransfer();
        testConcurrency();
        testConcurrencyPreferredWithdrawal();
        testConcurrencyPreferredWithdrawalFailure();
        testNegativeAmount();

        System.out.println("All tests passed.");
    }

    public static void testDepositAndGetBalance() {
        SavingsAccount account = new SavingsAccount();
        account.deposit(100);
        assert account.getBalance() == 100 : "testDepositAndGetBalance failed";
    }

    public static void testWithdraw() throws InterruptedException {
        SavingsAccount account = new SavingsAccount();
        account.deposit(100);
        account.withdraw(50);
        assert account.getBalance() == 50 : "testWithdraw failed";
    }

    public static void testWithdrawPreferred() throws InterruptedException {
        SavingsAccount account = new SavingsAccount();
        account.deposit(100);
        account.withdrawPreferred(50);
        assert account.getBalance() == 50 : "testWithdrawPreferred failed";
    }

    public static void testTransfer() throws InterruptedException {
        SavingsAccount fromAccount = new SavingsAccount();
        SavingsAccount toAccount = new SavingsAccount();
        fromAccount.deposit(100);
        SavingsAccount.transfer(fromAccount, toAccount, 50);
        assert fromAccount.getBalance() == 50 : "testTransfer failed for fromAccount";
        assert toAccount.getBalance() == 50 : "testTransfer failed for toAccount";
    }

    public static void testConcurrency() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        SavingsAccount account = new SavingsAccount();

        for (int i = 0; i < numThreads; i++) {
            int finalI = i;
            executor.submit(() -> {
                try {
                    if (finalI % 2 == 0) {
                        account.deposit(100);
                    } else {
                        account.withdraw(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        assert account.getBalance() == 500 : "testConcurrency failed";
    }

    public static void testNegativeAmount() {
        SavingsAccount account = new SavingsAccount();
        account.deposit(100);

        try {
            account.deposit(-10);
            throw new AssertionError("testNegativeAmount failed for deposit");
        } catch (IllegalArgumentException e) {
        }

        try {
            account.withdraw(-10);
            throw new AssertionError("testNegativeAmount failed for withdraw");
        } catch (IllegalArgumentException e) {
        } catch (InterruptedException e) {
            throw new AssertionError("testNegativeAmount failed for withdraw due to InterruptedException", e);
        }

        try {
            account.withdrawPreferred(-10);
            throw new AssertionError("testNegativeAmount failed for withdrawPreferred");
        } catch (IllegalArgumentException e) {
        } catch (InterruptedException e) {
            throw new AssertionError("testNegativeAmount failed for withdrawPreferred due to InterruptedException", e);
        }

        SavingsAccount toAccount = new SavingsAccount();
        try {
            SavingsAccount.transfer(account, toAccount, -10);
            throw new AssertionError("testNegativeAmount failed for transfer");
        } catch (IllegalArgumentException e) {
        } catch (InterruptedException e) {
            throw new AssertionError("testNegativeAmount failed for transfer due to InterruptedException", e);
        }
    }
    public static void testConcurrencyPreferredWithdrawal() throws InterruptedException {
        int numThreads = 4;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        SavingsAccount account = new SavingsAccount();

        account.deposit(200);

        executor.submit(() -> {
            try {
                account.withdrawPreferred(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        Thread.sleep(500);

        // Ordinary withdrawals
        for (int i = 0; i < numThreads - 1; i++) {
            executor.submit(() -> {
                try {
                    account.withdraw(50);
                    throw new AssertionError("testConcurrencyPreferredWithdrawal failed: ordinary withdrawal not blocked");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        Thread.sleep(500);
        account.deposit(150);

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        assert account.getBalance() == 200 : "testConcurrencyPreferredWithdrawal failed";
    }
    public static void testConcurrencyPreferredWithdrawalFailure() throws InterruptedException {
        int numThreads = 4;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        SavingsAccount account = new SavingsAccount();
        AtomicBoolean preferredWithdrawalStarted = new AtomicBoolean(false);
        AtomicBoolean preferredWithdrawalCompleted = new AtomicBoolean(false);

        account.deposit(200);

        executor.submit(() -> {
            try {
                preferredWithdrawalStarted.set(true);
                account.withdrawPreferred(100);
                preferredWithdrawalCompleted.set(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        for (int i = 0; i < numThreads - 1; i++) {
            executor.submit(() -> {
                try {
                    if (preferredWithdrawalStarted.get() && !preferredWithdrawalCompleted.get()) {
                        account.withdraw(50);
                        throw new AssertionError("testConcurrencyPreferredWithdrawalFailure failed: ordinary withdrawal not blocked");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        Thread.sleep(500);
        account.deposit(150);

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        assert account.getBalance() == 200 : "testConcurrencyPreferredWithdrawalFailure failed";
    }


}
