import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ATM {
    private static ArrayList<User> users = new ArrayList<>();
    private static User currentUser = null;
    private static Chatbot chatbot;
    private static final String DATA_FILE = "users.txt";


    public static void main(String[] args) {
        chatbot = new Chatbot("sk-proj-9b6AtpnPBvVnotcxLBCOT3BlbkFJgDCNlVVVpJXQMk77lfQ9");
        loadUserData();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Chat with AI");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createAccount(scanner);
                    saveUserData();
                    break;
                case 2:
                    login(scanner);
                    break;
                case 3:
                    chatWithAI(scanner);
                    break;
                case 4:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    saveUserData();
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void createAccount(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        for (User user : users){
            if(user.getUsername().equals(username)){
                System.out.println("Username already exists. Please try again.");
                return;
            }
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if(!isValidPassword(password)){
            System.out.println("Password does not meet complexity requirements.");
            return;
        }
        System.out.print("Enter initial balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        users.add(new User(username, password, balance));
        System.out.println("Account created successfully!");
    }

    private static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        return hasUppercase && hasLowercase && hasDigit;
    }
    private static void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                currentUser = user;
                System.out.println("Login successful!");
                userMenu(scanner);
                return;
            }
        }
        System.out.println("Invalid username or password.");
    }



    private static void userMenu(Scanner scanner) {
        while (true) {
            System.out.println("1. View Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. View Transactions");
            System.out.println("5. Calculate Interest");
            System.out.println("6. Chat with AI");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Current balance: $" + currentUser.getBalance());
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    currentUser.deposit(depositAmount);
                    System.out.println("Deposit successful!");
                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    if (currentUser.withdraw(withdrawAmount)) {
                        System.out.println("Withdrawal successful!");
                    } else {
                        System.out.println("Insufficient balance.");
                    }
                    break;
                case 4:
                    System.out.println("Transaction History:");
                    for (String transaction : currentUser.getTransactions()) {
                        System.out.println(transaction);
                    }
                    break;
                case 5:
                    System.out.print("Enter annual interest rate (in %): ");
                    double rate = scanner.nextDouble();
                    System.out.print("Enter time period (in years): ");
                    int time = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    double interest = currentUser.calculateInterest(rate, time);
                    System.out.println("Calculated interest: $" + interest);
                    break;
                case 6:
                    System.out.println("Logged out.");
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void saveUserData(){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }

    private static void loadUserData(){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))){
            users = (ArrayList<User>) in.readObject();
        }catch (IOException | ClassNotFoundException e){
            System.err.println("Error loading user data: " + e.getMessage());
        }
    }

    private static void chatWithAI(Scanner scanner){
        System.out.println("You are now chatting with the AI assistant. Type 'exit' to end the chat.");
        while (true){
            System.out.print("You: ");
            String query = scanner.nextLine();
            if(query.equalsIgnoreCase("exit")){
                System.out.println("AI: Goodbye!");
                return;
            }
            System.out.println("AI: " + chatbot.getResponse(query));
            
        }}
    }

