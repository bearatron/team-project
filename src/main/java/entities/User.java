package entities;

import java.util.Objects;

public class User {
    private String username;
    private UserType userType;

    public String getUserName() {
        return this.username;
    }

    public UserType getUserType() {
        return this.userType;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) return false;
        User user = (User) object;
        return Objects.equals(username, user.username) && getUserType() == user.getUserType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, getUserType());
    }
}

