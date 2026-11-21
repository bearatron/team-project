package entities;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;

/**
 * An immutable review of a food truck.
 *
 * <p>A review gives a score to any non-empty subset of {@link Category
 * categories}, at most once each, and may include text, photos and one reply
 * from the truck's owner.
 */
public final class Review {

    /** The lowest score a category can be given. */
    public static final int MIN_SCORE = 1;

    /** The highest score a category can be given. */
    public static final int MAX_SCORE = 10;

    /** The in-app author, or {@code null} for imported reviews. */
    private final User reviewer;

    /** The imported author's name, or {@code null} for in-app reviews. */
    private final String importedAuthorName;

    /** Where the review came from. */
    private final ReviewSource origin;

    /** The score for each rated category, in category order. */
    private final Map<Category, Integer> scores;

    /** The review text, or {@code null} if there is none. */
    private final String body;

    /** The attached photos; may be empty. */
    private final List<Image> images;

    /** The owner's reply, or {@code null} if there is none. */
    private final Reply ownerReply;

    /** When the review was posted. */
    private final LocalDateTime postedAt;

    /**
     * Creates a review. Use {@link #fromInApp} or {@link #fromGoogleMaps} for
     * new reviews; this constructor also restores a stored review that may
     * already have a reply.
     *
     * <p>Exactly one of {@code author} and {@code authorDisplayName} must be
     * given. An {@code author} makes this a {@link ReviewSource#APP} review;
     * an {@code authorDisplayName} makes it a
     * {@link ReviewSource#GOOGLE_MAPS} review.
     *
     * @param author the in-app author, or {@code null} for imported reviews
     * @param authorDisplayName the author's name on Google Maps, or
     *     {@code null} for in-app reviews; not blank
     * @param ratings a score from {@value #MIN_SCORE} to {@value #MAX_SCORE}
     *     for each rated category; at least one entry
     * @param text the review text, or {@code null} for none
     * @param photos the attached photos, or {@code null} for none
     * @param reply the owner's reply, or {@code null} for none; not posted
     *     before the review
     * @param timePosted when the review was posted; not {@code null}
     * @throws NullPointerException if a required argument is {@code null}
     * @throws IllegalArgumentException if an argument breaks a rule above
     */
    public Review(final User author, final String authorDisplayName,
                  final Map<Category, Integer> ratings, final String text,
                  final List<Image> photos, final Reply reply,
                  final LocalDateTime timePosted) {
        validateAuthor(author, authorDisplayName);
        validateRatings(ratings);
        validatePhotos(photos);
        Validate.notNull(timePosted, "a review must have a posting time");
        validateReply(reply, timePosted);

        this.reviewer = author;
        this.importedAuthorName = authorDisplayName;
        if (author != null) {
            this.origin = ReviewSource.APP;
        } else {
            this.origin = ReviewSource.GOOGLE_MAPS;
        }
        this.scores = Collections.unmodifiableMap(new EnumMap<>(ratings));
        this.body = text;
        if (photos == null) {
            this.images = List.of();
        } else {
            this.images = List.copyOf(photos);
        }
        this.ownerReply = reply;
        this.postedAt = timePosted;
    }

    /**
     * Creates a review posted by a reviewer in this app.
     *
     * @param author the reviewer; not {@code null}
     * @param ratings a score from {@value #MIN_SCORE} to {@value #MAX_SCORE}
     *     for each rated category; at least one entry
     * @param text the review text, or {@code null} for none
     * @param photos the attached photos, or {@code null} for none
     * @param timePosted when the review was posted; not {@code null}
     * @return the new review
     */
    public static Review fromInApp(final User author,
                                   final Map<Category, Integer> ratings,
                                   final String text,
                                   final List<Image> photos,
                                   final LocalDateTime timePosted) {
        return new Review(author, null, ratings, text, photos, null,
                timePosted);
    }

    /**
     * Creates a review imported from Google Maps.
     *
     * @param authorDisplayName the name shown on Google Maps; not blank
     * @param ratings a score from {@value #MIN_SCORE} to {@value #MAX_SCORE}
     *     for each rated category; at least one entry
     * @param text the review text, or {@code null} for none
     * @param photos the attached photos, or {@code null} for none
     * @param timePosted when the review was originally posted; not
     *     {@code null}
     * @return the new review
     */
    public static Review fromGoogleMaps(final String authorDisplayName,
                                        final Map<Category, Integer> ratings,
                                        final String text,
                                        final List<Image> photos,
                                        final LocalDateTime timePosted) {
        return new Review(null, authorDisplayName, ratings, text, photos,
                null, timePosted);
    }

    /**
     * Returns a copy of this review with {@code reply} as its only reply,
     * replacing any earlier one.
     *
     * @param reply the owner's reply; not {@code null} and not posted before
     *     this review
     * @return the review with the reply
     */
    public Review withReply(final Reply reply) {
        Validate.notNull(reply, "use withoutReply() to remove a reply");
        return new Review(reviewer, importedAuthorName, scores, body, images,
                reply, postedAt);
    }

    /**
     * Returns a copy of this review with no reply.
     *
     * @return the review without a reply
     */
    public Review withoutReply() {
        return new Review(reviewer, importedAuthorName, scores, body, images,
                null, postedAt);
    }

    /**
     * Returns whether {@code user} wrote this review in the app.
     *
     * @param user the user to check; may be {@code null}
     * @return {@code true} if {@code user} is this review's in-app author;
     *     always {@code false} for imported reviews
     */
    public boolean isAuthoredBy(final User user) {
        return reviewer != null && reviewer.equals(user);
    }

    /**
     * Returns the in-app author.
     *
     * @return the author, or {@code null} for imported reviews
     */
    public User getAuthor() {
        return reviewer;
    }

    /**
     * Returns the name that came with an imported review.
     *
     * @return the imported author's name, or {@code null} for in-app reviews
     */
    public String getAuthorDisplayName() {
        return importedAuthorName;
    }

    /**
     * Returns where this review came from.
     *
     * @return the review's source
     */
    public ReviewSource getSource() {
        return origin;
    }

    /**
     * Returns the score for each rated category.
     *
     * @return an unmodifiable map from category to score, in category order
     */
    public Map<Category, Integer> getRatings() {
        return scores;
    }

    /**
     * Returns the score for one category.
     *
     * @param category the category to look up
     * @return the score, or {@code null} if the category was not rated
     */
    public Integer getRating(final Category category) {
        return scores.get(category);
    }

    /**
     * Returns the mean score across the rated categories.
     *
     * @return the average score, from {@value #MIN_SCORE} to
     *     {@value #MAX_SCORE}
     */
    public double getAverageScore() {
        // The constructor rejects reviews with no ratings, so the division
        // below never divides by zero.
        assert !scores.isEmpty() : "a review always has at least one rating";
        int total = 0;
        for (final int score : scores.values()) {
            total += score;
        }
        return (double) total / scores.size();
    }

    /**
     * Returns the review text.
     *
     * @return the text, or {@code null} if there is none
     */
    public String getText() {
        return body;
    }

    /**
     * Returns the attached photos.
     *
     * @return an unmodifiable list of photos; empty if there are none
     */
    public List<Image> getPhotos() {
        return images;
    }

    /**
     * Returns the owner's reply.
     *
     * @return the reply, or {@code null} if there is none
     */
    public Reply getReply() {
        return ownerReply;
    }

    /**
     * Returns when the review was posted.
     *
     * @return the posting time
     */
    public LocalDateTime getTimePosted() {
        return postedAt;
    }

    private static void validateAuthor(final User author,
                                       final String authorDisplayName) {
        if (author == null) {
            Validate.notBlank(authorDisplayName, "a review needs either an "
                    + "in-app author or an imported author name");
        } else {
            Validate.isTrue(authorDisplayName == null, "a review cannot have "
                    + "both an in-app author and an imported author name");
        }
    }

    private static void validateRatings(final Map<Category, Integer> ratings) {
        Validate.notEmpty(ratings, "a review must rate at least one category");
        for (final Map.Entry<Category, Integer> entry : ratings.entrySet()) {
            Validate.notNull(entry.getKey(),
                    "a rated category must not be null");
            Validate.notNull(entry.getValue(),
                    "the score for %s must not be null", entry.getKey());
            final int score = entry.getValue();
            Validate.isTrue(score >= MIN_SCORE && score <= MAX_SCORE,
                    "the score for %s must be between %d and %d, but was %d",
                    entry.getKey(), MIN_SCORE, MAX_SCORE, score);
        }
    }

    private static void validatePhotos(final List<Image> photos) {
        if (photos != null) {
            Validate.noNullElements(photos,
                    "photo %d of the review must not be null");
        }
    }

    private static void validateReply(final Reply reply,
                                      final LocalDateTime timePosted) {
        if (reply != null) {
            Validate.isTrue(!reply.getTimestamp().isBefore(timePosted),
                    "a reply cannot be posted before the review it answers");
        }
    }
}
