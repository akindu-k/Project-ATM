import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    private String username;
    private String hashedPassword;
    private double balance;
    private ArrayList<String> transactions;

    public User(String username, String password, double initialBalance) {
        this.username = username;
        this.hashedPassword = hashPassword(password);
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
        transactions.add("Account created with balance: $" + initialBalance);
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.hashedPassword.equals(hashPassword(password));
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add("Deposited: $" + amount + " | New Balance: $" + balance);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactions.add("Withdrew: $" + amount + " | New Balance: $" + balance);
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<String> getTransactions() {
        return transactions;
    }

    public double calculateInterest(double rate, int time) {
        return balance * rate * time / 100;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
