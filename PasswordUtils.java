import java.security.SecureRandom;
import java.util.regex.Pattern;

public class PasswordUtils { // had to start by asking GPT where to start but then I got it
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

    // Add new constant for password cracking estimation
    private static final double COMBINATIONS_PER_SECOND = 1000000000; // 1 billion attempts per second -- a lot

    public static String generatePassword(int length) { // giving the user a password
        if (length < MIN_LENGTH) {
            throw new IllegalArgumentException("Password length must be at least " + MIN_LENGTH);
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        // ensure the password contains at least one character from each category
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        // fill the rest of the password length with random characters from all categories
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        return shuffleString(password.toString());
    }

    public static boolean checkPasswordRequirements(String password) { // make sure it is secure
        return password.length() >= MIN_LENGTH &&
               UPPERCASE_PATTERN.matcher(password).find() &&
               LOWERCASE_PATTERN.matcher(password).find() &&
               DIGIT_PATTERN.matcher(password).find() &&
               SPECIAL_CHAR_PATTERN.matcher(password).find();
    }

    private static String shuffleString(String input) { // randomly scramble string/password
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

    /**
     * Calculates the estimated time to crack a password
     */
    public static String calculateBreakTime(String password) { // this one was rough
        int length = password.length();
        int poolSize = 0;
        
        // gpt for this bit
        if (password.matches(".*[A-Z].*")) poolSize += 26;
        if (password.matches(".*[a-z].*")) poolSize += 26;
        if (password.matches(".*[0-9].*")) poolSize += 10;
        if (password.matches(".*[!@#$%^&*()\\-_=+<>?].*")) poolSize += 15;
        
        double combinations = Math.pow(poolSize, length);
        double seconds = combinations / COMBINATIONS_PER_SECOND;
        
        return formatTime(seconds);
    }

    private static String formatTime(double seconds) { // GPT was absolutly no help for this -- pretty much had to figure it out myself
        if (seconds < 60) return String.format("%.2f seconds", seconds);
        if (seconds < 3600) return String.format("%.2f minutes", seconds/60);
        if (seconds < 86400) return String.format("%.2f hours", seconds/3600);
        if (seconds < 31536000) return String.format("%.2f days", seconds/86400);
        return String.format("%.2f years", seconds/31536000);
    }
} 