package entities;

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
    public User(String username, UserType userType)  {
        this.username = username;
        this.userType = userType;
    }

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

