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

    /** The reply text; never blank. */
    private final String content;

    /** When the reply was posted. */
    private final LocalDateTime postedAt;

    /**
     * Creates a reply.
     *
     * @param text the reply text; not {@code null} or blank
     * @param timestamp when the reply was posted; not {@code null}
     * @throws NullPointerException if an argument is {@code null}
     * @throws IllegalArgumentException if {@code text} is blank
     */
    public Reply(final String text, final LocalDateTime timestamp) {
        Validate.notBlank(text, "a reply must have non-blank text");
        Validate.notNull(timestamp, "a reply must have a posting time");
        this.content = text;
        this.postedAt = timestamp;
    }

    /**
     * Returns the reply text.
     *
     * @return the reply text, never blank
     */
    public String getText() {
        return content;
    }

    /**
     * Returns when the reply was posted.
     *
     * @return the posting time
     */
    public LocalDateTime getTimestamp() {
        return postedAt;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Reply)) {
            return false;
        }
        final Reply that = (Reply) other;
        return content.equals(that.content) && postedAt.equals(that.postedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, postedAt);
    }
}
