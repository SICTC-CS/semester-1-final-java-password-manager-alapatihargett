import java.security.SecureRandom;
import java.util.regex.Pattern;

public class PasswordUtils {
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

    /**
     * Generates a secure password of the specified length.
     *
     * @param length the desired length of the password
     * @return a randomly generated password
     */
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

    /**
     * Checks if the given password meets the required criteria.
     *
     * @param password the password to check
     * @return true if the password meets the requirements, false otherwise
     */
    public static boolean checkPasswordRequirements(String password) {
        return password.length() >= MIN_LENGTH &&
               UPPERCASE_PATTERN.matcher(password).find() &&
               LOWERCASE_PATTERN.matcher(password).find() &&
               DIGIT_PATTERN.matcher(password).find() &&
               SPECIAL_CHAR_PATTERN.matcher(password).find();
    }

    /**
     * Shuffles the characters in a given string to randomize its order.
     *
     * @param input the string to shuffle
     * @return a new string with characters shuffled
     */
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