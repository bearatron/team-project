package entities;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

/**
 * Represents a user of the application.
 */
public class User {
    private final String username;
    private final UserType userType;

    /**
     * Creates a new user with the given username and type.
     *
     * @param username the user's username
     * @param userType the user's user type
     */
    public User(String username, UserType userType) {
        Validate.notEmpty(username, "username must be non-empty");
        this.username = username;
        this.userType = userType;
    }

    /**
     * Return the user's username.
     *
     * @return the username of the user
     */
    public String getUserName() {
        return this.username;
    }

    /**
     * Return the user's user type.
     *
     * @return the type of the user
     */
    public UserType getUserType() {
        return this.userType;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        User user = (User) object;
        return Objects.equals(username, user.username) && userType == user.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, userType);
    }
}

