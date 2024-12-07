import java.util.Random;

public class PasswordValidator {

    // Check to see if the password has the necessary requirments for their password
    public static boolean isValid(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") && // At least one uppercase letter
               password.matches(".*[0-9].*") && // At least one number
               password.matches(".*[!@#$%^&*()].*"); // At least one special character
    }

    // if they dont know what to keep as their password genrate a strong password
    public static String generatePassword() {
        String upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerChars = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()";

        String allChars = upperChars + lowerChars + digits + specialChars;
        Random rand = new Random();
        StringBuilder password = new StringBuilder();
        password.append(upperChars.charAt(rand.nextInt(upperChars.length())));
        password.append(digits.charAt(rand.nextInt(digits.length())));
        password.append(specialChars.charAt(rand.nextInt(specialChars.length())));

        for (int i = 3; i < 12; i++) {
            password.append(allChars.charAt(rand.nextInt(allChars.length())));
        }

        return password.toString();
    }
}
