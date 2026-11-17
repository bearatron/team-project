package entities;

import java.util.Objects;

public class User {
    private String username;
    private UserType userType;

    /**
     * Return the user's username
     *
     * @return the username of the user
     */
    public String getUserName() {
        return this.username;
    }

    /**
     * Return the user's user type
     *
     * @return the type of the user
     */
    public UserType getUserType() {
        return this.userType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && userType == user.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, userType);
    }
}

