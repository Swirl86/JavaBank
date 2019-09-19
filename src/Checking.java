
public class Checking extends BankAccount {

    /**
     * 
     */
    private static final long serialVersionUID = 5429254052226396071L;
    public static final double maxWithdraw = 5000.5;
    public static final double checkingInterest = 5.8;

    public Checking() {

    }

    public double addInterest() throws IllegalTransactionException {
        double intrestAdd = (super.getBalance() * checkingInterest) / 100;
        return intrestAdd;
    }

    public void withdraw(double amount) throws IllegalTransactionException {
        if (super.getBalance() < amount) {
            throw new IllegalTransactionException("The withdraw sum is to high, you are ower the balance limit.");
        }
        if (amount <= 0) {
            throw new IllegalTransactionException("ERROR! " + amount + " is not a legal withdraw.");
        }
        if (amount > maxWithdraw) {
            throw new IllegalTransactionException("The withdraw sum is to high, you are ower the max withdraw limit.");
        }
        amount += this.addInterest();
        super.withdraw(amount);
    }

    public void deposit(double amount) throws IllegalTransactionException {
        if (amount <= 0) {
            throw new IllegalTransactionException("ERROR! " + amount + " is not a legal deposit.");
        }
        super.deposit(amount);
    }

}
