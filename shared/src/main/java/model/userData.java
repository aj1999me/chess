package model;

import java.util.Objects;

public class userData {
    private final String username;
    private final String password;
    private final String email;

    userData(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public String getUser() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        userData other = (userData) o;
        return other.username.equals(username) &&
                other.password.equals(password) &&
                other.email.equals(email);
    }
    @Override
    public int hashCode() {
        return Objects.hash(username, password, email);
    }
}
