import entities.User;
import entities.UserType;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    void equal() {
        User user1 = new User("Bob", UserType.REVIEWER);
        User user2 = new User("Bob", UserType.REVIEWER);

        assert(user1.equals(user2));
    }

    @Test
    void equalHash() {
        User user1 = new User("Bob", UserType.REVIEWER);
        User user2 = new User("Bob", UserType.REVIEWER);

        assert(user1.hashCode() == user2.hashCode());
    }

    @Test
    void differentUsername() {
        User user1 = new User("Bob", UserType.REVIEWER);
        User user2 = new User("Alice", UserType.REVIEWER);

        assert(!(user1.equals(user2)));
    }

    @Test
    void differentUserType() {
        User user1 = new User("Bob", UserType.REVIEWER);
        User user2 = new User("Bob", UserType.OWNER);

        assert(!(user1.equals(user2)));
    }
}
