// I DID NOT SEE THE GIVEN CODE UNTIL I WAS ALMOST DONE >:o

import java.util.Scanner;
import java.util.List;

public class Main {
    private static Scanner scanner = new Scanner(System.in); //scanner to receive io
    private static PasswordManager passwordManager = new PasswordManager(); //pulling from password manager class

    public static void main(String[] args) {
        try {
            System.out.println("Welcome to Password Manager!");
            while (true) {
                try {
                    System.out.println("\n1. Login");
                    System.out.println("2. Register");
                    System.out.println("3. Exit");
                    
                    int choice = getIntInput();
                    
                    if (choice == 3) {
                        System.out.println("Goodbye!");
                        break;
                    }
                    
                    if (choice == 2) {
                        registerNewUser();
                    } else if (choice == 1) {
                        login();
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                    }
                    
                    if (passwordManager.getCurrentUser() != null) {
                        showMainMenu();
                    }
                // catching any exceptions that might occur
                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                    System.out.println("Please try again.");
                }
            }
        } finally {
            scanner.close();
        }
    }
    // making a new user that saves to the text file
    private static void registerNewUser() {
        try {
            System.out.println("Enter username:");
            String username = scanner.nextLine();
            
            System.out.println("Password requirements:");
            System.out.println("- Minimum 8 characters");
            System.out.println("- At least one uppercase letter");
            System.out.println("- At least one lowercase letter");
            System.out.println("- At least one number");
            System.out.println("- At least one special character (!@#$%^&*()-_=+<>?)");
            System.out.println("\nEnter password:");
            String password = scanner.nextLine();
            
            System.out.println("Enter password hint:");
            String hint = scanner.nextLine();
            System.out.println("Enter first name:");
            String firstName = scanner.nextLine();
            System.out.println("Enter last name:");
            String lastName = scanner.nextLine();

            passwordManager.registerUser(username, password, firstName, lastName, hint);
            System.out.println("Registration successful!");
            // any exceptions
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
            System.out.println("Please try again.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }
    // main print out that provides the options on what to do 
    private static void showMainMenu() {
        while (true) {
            try {
                System.out.println("\nPassword Manager Menu:");
                System.out.println("1. Add Password Record");
                System.out.println("2. View All Records");
                System.out.println("3. View Records by Category");
                System.out.println("4. Modify Password Record");
                System.out.println("5. Delete Password Record");
                System.out.println("6. Generate Strong Password");
                System.out.println("7. Check Password Strength");
                System.out.println("8. Logout");

                int choice = getIntInput();
                // the choices
                switch (choice) {
                    case 1:
                        handleAddRecord();
                        break;
                    case 2:
                        passwordManager.printPasswordRecords();
                        break;
                    case 3:
                        handleViewByCategory();
                        break;
                    case 4:
                        handleModifyRecord();
                        break;
                    case 5:
                        handleDeleteRecord();
                        break;
                    case 6:
                        handleGeneratePassword();
                        break;
                    case 7:
                        handleCheckPasswordStrength();
                        break;
                    case 8:
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }

    private static int getIntInput() { // using a function to catch possible problems -- ensure that nothing crashes
        while (true) {
            try {
                if (scanner.hasNextInt()) {
                    int result = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    return result;
                } else {
                    System.out.println("Please enter a valid number.");
                    scanner.nextLine(); // Consume invalid input
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    private static void handleAddRecord() { //adding a password
        try {
            System.out.println("Enter account name:");
            String accountName = scanner.nextLine();
            System.out.println("Enter username:");
            String username = scanner.nextLine();
            System.out.println("Enter password (or 'generate' for auto-generated):");
            String password = scanner.nextLine();
            // auto-generating a secure password of any length more than a specified amount
            if (password.equalsIgnoreCase("generate")) {
                password = PasswordUtils.generatePassword(12);
                System.out.println("Generated password: " + password);
            }
            
            System.out.println("Enter category:");
            String category = scanner.nextLine();
            
            passwordManager.addPasswordRecord(accountName, username, password, category);
            System.out.println("Password record added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding password record: " + e.getMessage());
        }
    }

    private static void handleViewByCategory() { // viewing records by category
        try {
            List<String> categories = passwordManager.getCategories();
            if (categories.isEmpty()) {
                System.out.println("No categories found.");
                return;
            }
            
            System.out.println("Available categories:");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i));
            }
            
            System.out.println("Select category number (or 0 to cancel):");
            int choice = getIntInput();
            
            if (choice == 0) {
                return;
            }
            
            if (choice > 0 && choice <= categories.size()) {
                List<PasswordRecord> records = passwordManager.getPasswordRecordsByCategory(categories.get(choice - 1));
                if (records.isEmpty()) { // if nothing found
                    System.out.println("No records found in this category.");
                } else {
                    for (PasswordRecord record : records) {
                        System.out.println(record);
                    }
                }
            } else {
                System.out.println("Invalid category selection.");
            }
        } catch (Exception e) {
            System.out.println("Error viewing categories: " + e.getMessage()); // printing exception
        }
    }

    private static void login() {
        int attempts = 0;
        while (attempts < 3) { // only get 3 attempts before you need to restart
            try {
                System.out.println("Enter username:");
                String username = scanner.nextLine();
                System.out.println("Enter password:");
                String password = scanner.nextLine();

                if (passwordManager.login(username, password)) {
                    System.out.println("Login successful!");
                    return;
                } else {
                    System.out.println("Invalid credentials.");
                    attempts++;
                    if (attempts < 3) {
                        System.out.println("Would you like to see your password hint? (y/n)");
                        String response = scanner.nextLine();
                        if (response.equalsIgnoreCase("y")) {
                            String hint = passwordManager.getPasswordHint(username);
                            if (hint != null) {
                                System.out.println("Password hint: " + hint);
                            } else {
                                System.out.println("No hint found for this username."); // if there was no hint given when it was created
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                attempts++;
            }
        }
        System.out.println("Too many failed attempts. Please try again later."); // can just restart program to fix but gives the concept
    }

    private static void handleModifyRecord() { // modifying a password -- pretty much copying the create 
        try {
            System.out.println("Enter account name to modify:");
            String accountName = scanner.nextLine();
            System.out.println("Enter new username:");
            String newUsername = scanner.nextLine();
            System.out.println("Enter new password (or 'generate' for auto-generated):");
            String newPassword = scanner.nextLine();
            
            if (newPassword.equalsIgnoreCase("generate")) {
                newPassword = PasswordUtils.generatePassword(12);
                System.out.println("Generated password: " + newPassword);
            }
            
            System.out.println("Enter new category:");
            String newCategory = scanner.nextLine();
            
            passwordManager.modifyPasswordRecord(accountName, newUsername, newPassword, newCategory);
            System.out.println("Record modified successfully!");
        } catch (Exception e) {
            System.out.println("Error modifying record: " + e.getMessage());
        }
    }

    private static void handleDeleteRecord() { // deleting password
        try {
            System.out.println("Enter account name to delete:");
            String accountName = scanner.nextLine();
            
            passwordManager.deletePasswordRecord(accountName);
            System.out.println("Record deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }

    private static void handleGeneratePassword() { // pulls from a lot of random letters/numbers/symbols
        try {
            System.out.println("Enter desired password length (minimum 8):");
            int length = getIntInput();
            String password = PasswordUtils.generatePassword(length);
            System.out.println("Generated password: " + password);
        } catch (Exception e) {
            System.out.println("Error generating password: " + e.getMessage());
        }
    }

    private static void handleCheckPasswordStrength() { // gives you the break time of a password given by the user
        try {
            System.out.println("Enter password to check:");
            String password = scanner.nextLine();
            String breakTime = PasswordUtils.calculateBreakTime(password);
            System.out.println("Estimated time to crack: " + breakTime);
        } catch (Exception e) {
            System.out.println("Error checking password strength: " + e.getMessage());
        }
    }

    // Add similar try-catch blocks to other handler methods...
}