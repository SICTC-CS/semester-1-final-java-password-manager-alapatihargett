import java.util.*;
import java.io.*;

public class PasswordManager {
    private User currentUser;
    private List<PasswordRecord> passwordRecords;
    private Map<String, List<PasswordRecord>> categoryMap;
    private static final String DATA_FILE = "passwords.txt";
    private static final String USERS_FILE = "users.txt";

    public PasswordManager() {
        this.passwordRecords = new ArrayList<>();
        this.categoryMap = new HashMap<>();
        initializeDatabase();
        loadPasswordRecords();
    }

    private void initializeDatabase() {
        // create users.txt if it doesn't exist
        File usersFile = new File(USERS_FILE);
        if (!usersFile.exists()) {
            try {
                if (usersFile.getParentFile() != null) {
                    usersFile.getParentFile().mkdirs();
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFile))) {
                    writer.write("admin,Admin123!,Admin,User,This is the admin account\n");
                }
                System.out.println("Created users database with default admin account.");
            } catch (IOException e) {
                System.out.println("Warning: Could not create users database: " + e.getMessage());
            }
        }

        // create passwords.txt if it doesn't exist
        File passwordsFile = new File(DATA_FILE);
        if (!passwordsFile.exists()) {
            try {
                if (passwordsFile.getParentFile() != null) {
                    passwordsFile.getParentFile().mkdirs();
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(passwordsFile))) {
                    // Empty file is fine - will be populated as users add passwords
                }
                System.out.println("Created passwords database.");
            } catch (IOException e) {
                System.out.println("Warning: Could not create passwords database: " + e.getMessage());
            }
        }
    }

    public void registerUser(String username, String password, String firstName, String lastName, String hint) { // make user
        // Check if user already exists
        if (userExists(username)) {
            throw new IllegalArgumentException("Username already exists"); // if user already created then it will say that
        }
        
        // Create new user and save to file
        currentUser = new User(username, password, firstName, lastName, hint);
        saveUserToFile(currentUser); // saving
    }

    public boolean login(String username, String password) { // loging in
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) { 
                String[] parts = line.split(","); // "cleaning" but really just separating 
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    currentUser = new User(parts[0], parts[1], parts[2], parts[3], parts[4]); 
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPasswordHint(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return parts[4]; // return password hint
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getCategories() { // pretty self explanatory this one
        Set<String> categories = new HashSet<>();
        for (PasswordRecord record : passwordRecords) {
            categories.add(record.getCategory());
        }
        return new ArrayList<>(categories);
    }

    public List<PasswordRecord> getPasswordRecordsByCategory(String category) { // filter them by cat
        List<PasswordRecord> filtered = new ArrayList<>();
        for (PasswordRecord record : passwordRecords) {
            if (record.getCategory().equalsIgnoreCase(category)) {
                filtered.add(record);
            }
        }
        return filtered;
    }

    public void modifyPasswordRecord(String accountName, String newUsername, String newPassword, String newCategory) { // to modify
        for (PasswordRecord record : passwordRecords) {
            if (record.getAccountName().equals(accountName)) {
                passwordRecords.remove(record); // getting rid of it
                passwordRecords.add(new PasswordRecord(accountName, newUsername, newPassword, newCategory));
                savePasswordRecords(); // save
                return;
            }
        }
        throw new IllegalArgumentException("Account not found");
    }

    private void savePasswordRecords() { //save
        try {
            // Ensure directory exists
            File file = new File(DATA_FILE);
            file.getParentFile().mkdirs(); // create directories if they don't exist -- shouldn't be neccesary but if cloning issues happen then this could fix it
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (PasswordRecord record : passwordRecords) {
                    writer.write(String.format("%s,%s,%s,%s\n",
                        record.getAccountName(),
                        record.getUsername(),
                        record.getPassword(),
                        record.getCategory()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save password records: " + e.getMessage());
        }
    }

    public void deletePasswordRecord(String accountName) { // deleting
        passwordRecords.removeIf(record -> record.getAccountName().equals(accountName)); // i forget what this is called but I remembered it and somehow it worked
        savePasswordRecords();
    }

    public User getCurrentUser() { 
        return currentUser;
    }

    private boolean userExists(String username) { // function from earlier that checks is record is already found
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return false; // if file doesn't exist, no users exist
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Error checking user existence: " + e.getMessage());
        }
        return false;
    }

    private void saveUserToFile(User user) {
        try {
            // ensure directory exists
            File file = new File(USERS_FILE);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            
            // read existing users
            List<String> existingLines = new ArrayList<>();
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        existingLines.add(line);
                    }
                }
            }
            
            // add new user
            existingLines.add(String.format("%s,%s,%s,%s,%s",
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPasswordHint()));
                
            // write all users back to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : existingLines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage());
        }
    }

    private void loadPasswordRecords() { // load in records
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return; // if file doesn't exist, start with empty records
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    passwordRecords.add(new PasswordRecord(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Error loading password records: " + e.getMessage());
        }
    }

    public void printPasswordRecords() { // printing -- nothing too difficult
        if (passwordRecords.isEmpty()) {
            System.out.println("No password records found.");
            return;
        }
        
        for (int i = 0; i < passwordRecords.size(); i++) {
            PasswordRecord record = passwordRecords.get(i);
            System.out.println("Account " + (i + 1) + ":");
            System.out.println("  - Username: " + record.getUsername());
            System.out.println("  - Password: " + record.getPassword());
            System.out.println("---------------------------------");
        }
    }

    public void setCurrentUser(String username, String password) { // when login
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be null or empty.");
        }
        this.currentUser = new User(username, password, "", "", ""); // Minimal user object for login
    }

    public void addPasswordRecord(String accountName, String username, String password, String category) { // adding record -- the reason it is way down here is because I had problems with the old one and had to fix it
        if (currentUser == null) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        PasswordRecord record = new PasswordRecord(accountName, username, password, category);
        passwordRecords.add(record);
        savePasswordRecords();
    }
} 