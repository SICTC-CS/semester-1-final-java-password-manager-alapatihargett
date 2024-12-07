import java.util.*;
import java.io.*;

public class AccountManager {
    private static final String FILE_NAME = "accounts.txt";
    private List<Account> accounts;

    public AccountManager() {
        this.accounts = new ArrayList<>();
        loadAccounts();
    }

    // Load accounts from the file
    private void loadAccounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            Account currentAccount = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Account:")) {
                    if (currentAccount != null) {
                        accounts.add(currentAccount);
                    }
                    currentAccount = new Account();
                    currentAccount.setName(line.substring(8).trim());
                } else if (line.startsWith("Username:")) {
                    currentAccount.setUsername(line.substring(10).trim());
                } else if (line.startsWith("Password:")) {
                    currentAccount.setPassword(line.substring(9).trim());
                } else if (line.startsWith("Category:")) {
                    currentAccount.setCategory(line.substring(9).trim());
                }
            }
            if (currentAccount != null) {
                accounts.add(currentAccount);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save accounts to the file
    private void saveAccounts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Account account : accounts) {
                writer.write("Account: " + account.getName() + "\n");
                writer.write("Username: " + account.getUsername() + "\n");
                writer.write("Password: " + account.getPassword() + "\n");
                writer.write("Category: " + account.getCategory() + "\n");
                writer.write("---------------------------------\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Authenticate a user
    public boolean authenticate(String username, String password) {
        // Placeholder for authentication logic
        return username.equals("admin") && password.equals("password123");
    }

    // Create a new account
    public void createAccount(String username, String password, String firstName, String lastName) {
        Account account = new Account(username, password, firstName, lastName);
        accounts.add(account);
        saveAccounts();
    }

    // Add a new account
    public void addAccount(String accountName, String username, String password, String category) {
        Account account = new Account(accountName, username, password, category);
        accounts.add(account);
        saveAccounts();
    }

    // Modify an existing account
    public void modifyAccount(Account account, String newUsername, String newPassword, String newCategory) {
        account.setUsername(newUsername);
        account.setPassword(newPassword);
        account.setCategory(newCategory);
        saveAccounts();
    }

    // Delete an account by name
    public void deleteAccount(String accountName) {
        accounts.removeIf(account -> account.getName().equals(accountName));
        saveAccounts();
    }

    // Get all accounts
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    // Get an account by name
    public Account getAccountByName(String name) {
        for (Account account : accounts) {
            if (account.getName().equals(name)) {
                return account;
            }
        }
        return null;
    }

    // Get all categories
    public Set<String> getCategories() {
        Set<String> categories = new HashSet<>();
        for (Account account : accounts) {
            categories.add(account.getCategory());
        }
        return categories;
    }
}
