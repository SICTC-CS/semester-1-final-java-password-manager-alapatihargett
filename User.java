public class User { // probably the most important class in the whole project
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String passwordHint;

    public User(String username, String password, String firstName, String lastName, String passwordHint) {
        if (username == null || username.isEmpty() || 
            password == null || password.isEmpty() ||
            firstName == null || firstName.isEmpty() ||
            lastName == null || lastName.isEmpty() ||
            passwordHint == null || passwordHint.isEmpty()) {
            throw new IllegalArgumentException("All fields must be filled.");
        }
        
        if (!PasswordUtils.checkPasswordRequirements(password)) { // checking for security
            throw new IllegalArgumentException("Password does not meet security requirements.");
        }
        
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHint = passwordHint;
    }

    // getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPasswordHint() { return passwordHint; }
}
