import java.io.Serializable;

public class Customer implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6988063583328076083L;

    private int customerNumber;
    private int pin;
    private Checking checkingAcount;
    private Savings savingsAcount;

    public Customer(int customerNumber, int pin) {
        this.customerNumber = customerNumber;
        this.pin = pin;
        this.checkingAcount = new Checking();
        this.savingsAcount = new Savings();
    }

    public int getCustomerNumber() {
        return this.customerNumber;
    }

    public int getCustumerPin() {
        return this.pin;
    }

    public boolean matchCustomer(int customerNr, int thePin) {
        return this.customerNumber == customerNr && this.pin == thePin;
    }

    public BankAccount getCheckingAcount() {
        return this.checkingAcount;
    }

    public BankAccount getSavingsAcount() {
        return this.savingsAcount;
    }

    public double getCheckingBalance() {
        return this.checkingAcount.getBalance();
    }

    public double getSavingsBalance() {
        return this.savingsAcount.getBalance();
    }

    public double getBalance() {
        return this.checkingAcount.getBalance() + this.savingsAcount.getBalance();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + customerNumber;
        result = prime * result + pin;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Customer other = (Customer) obj;
        if (customerNumber != other.customerNumber)
            return false;
        if (pin != other.pin)
            return false;
        return true;
    }

    public String toStringS() {
        String balance = String.format("%.2f", this.getBalance());

        return "<html>Customer number: " + this.customerNumber + "<br>" + "\nTotal Balance: " + balance
                + " Kr.<br>\n\n<br>Checking's Account:<br>\n" + this.checkingAcount.toStringSp() + "<br>"
                + "\nSaving's Account:<br>" + this.savingsAcount.toStringSp() + "<br>\n </span></html>";
    }
}
