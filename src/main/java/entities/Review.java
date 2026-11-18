package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * An immutable review of a food truck.
 *
 * <p>A review rates any non-empty subset of {@link Category categories}, at most once each,
 * and may include text, photo paths and one owner {@link Reply}.
 *
 * <p>Privacy rule: an in-app reviewer's name is never shown. {@link #getPublicAuthorName()}
 * returns {@value #ANONYMOUS} for {@link ReviewSource#APP} reviews. Imported reviews show
 * the display name that came with them. The author is still stored so that ownership can
 * be checked with {@link #isAuthoredBy(User)}.
 *
 * <p>Editing returns a new {@code Review}. Checking who may edit or delete a review is a
 * use-case rule and does not belong here.
 */
public final class Review {

    /** The name shown publicly for reviews posted in the app. */
    public static final String ANONYMOUS = "Anonymous";

    private final User author;
    private final String authorDisplayName;
    private final ReviewSource source;
    private final Map<Category, Rating> ratings;
    private final String text;
    private final List<String> photoPaths;
    private final Reply reply;
    private final LocalDateTime timestamp;
    private final LocalDateTime editedTimestamp;

    private Review(User author, String authorDisplayName, ReviewSource source,
                   Collection<Rating> ratings, String text, List<String> photoPaths,
                   Reply reply, LocalDateTime timestamp, LocalDateTime editedTimestamp) {
        this.source = Objects.requireNonNull(source, "source");
        if (source == ReviewSource.APP) {
            this.author = Objects.requireNonNull(author, "author");
            this.authorDisplayName = null;
        } else {
            if (author != null) {
                throw new IllegalArgumentException("imported reviews have no in-app author");
            }
            Objects.requireNonNull(authorDisplayName, "authorDisplayName");
            if (authorDisplayName.isBlank()) {
                throw new IllegalArgumentException("authorDisplayName must not be blank");
            }
            this.author = null;
            this.authorDisplayName = authorDisplayName;
        }
        this.ratings = copyRatings(ratings);
        this.text = (text == null || text.isBlank()) ? null : text;
        this.photoPaths = copyPhotoPaths(photoPaths);
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp");
        if (editedTimestamp != null && editedTimestamp.isBefore(timestamp)) {
            throw new IllegalArgumentException("a review cannot be edited before it was posted");
        }
        this.editedTimestamp = editedTimestamp;
        if (reply != null && reply.getTimestamp().isBefore(timestamp)) {
            throw new IllegalArgumentException("a reply cannot be posted before its review");
        }
        this.reply = reply;
    }

    /**
     * Creates a review posted in the app.
     *
     * @param author the reviewer; not {@code null}
     * @param ratings one or more ratings, each for a different category
     * @param text the review text, or {@code null} or blank for none
     * @param photoPaths paths to photos, or {@code null} for none; no element may be
     *     {@code null} or blank
     * @param timestamp when the review was posted; not {@code null}
     * @return the new review
     * @throws NullPointerException if a required argument is {@code null}
     * @throws IllegalArgumentException if the ratings are empty or repeat a category, or a
     *     photo path is blank
     */
    public static Review posted(User author, Collection<Rating> ratings, String text,
                                List<String> photoPaths, LocalDateTime timestamp) {
        return new Review(author, null, ReviewSource.APP, ratings, text, photoPaths,
                null, timestamp, null);
    }

    /**
     * Creates a review imported from Google Maps.
     *
     * @param authorDisplayName the name shown with the imported review; not blank
     * @param ratings one or more ratings, each for a different category
     * @param text the review text, or {@code null} or blank for none
     * @param photoPaths paths to photos, or {@code null} for none
     * @param timestamp when the review was originally posted; not {@code null}
     * @return the new review
     * @throws NullPointerException if a required argument is {@code null}
     * @throws IllegalArgumentException if a value is invalid, as for
     *     {@link #posted(User, Collection, String, List, LocalDateTime)}
     */
    public static Review imported(String authorDisplayName, Collection<Rating> ratings,
                                  String text, List<String> photoPaths,
                                  LocalDateTime timestamp) {
        return new Review(null, authorDisplayName, ReviewSource.GOOGLE_MAPS, ratings, text,
                photoPaths, null, timestamp, null);
    }

    /**
     * Returns an edited copy of this review. The author, source, original timestamp and
     * reply are kept.
     *
     * @param newRatings the replacement ratings
     * @param newText the replacement text, or {@code null} or blank for none
     * @param newPhotoPaths the replacement photo paths, or {@code null} for none
     * @param editedAt when the edit was made; not before the original post or last edit
     * @return the edited review
     * @throws IllegalStateException if this review was imported and so has no in-app author
     * @throws IllegalArgumentException if a value is invalid, or {@code editedAt} is earlier
     *     than the original post or the last edit
     */
    public Review edit(Collection<Rating> newRatings, String newText,
                       List<String> newPhotoPaths, LocalDateTime editedAt) {
        if (source != ReviewSource.APP) {
            throw new IllegalStateException("imported reviews cannot be edited");
        }
        Objects.requireNonNull(editedAt, "editedAt");
        if (editedTimestamp != null && editedAt.isBefore(editedTimestamp)) {
            throw new IllegalArgumentException("edits must not go back in time");
        }
        return new Review(author, null, source, newRatings, newText, newPhotoPaths,
                reply, timestamp, editedAt);
    }

    /**
     * Returns a copy of this review with {@code newReply} as its only reply, replacing any
     * earlier reply.
     *
     * @param newReply the owner's reply; not {@code null} and not before this review
     * @return the review with the reply
     */
    public Review withReply(Reply newReply) {
        Objects.requireNonNull(newReply, "newReply");
        return new Review(author, authorDisplayName, source, ratings.values(), text,
                photoPaths, newReply, timestamp, editedTimestamp);
    }

    /** Returns a copy of this review with no reply. */
    public Review withoutReply() {
        return new Review(author, authorDisplayName, source, ratings.values(), text,
                photoPaths, null, timestamp, editedTimestamp);
    }

    /**
     * Returns the name to show publicly for this review's author: {@value #ANONYMOUS} for
     * reviews posted in the app, or the imported display name otherwise.
     */
    public String getPublicAuthorName() {
        return source == ReviewSource.APP ? ANONYMOUS : authorDisplayName;
    }

    /**
     * Returns whether {@code user} wrote this review in the app. Imported reviews have no
     * in-app author and always return {@code false}.
     */
    public boolean isAuthoredBy(User user) {
        return author != null && author.equals(user);
    }

    /**
     * Returns the in-app author, for ownership checks and storage. Never show this to other
     * users; use {@link #getPublicAuthorName()} for display.
     */
    public Optional<User> getAuthor() {
        return Optional.ofNullable(author);
    }

    /** Returns where this review came from. */
    public ReviewSource getSource() {
        return source;
    }

    /** Returns the ratings by category, in category order. The map cannot be changed. */
    public Map<Category, Rating> getRatings() {
        return ratings;
    }

    /** Returns the rating for {@code category}, or empty if it was not rated. */
    public Optional<Rating> getRating(Category category) {
        return Optional.ofNullable(ratings.get(category));
    }

    /** Returns the mean score of all ratings in this review. */
    public double getAverageScore() {
        int total = 0;
        for (Rating rating : ratings.values()) {
            total += rating.getScore();
        }
        return (double) total / ratings.size();
    }

    /** Returns the review text, or empty if there is none. */
    public Optional<String> getText() {
        return Optional.ofNullable(text);
    }

    /** Returns the photo paths. The list cannot be changed and may be empty. */
    public List<String> getPhotoPaths() {
        return photoPaths;
    }

    /** Returns the owner's reply, or empty if there is none. */
    public Optional<Reply> getReply() {
        return Optional.ofNullable(reply);
    }

    /** Returns when the review was first posted. */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /** Returns when the review was last edited, or empty if it never was. */
    public Optional<LocalDateTime> getEditedTimestamp() {
        return Optional.ofNullable(editedTimestamp);
    }

    /** Returns whether the review has been edited since it was posted. */
    public boolean isEdited() {
        return editedTimestamp != null;
    }

    private static Map<Category, Rating> copyRatings(Collection<Rating> ratings) {
        Objects.requireNonNull(ratings, "ratings");
        if (ratings.isEmpty()) {
            throw new IllegalArgumentException("a review needs at least one rating");
        }
        Map<Category, Rating> byCategory = new EnumMap<>(Category.class);
        for (Rating rating : ratings) {
            Objects.requireNonNull(rating, "ratings must not contain null");
            if (byCategory.put(rating.getCategory(), rating) != null) {
                throw new IllegalArgumentException(
                        "category rated more than once: " + rating.getCategory());
            }
        }
        return Collections.unmodifiableMap(byCategory);
    }

    private static List<String> copyPhotoPaths(List<String> photoPaths) {
        if (photoPaths == null) {
            return Collections.emptyList();
        }
        List<String> copy = new ArrayList<>(photoPaths.size());
        for (String path : photoPaths) {
            Objects.requireNonNull(path, "photoPaths must not contain null");
            if (path.isBlank()) {
                throw new IllegalArgumentException("photo paths must not be blank");
            }
            copy.add(path);
        }
        return Collections.unmodifiableList(copy);
    }
}
