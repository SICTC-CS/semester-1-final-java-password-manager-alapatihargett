import java.util.Objects;

public class PasswordRecord {
    private String accountName;
    private String username;
    private String password;
    private String category;

    public PasswordRecord(String accountName, String username, String password, String category) { 
        if (accountName == null || accountName.isEmpty() ||
            username == null || username.isEmpty() ||
            password == null || password.isEmpty() ||
            category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Fields cannot be null or empty");
        }
        this.accountName = accountName;
        this.username = username;
        this.password = password; 
        this.category = category;
    }
    // getters
    public String getAccountName() {
        return accountName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "PasswordRecord{" +
                "accountName='" + accountName + '\'' +
                ", username='" + username + '\'' +
                ", category='" + category + '\'' +
                '}'; // omitting password for security reasons
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PasswordRecord)) 
            return false;
        PasswordRecord that = (PasswordRecord) o;
        return Objects.equals(accountName, that.accountName) &&
               Objects.equals(username, that.username) &&
               Objects.equals(password, that.password) &&
               Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountName, username, password, category);
    }
} 