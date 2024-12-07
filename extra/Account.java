public class Account {
    private String name;
    private String username;
    private String password;
    private String category;

    public Account() {}

    public Account(String name, String username, String password, String category) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.category = category;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Account Name: " + name + "\n" +
            "Username: " + username + "\n" +
            "Password: " + password + "\n" +
            "Category: " + category + "\n---------------------------------";
    }
}
