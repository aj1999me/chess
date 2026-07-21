package model;

import java.util.Objects;

public class authData {
    private final String authToken;
    private final String username;

    authData(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }
    public String getAuthToken() {
        return authToken;
    }
    public String getUsername() {
        return username;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        authData other = (authData) o;
        return other.authToken.equals(authToken) &&
                other.username.equals(username);
    }
    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }
}
