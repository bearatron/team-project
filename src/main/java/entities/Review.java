package entities;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.awt.Image;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A review of a food truck.
 *
 * <p>An in-app review scores every {@link Category}; a review imported from
 * Google Maps must score {@link Category#OVERALL} and may leave the others
 * blank. A review may include text, photos and, once set, one reply from the
 * truck's owner that cannot be replaced.
 */
public final class Review {

    /** The lowest score a category can be given. */
    public static final int MIN_SCORE = 1;

    /** The highest score a category can be given. */
    public static final int MAX_SCORE = 5;

    private final User author;
    private final String authorName;
    private final ReviewSource source;
    private final Map<Category, Integer> ratings;
    private final String text;
    private final List<Image> photos;
    private Reply reply;
    private final LocalDateTime timePosted;

    /**
     * Creates a review. Use {@link #fromInApp} or {@link #fromGoogleMaps} for
     * new reviews; this constructor also restores a stored review that may
     * already have a reply.
     *
     * <p>Give exactly one of {@code author} and {@code authorDisplayName}. An
     * {@code author} makes this an in-app review whose author name is the
     * author's username; an {@code authorDisplayName} makes it an imported
     * Google Maps review.
     *
     * @param author the in-app author, or {@code null} for imported reviews
     * @param authorDisplayName the author's name on Google Maps, or
     *     {@code null} for in-app reviews
     * @param ratings a score from {@value #MIN_SCORE} to {@value #MAX_SCORE}
     *     for every category in an in-app review, or at least for
     *     {@link Category#OVERALL} in an imported review
     * @param text the review text, or {@code null} for none
     * @param photos the attached photos, or {@code null} for none
     * @param reply the owner's reply, or {@code null} for none
     * @param timePosted when the review was posted; not {@code null}
     * @throws NullPointerException if {@code timePosted} is {@code null}
     * @throws IllegalArgumentException if any other argument is invalid
     */
    public Review(User author, String authorDisplayName,
                  Map<Category, Integer> ratings, String text,
                  List<Image> photos, Reply reply, LocalDateTime timePosted) {
        Validate.notNull(timePosted, "timePosted must be non-null");
        Validate.isTrue(hasValidAuthor(author, authorDisplayName),
                "give exactly one of an in-app author or a non-blank "
                        + "authorDisplayName for an imported review");
        Validate.isTrue(hasValidRatings(ratings, author != null),
                "an in-app review must rate every category and an imported "
                        + "review must rate OVERALL, with every score between "
                        + "%d and %d", MIN_SCORE, MAX_SCORE);
        Validate.isTrue(hasValidPhotos(photos),
                "photos must not contain null; pass null or an empty list "
                        + "for a review without photos");
        Validate.isTrue(hasValidReply(reply, timePosted),
                "the reply cannot be posted before the review it answers");

        this.author = author;
        this.authorName = author != null ? author.getUserName() : authorDisplayName;
        this.source = author != null ? ReviewSource.APP : ReviewSource.GOOGLE_MAPS;
        this.ratings = ratings;
        this.text = text;
        this.photos = photos != null ? photos : List.of();
        this.reply = reply;
        this.timePosted = timePosted;
    }

    /**
     * Creates a review posted by a reviewer in this app.
     *
     * @param author the reviewer; not {@code null}
     * @param ratings a score from {@value #MIN_SCORE} to {@value #MAX_SCORE}
     *     for every category
     * @param text the review text, or {@code null} for none
     * @param photos the attached photos, or {@code null} for none
     * @param timePosted when the review was posted; not {@code null}
     * @return the new review
     */
    public static Review fromInApp(User author, Map<Category, Integer> ratings,
                                   String text, List<Image> photos,
                                   LocalDateTime timePosted) {
        Validate.notNull(author, "author must be non-null for an in-app review; "
                + "use fromGoogleMaps() for an imported review");
        return new Review(author, null, ratings, text, photos, null, timePosted);
    }

    /**
     * Creates a review imported from Google Maps.
     *
     * @param authorDisplayName the name shown on Google Maps; not blank
     * @param ratings a score from {@value #MIN_SCORE} to {@value #MAX_SCORE}
     *     for {@link Category#OVERALL}, and optionally for other categories
     * @param text the review text, or {@code null} for none
     * @param photos the attached photos, or {@code null} for none
     * @param timePosted when the review was originally posted; not
     *     {@code null}
     * @return the new review
     */
    public static Review fromGoogleMaps(String authorDisplayName,
                                        Map<Category, Integer> ratings,
                                        String text, List<Image> photos,
                                        LocalDateTime timePosted) {
        return new Review(null, authorDisplayName, ratings, text, photos, null,
                timePosted);
    }

    /**
     * Returns the owner's reply.
     *
     * @return the reply, or {@code null} if there is none yet
     */
    public Reply getReply() {
        return reply;
    }

    /**
     * Sets the owner's reply. A review has at most one reply, and once set it
     * cannot be replaced.
     *
     * @param reply the owner's reply; not {@code null} and not posted before
     *     this review
     * @throws NullPointerException if {@code reply} is {@code null}
     * @throws IllegalStateException if this review already has a reply
     * @throws IllegalArgumentException if {@code reply} was posted before this
     *     review
     */
    public void setReply(Reply reply) {
        Validate.notNull(reply, "reply must be non-null");
        Validate.validState(this.reply == null,
                "this review already has a reply, and replies cannot be replaced");
        Validate.isTrue(hasValidReply(reply, timePosted),
                "the reply cannot be posted before the review it answers");
        this.reply = reply;
    }

    /**
     * Returns whether this review was imported rather than posted in the app.
     *
     * @return {@code true} for imported Google Maps reviews, {@code false} for
     *     in-app reviews
     */
    public boolean isImported() {
        return source == ReviewSource.GOOGLE_MAPS;
    }

    /**
     * Returns whether {@code user} wrote this review in the app.
     *
     * @param user the user to check; may be {@code null}
     * @return {@code true} if {@code user} is this review's in-app author;
     *     always {@code false} for imported reviews
     */
    public boolean isAuthoredBy(User user) {
        return author != null && author.equals(user);
    }

    /**
     * Returns the in-app author.
     *
     * @return the author, or {@code null} for imported reviews
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Returns the author's name: the username for in-app reviews, or the
     * Google Maps display name for imported reviews.
     *
     * @return the author's name; never blank
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Returns where this review came from.
     *
     * @return the review's source
     */
    public ReviewSource getSource() {
        return source;
    }

    /**
     * Returns the score for each rated category.
     *
     * @return an unmodifiable view of the scores by category
     */
    public Map<Category, Integer> getRatings() {
        return Collections.unmodifiableMap(ratings);
    }

    /**
     * Returns the score for one category.
     *
     * @param category the category to look up
     * @return the score, or {@code null} if the category was not rated
     */
    public Integer getRating(Category category) {
        return ratings.get(category);
    }

    /**
     * Returns the review text.
     *
     * @return the text, or {@code null} if there is none
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the attached photos.
     *
     * @return an unmodifiable view of the photos; empty if there are none
     */
    public List<Image> getPhotos() {
        return Collections.unmodifiableList(photos);
    }

    /**
     * Returns when the review was posted.
     *
     * @return the posting time
     */
    public LocalDateTime getTimePosted() {
        return timePosted;
    }

    /**
     * Checks that exactly one of an in-app author and an imported display
     * name is given, and that a display name is not blank.
     *
     * @param author the in-app author, or {@code null}
     * @param authorDisplayName the imported display name, or {@code null}
     * @return {@code true} if the author information is valid
     */
    private static boolean hasValidAuthor(User author, String authorDisplayName) {
        return (author != null || StringUtils.isNotBlank(authorDisplayName))
                && (author == null || authorDisplayName == null);
    }

    /**
     * Checks that the required categories are rated, and that every category
     * and score is non-null with each score from {@value #MIN_SCORE} to
     * {@value #MAX_SCORE}. An in-app review must rate every category; an
     * imported review must rate at least {@link Category#OVERALL}.
     *
     * @param ratings the ratings to check; may be {@code null}
     * @param inApp whether the review was posted in the app
     * @return {@code true} if the ratings are valid
     */
    private static boolean hasValidRatings(Map<Category, Integer> ratings,
                                           boolean inApp) {
        if (ratings == null || !ratings.containsKey(Category.OVERALL)) {
            return false;
        }
        if (inApp && ratings.size() != Category.values().length) {
            return false;
        }
        for (Map.Entry<Category, Integer> entry : ratings.entrySet()) {
            Integer score = entry.getValue();
            if (entry.getKey() == null || score == null
                    || score < MIN_SCORE || score > MAX_SCORE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks that the photo list, if given, contains no {@code null}
     * elements.
     *
     * @param photos the photos to check; may be {@code null}
     * @return {@code true} if the photos are valid
     */
    private static boolean hasValidPhotos(List<Image> photos) {
        // Lists from List.of() throw on contains(null), so check each element.
        return photos == null || photos.stream().noneMatch(Objects::isNull);
    }

    /**
     * Checks that a reply, if given, was not posted before the review.
     *
     * @param reply the reply to check; may be {@code null}
     * @param timePosted when the review was posted; not {@code null}
     * @return {@code true} if the reply is valid
     */
    private static boolean hasValidReply(Reply reply, LocalDateTime timePosted) {
        return reply == null || !reply.getTimestamp().isBefore(timePosted);
    }
}
