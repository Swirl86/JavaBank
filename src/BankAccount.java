import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class BankAccount implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3029036123415540952L;
    private double balance;
    private List<Transaction> transactions;

    public BankAccount() {
        super();
        this.balance = 0;
        this.transactions = new ArrayList<Transaction>();
    }

    public void withdraw(double amount) throws IllegalTransactionException {
        this.balance = this.balance - amount;
        this.transactions.add(new Transaction(this.getTimeStamp(), (amount * -1)));
    }

    public void deposit(double amount) throws IllegalTransactionException {
        this.balance = this.balance + amount;
        this.transactions.add(new Transaction(this.getTimeStamp(), amount));
    }

    public abstract double addInterest() throws IllegalTransactionException;

    public String getTimeStamp() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String toReturn = dtf.format(now);
        return toReturn;
    }

    public double getBalance() {
        return this.balance;
    }

    public String toStringSp() {

        String toReturn = "";

        for (Transaction trans : transactions) {
            toReturn += trans.toStringSp();
        }

        return toReturn;
    }

}
