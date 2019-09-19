
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ATM_GUI extends JFrame {
    public class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            String buttonText = event.getActionCommand();

            if (buttonText.equals("Checkings")) {

                try {
                    setCurrentAccount(currentCustomer.getCheckingAcount());
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            if (buttonText.equals("Savings")) {
                try {
                    setCurrentAccount(currentCustomer.getSavingsAcount());
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            if (buttonText.equals("Deposit")) {
                try {
                    addADeposit();
                } catch (IllegalTransactionException e) {
                    e.getMessage();
                }
            }
            if (buttonText.equals("Withdraw")) {
                try {
                    addWithdraw();
                } catch (IllegalTransactionException e) {
                    e.getMessage();
                }
            }
            if (buttonText.equals("History")) {
                readFromFile();
            }
            if (buttonText.equals("Save trans")) {
                saveToFile();
            }
            if (buttonText.equals("Total Balance")) {
                getTotalBalance();
            }
            if (buttonText.equals("Loggout")) {
                try {
                    resetWhenLoggOut();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }

    private JTextField txtAmount;
    private static JList<String> listTransactions;
    private static JScrollPane jScrollPane;
    private Container contentPane;
    private static Bank theBank;
    private static Bank_ATM myATM;
    private static Customer currentCustomer;
    private static BankAccount currentAccount;

    public ATM_GUI() {

        initiateInstanceVariables();
    }

    public void saveToFile() {
        List<String> items = new ArrayList<String>();
        items.add("<html>Saving your Transaction History.<br>Status:<br></span></html>");
        String infoStr = "";
        try {
            String fileName = Integer.toString(currentCustomer.getCustomerNumber());
            FileOutputStream saveFile = new FileOutputStream(fileName + ".txt");
            ObjectOutputStream save = new ObjectOutputStream(saveFile);
            save.writeObject(currentCustomer);
            save.flush();
            infoStr = "The Transactions were successfully saved to our data bank.";
            save.close();

        } catch (FileNotFoundException e) {
            infoStr = "The Transaction's are NOT saved, ERROR. ";
            e.printStackTrace();
        } catch (IOException e) {
            infoStr = "The Transaction's is NOT Saved, ERROR.";
            e.printStackTrace();
        }
        items.add(infoStr);
        ATM_GUI.listTransactions.setListData(items.toArray(new String[0]));
    }

    public static void readFromFile() {
        List<String> items = new ArrayList<String>();
        items.add("<html>Loading your Transaction History.<br>Status: </span></html>");
        FileInputStream saveFile = null;
        ObjectInputStream saveObject = null;
        String custNrFileName = Integer.toString(currentCustomer.getCustomerNumber());
        try {
            File file = new File(custNrFileName + ".txt");
            if (!file.exists()) {
                System.out.println("No file exists yet");
                items.add("<html>There are no Transaction history To Import From<br>"
                        + "The Banks Database.<br>Please save your Transactions.<br>\n </span></html>");
                ATM_GUI.listTransactions.setListData(items.toArray(new String[0]));
                return;
            }
            saveFile = new FileInputStream(file);
            saveObject = new ObjectInputStream(saveFile);

            currentCustomer = (Customer) saveObject.readObject();
            items.add("The Transactions were successfully loaded.");
            items.add(currentCustomer.toStringS());
            ATM_GUI.listTransactions.setListData(items.toArray(new String[0]));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (saveObject != null) {
                	saveObject.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void welcomeMessage() {
        List<String> msg = new ArrayList<String>();
        msg.add("<html>Welcome customer number " + ATM_GUI.myATM.getCurrentCustNumber()
                + " To SwIrl's remote ATM-Bank!<br>\n </span></html>");
        ATM_GUI.listTransactions.setListData(msg.toArray(new String[0]));
    }

    public void resetWhenLoggOut() {
        this.saveToFile();
        theBank.saveCustomersForStart();
        System.out.println("Ready to logg-in next customer!");
        boolean loggedIn = false;
        while (!loggedIn) {
            loggedIn = tryLoggin();
        }
    }

    /**
     * Setting current account based on which radio button is pressed. *
     * 
     * @param account
     * @throws Exception
     */
    public void setCurrentAccount(BankAccount account) throws Exception {

        if (myATM == null) {
            throw new Exception("There are no customer logged in atm, doing maintanance!");
        }
        List<String> msg = new ArrayList<String>();
        if (account == currentCustomer.getCheckingAcount()) {
            System.out.println("Setting currentAccount to checkings!");
            currentAccount = currentCustomer.getCheckingAcount();
            msg.add("Current account chosen is: Checking");
            ATM_GUI.listTransactions.setListData(msg.toArray(new String[0]));
        } else {
            System.out.println("Setting currentAccount to savings!");
            currentAccount = currentCustomer.getSavingsAcount();
            msg.add("Current account chosen is: Saving");
            ATM_GUI.listTransactions.setListData(msg.toArray(new String[0]));
        }
    }

    public void getTotalBalance() {

        List<String> msg = new ArrayList<String>();
        msg.add("<html>Total balance is: " + String.format("%.2f", currentCustomer.getBalance())
                + " Kr.<br>Checking's: " + String.format("%.2f", currentCustomer.getCheckingBalance())
                + " Kr.<br>Saving's: " + String.format("%.2f", currentCustomer.getSavingsBalance())
                + " Kr.<br>\n </span></html>");
        ATM_GUI.listTransactions.setListData(msg.toArray(new String[0]));
        this.clearTextField();
    }

    public void addADeposit() throws IllegalTransactionException {

        double value = getValueFromTextInPut();
        this.clearTextField();
        List<String> msg = new ArrayList<String>();
        if (value <= 0.0) {
        	msg.add("<html>The amount " + value
                    + " is not a legal transaction!<br>Check your balance if needed.<br>\n </span></html>");
            ATM_GUI.listTransactions.setListData(msg.toArray(new String[0]));
        } else if (ATM_GUI.currentAccount == currentCustomer.getSavingsAcount() && value < Savings.minDeposit) {
        	msg.add("<html>The amount " + value + " is not a legal transaction!<br> The minimum deposit in "
                    + "a saving's account is " + Savings.minDeposit + " Kr.<br>\n </span></html>");
            ATM_GUI.listTransactions.setListData(msg.toArray(new String[0]));
        } else {
            ATM_GUI.currentAccount.deposit(value);
            System.out.println("\nDeposit: " + value);
            msg.add("Deposit: " + value + " Kr.");
            ATM_GUI.listTransactions.setListData(msg.toArray(new String[0]));
        }
    }

    public void addWithdraw() throws IllegalTransactionException {

        double value = getValueFromTextInPut();
        this.clearTextField();

        List<String> msg = new ArrayList<String>();
        if (value <= 0.0 || value > ATM_GUI.currentAccount.getBalance()) {
        	msg.add("<html>The amount " + value
                    + " is not a legal transaction!<br>Check your balance if needed.<br>\n </span></html>");
            ATM_GUI.listTransactions.setListData(msg.toArray(new String[0]));
        } else if (ATM_GUI.currentAccount == currentCustomer.getCheckingAcount() && value > Checking.maxWithdraw) {
        	msg.add("<html>The amount " + value + " is not a legal transaction!<br> The maximum withdraw in "
                    + "a checking's account i " + Checking.maxWithdraw + " Kr.<br>\n </span></html>");
            ATM_GUI.listTransactions.setListData(msg.toArray(new String[0]));
        } else {
            ATM_GUI.currentAccount.withdraw(value);
            System.out.println("\nWithdraw: " + value);
            msg.add("Withdrew: " + value + " Kr.");
            ATM_GUI.listTransactions.setListData(msg.toArray(new String[0]));
        }
    }

    /**
     * Get text input and convert to double.
     * 
     * @return
     */
    public double getValueFromTextInPut() {
        String valueAsString = txtAmount.getText();
        if (valueAsString == null || valueAsString.isEmpty()) {
            return 0;
        }
        return Double.parseDouble(valueAsString);
    }

    /**
     * Clear text input window after button press for easy input, no delete old
     * stuff.
     */
    public void clearTextField() {
        this.txtAmount.setText("");
    }

    private void initiateInstanceVariables() {

        ATM_GUI.theBank = new Bank();
        ATM_GUI.myATM = new Bank_ATM(theBank);

        this.txtAmount = new JTextField();
        ATM_GUI.listTransactions = new JList<>();
        ATM_GUI.jScrollPane = new JScrollPane(listTransactions);
        this.contentPane = this.getContentPane();
    }

    /**
     * Start building GUI frame
     */
    public void configureFrame() {
        this.contentPane.setLayout(new GridLayout(2, 1));
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2) - 400;
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2) - 200;
        this.setLocation(x, y);
        this.setResizable(false);

        this.setSize(800, 600);

        JPanel textPanel = createTextPanel();
        JPanel splitPanel = buildSplitPanel();

        contentPane.add(textPanel);
        contentPane.add(splitPanel);
    }

    private JPanel createTextPanel() {

        JPanel textPanel = new JPanel();
        JPanel radioButton = addRadioButtons();

        textPanel.setLayout(new GridLayout(1, 1));
        ATM_GUI.listTransactions.setBorder(BorderFactory.createTitledBorder("Transactions."));
        textPanel.add(ATM_GUI.jScrollPane);
        textPanel.add(radioButton);

        return textPanel;
    }

    /**
     * Radio buttons to easily chose what account to add deposit and withdraw
     * in. And Some "ATM History"
     * 
     * @return
     */
    private JPanel addRadioButtons() {

        ButtonListener listener = new ButtonListener();

        JPanel buttonPanel = new JPanel();
        JTextArea infoText = new JTextArea();

        ButtonGroup group = new ButtonGroup();
        JRadioButton checkings = new JRadioButton("Checkings");
        JRadioButton savings = new JRadioButton("Savings");

        checkings.setLocation(60, 35);
        checkings.setSelected(true);
        checkings.addActionListener(listener);

        savings.setLocation(65, 50);
        savings.addActionListener(listener);

        group.add(checkings);
        group.add(savings);
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Chose Account:"));
        buttonPanel.add(checkings, savings);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        buttonPanel.add(checkings);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(savings);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        infoText.setSize(220, 50);
        infoText.setLocation(20, 30);
        infoText.setBorder(null);
        infoText.setText("Click on the button next to the account that you want to use.\n\n"
                + "We hope you will be satesfied by our ATM's service options.\n"
                + "We have been in business since 1801, SwIrL's Bank.\n"
                + "The first banker and owner was Åke Uno and his daughter Susanne.\n"
                + "Thay made the first ATM 1968. It's the first ATM in the world!\n"
                + "So if you want quality you come to the right ATM!\n"
                + "If you got anny complaintes please dont hesitate to file a\ncomplaine at our 'Emo Desk' by the corner of the streets.\n"
                + "Thank you for beeing our customer, have a lovely day!");

        infoText.setEditable(false);

        buttonPanel.add(infoText);

        return buttonPanel;
    }

    /**
     * Creating my bottom split panels with number buttons and chose what to do
     * buttons.
     * 
     * @return
     */
    private JPanel buildSplitPanel() {
        JPanel splitPanel = new JPanel();
        splitPanel.setLayout(new GridLayout(1, 3));

        JPanel buttonPanel = creatButtonPanal();
        JPanel extraPanel = buildOptionsPanel();

        splitPanel.add(buttonPanel);
        splitPanel.add(extraPanel);

        return splitPanel;
    }

    private JPanel buildOptionsPanel() {
        JPanel extraPanel = new JPanel();

        extraPanel.setLayout(null);

        extraPanel.setBorder(BorderFactory.createTitledBorder("ATM-Panel"));

        this.txtAmount.setSize(220, 50);
        this.txtAmount.setLocation(20, 30);
        txtAmount.setBorder(BorderFactory.createTitledBorder("Amount to transfer"));

        createChoicePanel(extraPanel);

        extraPanel.add(txtAmount);

        return extraPanel;
    }

    public JPanel creatButtonPanal() {
        JPanel buttonPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(4, 3);
        buttonPanel.setLayout(gridLayout);

        addButton("7", buttonPanel);
        addButton("8", buttonPanel);
        addButton("9", buttonPanel);
        addButton("4", buttonPanel);
        addButton("5", buttonPanel);
        addButton("6", buttonPanel);
        addButton("1", buttonPanel);
        addButton("2", buttonPanel);
        addButton("3", buttonPanel);
        addButton("0", buttonPanel);
        addButton(".", buttonPanel);

        createClearButton(buttonPanel);

        return buttonPanel;
    }

    /**
     * Easy clear option
     * 
     * @param buttonPanel
     */
    private void createClearButton(JPanel buttonPanel) {
        JButton clearButton = new JButton("CE");

        class ClearButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                txtAmount.setText("");
            }
        }
        clearButton.addActionListener(new ClearButtonListener());
        buttonPanel.add(clearButton);
    }

    private void addButton(final String label, JPanel buttonPanel) {
        class DigitButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                if (label.equals(".") && txtAmount.getText().indexOf(".") != -1) {
                    return;
                }
                txtAmount.setText(txtAmount.getText() + label);
            }
        }
        JButton button = new JButton(label);
        buttonPanel.add(button);
        ActionListener listener = new DigitButtonListener();
        button.addActionListener(listener);
    }

    private void createChoicePanel(JPanel panel) {
        ButtonListener listener = new ButtonListener();

        Dimension d = new Dimension(100, 60);
        JButton button = new JButton("Total Balance");
        button.setSize(d);
        button.setLocation(20, 105);
        button.setSize(d);
        panel.add(button);
        button.addActionListener(listener);

        button = new JButton("Loggout");
        button.setLocation(20, 200);
        button.setSize(d);
        panel.add(button);
        button.addActionListener(listener);

        button = new JButton("Deposit");
        button.setLocation(230, 105);
        button.setSize(d);
        panel.add(button);
        button.addActionListener(listener);

        button = new JButton("Withdraw");
        button.setLocation(125, 105);
        button.setSize(d);
        panel.add(button);
        button.addActionListener(listener);

        button = new JButton("Save trans");
        button.setLocation(125, 200);
        button.setSize(d);
        panel.add(button);
        button.addActionListener(listener);

        button = new JButton("History");
        button.setLocation(230, 200);
        button.setSize(d);
        panel.add(button);
        button.addActionListener(listener);
    }

    public static boolean tryLoggin() {

        boolean loggedIn = false;
        String customerNumber = "";
        String pin = "";
        int c = 0;
        int p = 0;

        customerNumber = JOptionPane.showInputDialog(null, "Enter Customer number: ", "Cutomer Number",
                JOptionPane.INFORMATION_MESSAGE);
        pin = JOptionPane.showInputDialog(null, "Enter pin: ", "Cutomer Pin", JOptionPane.INFORMATION_MESSAGE);
        if (customerNumber == null || customerNumber.trim().isEmpty() || pin.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have not entered a valid numbers..", "Logg In Error!",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println("Invalid number!");
            doYouWantToExit();

        } else { // if valid input continue
            c = Integer.valueOf(customerNumber);
            p = Integer.valueOf(pin);
            ATM_GUI.myATM.setCustomerNumber(c);
            if (theBank.checkCustomerBankListAtStart(c, p)) {
                // if found in map function
                loggedIn = true;
                currentCustomer = theBank.getCustomer(c, p);
                currentCustomer = new Customer(c, p);
                currentAccount = currentCustomer.getCheckingAcount();
                ATM_GUI.welcomeMessage();
                ATM_GUI.readFromFile();
                System.out.println("Valid number");
                System.out.println("Current customer number: " + ATM_GUI.myATM.getCurrentCustNumber());
            } else {
                loggInCheckNotCustomer(c, p);
            }
        }
        return loggedIn;
    }

    public static void doYouWantToExit() {

        int reply = JOptionPane.showConfirmDialog(null, "Do you want to try again?", "Continue LoggIn?",
                JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            tryLoggin();
        } else {
            System.exit(0);
        }
    }

    public static void loggInCheckNotCustomer(int c, int p) {
        int msg = JOptionPane.showConfirmDialog(null,
                "You are not in our database.\n" + "Would you like to register as a customer?"
                        + "\nTo exit press Avbryt.",
                "Logg In Error!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
        if (msg == 0) {
            try {
                currentCustomer = theBank.addCustomer(c, p);
                JOptionPane.showMessageDialog(null, "You are now a cutomer!\nPlease try to logg in again.");
                theBank.saveCustomersForStart();
            } catch (IllegalTransactionException e) {
                e.printStackTrace();
            }
        } else if (msg == 1) {
            tryLoggin();
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) throws IllegalTransactionException, InterruptedException, IOException {

        Scanner in = new Scanner(System.in);

        ATM_GUI window = new ATM_GUI();
        window.setTitle("Swirl's ATM.");
        window.configureFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boolean loggedIn = false;
        window.setVisible(false);
        while (!loggedIn) {
            loggedIn = tryLoggin();
        }
        window.setVisible(true);

        in.close();
    }
}
