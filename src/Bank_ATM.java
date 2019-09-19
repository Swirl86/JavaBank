public class Bank_ATM {

    private Bank bankObj;
    private int currentCustomerNr;

    public Bank_ATM(Bank aObj) {
        this.bankObj = aObj;
        this.currentCustomerNr = 0;
    }

    public void setCustomerNumber(int customerNumber) {
        this.currentCustomerNr = customerNumber;
    }

    public boolean confirmCustomerExists(int pin) {
        boolean isFound = false;
        if (this.bankObj.findMatchingCustomer(this.currentCustomerNr, pin)) {
            isFound = true;
        }
        return isFound;

    }

    public int getCurrentCustNumber() {
        return this.currentCustomerNr;
    }
}