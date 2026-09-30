package praktikum.model;

public class User {
    public String email;
    public String password;
    public String name;

    public User() {}

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}