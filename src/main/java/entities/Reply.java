package entities;

import java.time.LocalDateTime;
import java.util.Objects;
import org.apache.commons.lang3.Validate;

/**
 * An immutable reply to a {@link Review}.
 *
 * <p>The author is always the owner of the reviewed truck, so no author is
 * stored.
 */
public final class Reply {

    private final String text;
    private final LocalDateTime timestamp;

    /**
     * Creates a reply.
     *
     * @param text the reply text; not {@code null} or blank
     * @param timestamp when the reply was posted; not {@code null}
     * @throws NullPointerException if an argument is {@code null}
     * @throws IllegalArgumentException if {@code text} is blank
     */
    public Reply(String text, LocalDateTime timestamp) {
        Validate.notBlank(text, "reply text must be non-blank");
        Validate.notNull(timestamp, "reply timestamp must be non-null");
        this.text = text;
        this.timestamp = timestamp;
    }

    /**
     * Returns the reply text.
     *
     * @return the reply text, never blank
     */
    public String getText() {
        return text;
    }

    /**
     * Returns when the reply was posted.
     *
     * @return the posting time
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Reply)) {
            return false;
        }
        Reply that = (Reply) other;
        return text.equals(that.text) && timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, timestamp);
    }
}
