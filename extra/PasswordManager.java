import java.util.*;
import java.io.*;

public class PasswordManager {

    private static final String FILE_NAME = "accounts.txt";
    private static Scanner scanner = new Scanner(System.in);
    private static AccountManager accountManager = new AccountManager();
    private static boolean isAuthenticated = false;

    public static void main(String[] args) {
        System.out.println("Welcome to the Password Manager!");
        
        // Attempt login or first-time registration
        if (!login()) {
            System.out.println("Too many login attempts. Exiting...");
            return;
        }

        // Main menu for logged-in users
        boolean running = true;
        while (running) {
            System.out.println("\n1. Add Account");
            System.out.println("2. View Accounts");
            System.out.println("3. Modify Account");
            System.out.println("4. Delete Account");
            System.out.println("5. Generate Password");
            System.out.println("6. View Categories");
            System.out.println("7. Exit");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    addAccount();
                    break;
                case 2:
                    viewAccounts();
                    break;
                case 3:
                    modifyAccount();
                    break;
                case 4:
                    deleteAccount();
                    break;
                case 5:
                    generatePassword();
                    break;
                case 6:
                    viewCategories();
                    break;
                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Login method with 3 attempts
    private static boolean login() {
        System.out.println("Please log in:");

        // First-time user registration
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("First time using this program. Please register.");
            return register();
        }

        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            if (accountManager.authenticate(username, password)) {
                System.out.println("Login successful!");
                return true;
            } else {
                attempts++;
                System.out.println("Invalid credentials. Attempts left: " + (3 - attempts));
            }
        }
        return false;
    }

    // Register method for first-time users
    private static boolean register() {
        System.out.println("Registering a new account...");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        if (!PasswordValidator.isValid(password)) {
            System.out.println("Password does not meet the requirements.");
            return false;
        }

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        accountManager.createAccount(username, password, firstName, lastName);
        System.out.println("Account created successfully!");
        return true;
    }

    // Add new account
    private static void addAccount() {
        System.out.print("Enter account name: ");
        String accountName = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        // Save the account details to the file
        accountManager.addAccount(accountName, username, password, category);
    }

    // View all accounts
    private static void viewAccounts() {
        List<Account> accounts = accountManager.getAllAccounts();
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    // Modify an existing account
    private static void modifyAccount() {
        System.out.print("Enter account name to modify: ");
        String accountName = scanner.nextLine();

        Account account = accountManager.getAccountByName(accountName);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Enter new username: ");
        String newUsername = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        System.out.print("Enter new category: ");
        String newCategory = scanner.nextLine();

        accountManager.modifyAccount(account, newUsername, newPassword, newCategory);
    }

    // Delete an existing account
    private static void deleteAccount() {
        System.out.print("Enter account name to delete: ");
        String accountName = scanner.nextLine();
        accountManager.deleteAccount(accountName);
    }

    // Generate a random password
    private static void generatePassword() {
        String password = PasswordManager.generateStrongPassword();
        System.out.println("Generated Password: " + password);
    }

    // Generate a strong password
    public static String generateStrongPassword() {
        return PasswordValidator.generatePassword();
    }

    // View categories
    private static void viewCategories() {
        Set<String> categories = accountManager.getCategories();
        for (String category : categories) {
            System.out.println(category);
        }
    }

    // Get user choice from the menu
    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        return Integer.parseInt(scanner.nextLine());
    }
}
