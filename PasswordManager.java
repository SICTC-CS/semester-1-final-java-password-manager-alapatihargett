import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;
import java.util.regex.Pattern;

// User class to represent a user
class User {
    private String username;
    private String password; // Ideally, this should be a hashed password

    public User(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be null or empty.");
        }
        this.username = username;
        this.password = password; // In a real application, hash the password here
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password; // Consider returning a masked version or not exposing this directly
    }
}

// PasswordRecord class to represent a password entry
class PasswordRecord {
    private String accountName;
    private String username;
    private String password;
    private String category;

    public PasswordRecord(String accountName, String username, String password, String category) {
        this.accountName = accountName;
        this.username = username;
        this.password = password;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Account: " + accountName + ", Username: " + username + ", Category: " + category;
    }
}

// PasswordUtils class for password-related utilities
class PasswordUtils {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+<>?";
    private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;

    private static final int MIN_LENGTH = 8;
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*()\\-_=+<>?]");

    public static String generatePassword(int length) {
        if (length < MIN_LENGTH) {
            throw new IllegalArgumentException("Password length must be at least " + MIN_LENGTH);
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        
        // Ensure the password contains at least one character from each category
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        // Fill the rest of the password length with random characters from all categories
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        return shuffleString(password.toString());
    }

    public static boolean checkPasswordRequirements(String password) {
        return password.length() >= MIN_LENGTH &&
               UPPERCASE_PATTERN.matcher(password).find() &&
               LOWERCASE_PATTERN.matcher(password).find() &&
               DIGIT_PATTERN.matcher(password).find() &&
               SPECIAL_CHAR_PATTERN.matcher(password).find();
    }

    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        SecureRandom random = new SecureRandom();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }
}

// PasswordManager class to manage users and their password records
public class PasswordManager {
    private User currentUser ; // The currently logged-in user
    private List<PasswordRecord> passwordRecords; // List to store password records

    public PasswordManager() {
        this.passwordRecords = new ArrayList<>();
    }

    public void setCurrentUser (String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be null or empty.");
        }
        this.currentUser  = new User(username, password);
    }

    public void addPasswordRecord(String accountName, String username, String password,
String category) {
        if (currentUser   == null) {
            throw new IllegalStateException("No user is currently logged in.");
        }

        // Validate the password requirements
        if (!PasswordUtils.checkPasswordRequirements(password)) {
            throw new IllegalArgumentException("Password does not meet the required criteria.");
        }

        // Create a new password record and add it to the list
        PasswordRecord record = new PasswordRecord(accountName, username, password, category);
        passwordRecords.add(record);
    }

    public List<PasswordRecord> getPasswordRecords() {
        return passwordRecords;
    }

    public User getCurrentUser  () {
        return currentUser  ;
    }
}