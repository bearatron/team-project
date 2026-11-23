package entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReviewTest {

    private static final LocalDateTime POSTED = LocalDateTime.of(2026, 11, 21, 12, 0);
    private static final LocalDateTime LATER = POSTED.plusHours(1);
    private static final LocalDateTime EARLIER = POSTED.minusHours(1);

    private final User author = new User("alice", UserType.REVIEWER);
    private final Image photo = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

    /** Builds a ratings map from alternating categories and scores. */
    private static Map<Category, Integer> ratings(Object... categoryScorePairs) {
        Map<Category, Integer> map = new HashMap<>();
        for (int i = 0; i < categoryScorePairs.length; i += 2) {
            map.put((Category) categoryScorePairs[i], (Integer) categoryScorePairs[i + 1]);
        }
        return map;
    }

    private Review inApp() {
        return Review.fromInApp(author,
                ratings(Category.OVERALL, 9, Category.SERVICE, 8, Category.WAITING_TIME, 3),
                "Great tacos", List.of(photo), POSTED);
    }

    private Review googleMaps() {
        return Review.fromGoogleMaps("Sam P.", ratings(Category.OVERALL, 7),
                "Solid", null, POSTED);
    }

    // Creation

    @Test
    void inAppReviewStoresItsFields() {
        Review review = inApp();

        assertEquals(ReviewSource.APP, review.getSource());
        assertFalse(review.isImported());
        assertEquals(author, review.getAuthor());
        assertEquals("alice", review.getAuthorName());
        assertEquals("Great tacos", review.getText());
        assertEquals(List.of(photo), review.getPhotos());
        assertEquals(POSTED, review.getTimePosted());
        assertNull(review.getReply());
    }

    @Test
    void googleMapsReviewStoresItsFields() {
        Review review = googleMaps();

        assertEquals(ReviewSource.GOOGLE_MAPS, review.getSource());
        assertTrue(review.isImported());
        assertNull(review.getAuthor());
        assertEquals("Sam P.", review.getAuthorName());
        assertTrue(review.getPhotos().isEmpty());
    }

    @Test
    void publicConstructorRestoresAReviewWithAReply() {
        Reply reply = new Reply("Thanks!", LATER);
        Review review = new Review(author, null, ratings(Category.OVERALL, 9),
                null, null, reply, POSTED);

        assertEquals(reply, review.getReply());
        assertFalse(review.isImported());
    }

    @Test
    void textAndPhotosAreOptional() {
        Review review = Review.fromInApp(author, ratings(Category.OVERALL, 5),
                null, null, POSTED);

        assertNull(review.getText());
        assertTrue(review.getPhotos().isEmpty());
    }

    // Ratings

    @Test
    void allowsRatingOverallPlusASubsetOfCategories() {
        Review review = Review.fromInApp(author,
                ratings(Category.OVERALL, 6, Category.CLEANLINESS, 10), null, null, POSTED);

        assertEquals(10, review.getRating(Category.CLEANLINESS));
        assertNull(review.getRating(Category.SERVICE));
        assertEquals(2, review.getRatings().size());
    }

    @Test
    void requiresAnOverallRating() {
        assertThrows(IllegalArgumentException.class, () -> Review.fromInApp(author,
                ratings(Category.SERVICE, 8), null, null, POSTED));
        assertThrows(IllegalArgumentException.class,
                () -> Review.fromInApp(author, ratings(), null, null, POSTED));
        assertThrows(IllegalArgumentException.class,
                () -> Review.fromInApp(author, null, null, null, POSTED));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10})
    void acceptsScoresInRange(int score) {
        Review review = Review.fromInApp(author, ratings(Category.OVERALL, score),
                null, null, POSTED);

        assertEquals(score, review.getRating(Category.OVERALL));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 11, -1})
    void rejectsScoresOutOfRange(int score) {
        assertThrows(IllegalArgumentException.class, () -> Review.fromInApp(author,
                ratings(Category.OVERALL, 5, Category.CLEANLINESS, score), null, null, POSTED));
    }

    @Test
    void rejectsNullCategoriesAndScores() {
        assertThrows(IllegalArgumentException.class, () -> Review.fromInApp(author,
                ratings(Category.OVERALL, 5, null, 5), null, null, POSTED));
        assertThrows(IllegalArgumentException.class, () -> Review.fromInApp(author,
                ratings(Category.OVERALL, null), null, null, POSTED));
    }

    @Test
    void averagesTheRatedCategories() {
        Review review = Review.fromInApp(author, ratings(
                Category.OVERALL, 10, Category.FOOD_QUALITY, 8,
                Category.WAITING_TIME, 3), null, null, POSTED);

        assertEquals(7.0, review.getAverageScore(), 1e-9);
    }

    // Authors

    @Test
    void needsExactlyOneOfAuthorAndDisplayName() {
        Map<Category, Integer> valid = ratings(Category.OVERALL, 5);

        assertThrows(NullPointerException.class,
                () -> Review.fromInApp(null, valid, null, null, POSTED));
        assertThrows(IllegalArgumentException.class,
                () -> Review.fromGoogleMaps(null, valid, null, null, POSTED));
        assertThrows(IllegalArgumentException.class,
                () -> Review.fromGoogleMaps(" ", valid, null, null, POSTED));
        assertThrows(IllegalArgumentException.class,
                () -> new Review(author, "Sam", valid, null, null, null, POSTED));
        assertThrows(IllegalArgumentException.class,
                () -> new Review(null, null, valid, null, null, null, POSTED));
    }

    @Test
    void rejectsMissingTime() {
        assertThrows(NullPointerException.class, () -> Review.fromInApp(author,
                ratings(Category.OVERALL, 5), null, null, null));
    }

    @Test
    void ownershipIsCheckedAgainstTheAuthor() {
        Review review = inApp();

        assertTrue(review.isAuthoredBy(new User("alice", UserType.REVIEWER)));
        assertFalse(review.isAuthoredBy(new User("bob", UserType.REVIEWER)));
        assertFalse(review.isAuthoredBy(null));
        assertFalse(googleMaps().isAuthoredBy(author));
    }

    // Photos

    @Test
    void rejectsNullPhotos() {
        assertThrows(IllegalArgumentException.class, () -> Review.fromInApp(author,
                ratings(Category.OVERALL, 5), null, Arrays.asList(photo, null), POSTED));
    }

    @Test
    void returnedCollectionsCannotBeChanged() {
        Review review = inApp();

        assertThrows(UnsupportedOperationException.class,
                () -> review.getRatings().put(Category.CLEANLINESS, 1));
        assertThrows(UnsupportedOperationException.class,
                () -> review.getPhotos().add(photo));
    }

    // Replies

    @Test
    void withReplyAddsOrReplacesTheOnlyReply() {
        Reply first = new Reply("Thanks!", LATER);
        Reply second = new Reply("Thanks again!", LATER.plusMinutes(5));

        Review replied = inApp().withReply(first);
        assertEquals(first, replied.getReply());
        assertEquals(second, replied.withReply(second).getReply());
    }

    @Test
    void withReplyKeepsEverythingElseAndLeavesTheOriginalUnchanged() {
        Review original = inApp();
        Review replied = original.withReply(new Reply("Thanks!", LATER));

        assertNull(original.getReply());
        assertEquals(original.getRatings(), replied.getRatings());
        assertEquals(original.getText(), replied.getText());
        assertEquals(original.getPhotos(), replied.getPhotos());
        assertEquals(original.getTimePosted(), replied.getTimePosted());
        assertEquals("alice", replied.getAuthorName());
        assertTrue(replied.isAuthoredBy(author));
    }

    @Test
    void googleMapsReviewsKeepTheirNameWhenRepliedTo() {
        Review review = googleMaps().withReply(new Reply("Thanks!", LATER));

        assertTrue(review.isImported());
        assertEquals("Sam P.", review.getAuthorName());
        assertEquals("Thanks!", review.getReply().getText());
    }

    @Test
    void replyCannotPredateTheReview() {
        assertThrows(IllegalArgumentException.class,
                () -> inApp().withReply(new Reply("Thanks!", EARLIER)));
    }

    @Test
    void withReplyRejectsNull() {
        NullPointerException e = assertThrows(NullPointerException.class,
                () -> inApp().withReply(null));
        assertTrue(e.getMessage().contains("withoutReply()"));
    }

    @Test
    void withoutReplyRemovesTheReply() {
        Review review = inApp().withReply(new Reply("Thanks!", LATER)).withoutReply();

        assertNull(review.getReply());
        assertSame(ReviewSource.APP, review.getSource());
    }
}
