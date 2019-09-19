
public class Savings extends BankAccount {

    /**
     * 
     */
    private static final long serialVersionUID = -7082019749124638190L;
    public static final double minDeposit = 500.0;
    public static final double savingInterestRate = 10.7;
    // Generous bank interest

    public Savings() {
    }

    public double addInterest() throws IllegalTransactionException {
        double intrestAdd = (super.getBalance() * savingInterestRate) / 100;
        return intrestAdd;
    }

    public void withdraw(double amount) throws IllegalTransactionException {
        if (super.getBalance() < amount) {
            throw new IllegalTransactionException("The withdraw sum is to high, you are ower the balance limit.");
        }
        if (amount <= 0) {
            throw new IllegalTransactionException("ERROR! " + amount + " is not a legal withdraw.");
        }
        amount += this.addInterest();
        super.withdraw(amount);
    }

    public void deposit(double amount) throws IllegalTransactionException {
        if (amount <= 0) {
            throw new IllegalTransactionException("ERROR! " + amount + " is not a legal deposit.");
        }
        if (amount < minDeposit) {
            throw new IllegalTransactionException("ERROR! " + amount + " is below minimum deposit.");
        }
        super.deposit(amount);
    }

}
