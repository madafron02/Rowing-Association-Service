package nl.tudelft.sem.template.auth.domain;

import java.util.Objects;

public class AccountCredentials {

    private String userId;
    private String password;

    public AccountCredentials(String userId, String password){
        this.userId = userId;
        this.password = password;
    }
    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountCredentials)) return false;
        AccountCredentials that = (AccountCredentials) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getPassword());
    }
}
