import java.io.Serializable;

public class Transaction implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4199892859657065443L;
    private String timeStamp;
    private double amount;

    public Transaction(String currentTimeMillis, double amount) {
        this.timeStamp = currentTimeMillis;
        this.amount = amount;
    }

    public String toStringSp() {

        return "TimeStamp: " + this.timeStamp + "<br>\nAmount: " + String.format("%.2f", this.amount) + " Kr<br>";

        // return "<html>TimeStamp: " + this.timeStamp + "<br>\nAmount: " +
        // this.amount + " Kr<br>\n </span></html>";
    }

}
