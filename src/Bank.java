import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bank implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8824561841690623047L;
    private ArrayList<Customer> customerList;

    public Bank() {
        this.customerList = new ArrayList<Customer>();
    }

    public Customer addCustomer(int customerNumber, int pin) throws IllegalTransactionException {
        Customer customer = new Customer(customerNumber, pin);
        if (!this.findMatchingCustomer(customerNumber, pin)) {
            this.customerList.add(customer);
        } else {
            throw new IllegalTransactionException("ERROR! That account already exist's");
        }
        System.out.println("Added customer " + customer.getCustomerNumber());
        return customer;
    }

    public Boolean findMatchingCustomer(int customerNumber, int pin) {
        Boolean isFound = false;
        Customer customer = new Customer(customerNumber, pin);
        if (this.customerList.contains(customer)) {
            isFound = true;
        }
        return isFound;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customerList == null) ? 0 : customerList.hashCode());
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
        Bank other = (Bank) obj;
        if (customerList == null) {
            if (other.customerList != null)
                return false;
        } else if (!customerList.equals(other.customerList))
            return false;
        return true;
    }

    public Boolean matchPin(int pin) {
        Boolean isFound = false;
        if (this.customerList.equals(pin)) {
            isFound = true;
        }
        return isFound;
    }

    public Customer getCustomer(int currentCustomerNr, int pin) {

        Customer customer = new Customer(currentCustomerNr, pin);
        System.out.println("Looking for customer with bank number " + currentCustomerNr);
        for (Customer persone : this.customerList) {

            if (!persone.matchCustomer(currentCustomerNr, pin)) {

            	customer = null;
            }
        }
        return customer;
    }

    @SuppressWarnings({ "unchecked", "resource" })
    public boolean checkCustomerBankListAtStart(int number, int pin) {

        boolean isFound = false;
        FileInputStream saveFile = null;
        ObjectInputStream save = null;
        Map<Integer, Integer> customerMap;
        
        try {
            File file = new File("customerListOfBank.txt");
            saveFile = new FileInputStream(file);
            if (saveFile.available() == 0) {
                System.out.println("Nothing on the file");
                return false;
            }
            save = new ObjectInputStream(saveFile);
            customerMap = (Map<Integer, Integer>) save.readObject();
            save.close();

            Integer maybePin = customerMap.get(number);

            if (maybePin != null) {
                // Found key
                System.out.println("Found match number.");
                if (maybePin == pin) {
                    // Found matching pin
                    isFound = true;
                    System.out.println("Found match pin.");

                }
            } else {
                // could not find a match.
                System.out.println("No match found. . .");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return isFound;
    }

    public void saveCustomersForStart() {
        System.out.println("Saving customers list in Bank");
        FileOutputStream saveFile = null;
        ObjectOutputStream save = null;

        File file = new File("customerListOfBank.txt");

        try {
            saveFile = new FileOutputStream(file);
            save = new ObjectOutputStream(saveFile);
            int pin = 0;
            int customer = 0;

            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (int i = 0; i < customerList.size(); i++) {
            	customer = this.customerList.get(i).getCustomerNumber();
                pin = this.customerList.get(i).getCustumerPin();
                map.put(customer, pin);

            }
            save.writeObject(map);
            save.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
