// Main class to demonstrate the functionality
public class Main {
    public static void main(String[] args) {
        PasswordManager passwordManager = new PasswordManager();

        // Set the current user
        try {
            passwordManager.setCurrentUser ("john_doe", "SecurePassword123!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error setting user: " + e.getMessage());
        }

        // Add a password record
        try {
            passwordManager.addPasswordRecord("MyEmail", "john@example.com", "EmailPassword!1", "Email");
            passwordManager.addPasswordRecord("MyBank", "john_bank", "BankPassword@2", "Bank");
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding password record: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Retrieve and print password records
        for (PasswordRecord record : passwordManager.getPasswordRecords()) {
            System.out.println(record);
        }
    }
}