package entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.junit.jupiter.params.provider.EnumSource;
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

    /** Builds ratings that give every category the same score. */
    private static Map<Category, Integer> allCategories(int score) {
        Map<Category, Integer> map = new HashMap<>();
        for (Category category : Category.values()) {
            map.put(category, score);
        }
        return map;
    }

    private Review inApp() {
        return Review.fromInApp(author, ratings(Category.OVERALL, 5, Category.SERVICE, 4,
                Category.FOOD_QUALITY, 5, Category.VALUE_FOR_MONEY, 3),
                "Great tacos", List.of(photo), POSTED);
    }

    private Review googleMaps() {
        return Review.fromGoogleMaps("Sam P.", ratings(Category.OVERALL, 4),
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
        assertEquals(4, review.getRating(Category.SERVICE));
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
        assertEquals(4, review.getRating(Category.OVERALL));
        assertNull(review.getRating(Category.SERVICE));
        assertTrue(review.getPhotos().isEmpty());
    }

    @Test
    void publicConstructorRestoresAReviewWithAReply() {
        Reply reply = new Reply("Thanks!", LATER);
        Review review = new Review(author, null, allCategories(5), null, null, reply, POSTED);

        assertEquals(reply, review.getReply());
        assertFalse(review.isImported());
    }

    @Test
    void textAndPhotosAreOptional() {
        Review review = Review.fromInApp(author, allCategories(3), null, null, POSTED);

        assertNull(review.getText());
        assertTrue(review.getPhotos().isEmpty());
    }

    // Ratings

    @ParameterizedTest
    @EnumSource(Category.class)
    void inAppReviewMustRateEveryCategory(Category missing) {
        Map<Category, Integer> ratings = allCategories(3);
        ratings.remove(missing);

        assertThrows(IllegalArgumentException.class,
                () -> Review.fromInApp(author, ratings, null, null, POSTED));
    }

    @Test
    void googleMapsReviewNeedsOnlyOverall() {
        assertEquals(1, googleMaps().getRatings().size());
        assertThrows(IllegalArgumentException.class, () -> Review.fromGoogleMaps("Sam",
                ratings(Category.SERVICE, 4), null, null, POSTED));
    }

    @Test
    void googleMapsReviewMayRateOtherCategoriesToo() {
        Review review = Review.fromGoogleMaps("Sam", allCategories(2), null, null, POSTED);

        assertEquals(Category.values().length, review.getRatings().size());
    }

    @Test
    void rejectsMissingOrEmptyRatings() {
        assertThrows(IllegalArgumentException.class,
                () -> Review.fromInApp(author, ratings(), null, null, POSTED));
        assertThrows(IllegalArgumentException.class,
                () -> Review.fromInApp(author, null, null, null, POSTED));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5})
    void acceptsScoresInRange(int score) {
        Review review = Review.fromInApp(author, allCategories(score), null, null, POSTED);

        assertEquals(score, review.getRating(Category.OVERALL));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 6, 10, -1})
    void rejectsScoresOutOfRange(int score) {
        Map<Category, Integer> ratings = allCategories(3);
        ratings.put(Category.VALUE_FOR_MONEY, score);

        assertThrows(IllegalArgumentException.class,
                () -> Review.fromInApp(author, ratings, null, null, POSTED));
    }

    @Test
    void rejectsNullCategoriesAndScores() {
        Map<Category, Integer> nullScore = allCategories(3);
        nullScore.put(Category.SERVICE, null);
        Map<Category, Integer> nullCategory = ratings(Category.OVERALL, 4, null, 4);

        assertThrows(IllegalArgumentException.class,
                () -> Review.fromInApp(author, nullScore, null, null, POSTED));
        assertThrows(IllegalArgumentException.class,
                () -> Review.fromGoogleMaps("Sam", nullCategory, null, null, POSTED));
    }

    // Authors

    @Test
    void needsExactlyOneOfAuthorAndDisplayName() {
        Map<Category, Integer> valid = allCategories(3);

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
        assertThrows(NullPointerException.class,
                () -> Review.fromInApp(author, allCategories(3), null, null, null));
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
                allCategories(3), null, Arrays.asList(photo, null), POSTED));
    }

    @Test
    void returnedCollectionsCannotBeChanged() {
        Review review = inApp();

        assertThrows(UnsupportedOperationException.class,
                () -> review.getRatings().put(Category.SERVICE, 1));
        assertThrows(UnsupportedOperationException.class,
                () -> review.getPhotos().add(photo));
    }

    // Replies

    @Test
    void setReplyAddsTheReply() {
        Review review = inApp();
        Reply reply = new Reply("Thanks!", LATER);

        review.setReply(reply);

        assertEquals(reply, review.getReply());
    }

    @Test
    void replyCannotBeReplaced() {
        Review review = inApp();
        Reply first = new Reply("Thanks!", LATER);
        review.setReply(first);

        assertThrows(IllegalStateException.class,
                () -> review.setReply(new Reply("Thanks again!", LATER.plusMinutes(5))));
        assertEquals(first, review.getReply());
    }

    @Test
    void restoredReplyCannotBeReplacedEither() {
        Review review = new Review(author, null, allCategories(5), null, null,
                new Reply("Thanks!", LATER), POSTED);

        assertThrows(IllegalStateException.class,
                () -> review.setReply(new Reply("Changed", LATER.plusMinutes(5))));
    }

    @Test
    void googleMapsReviewsCanBeRepliedTo() {
        Review review = googleMaps();

        review.setReply(new Reply("Thanks!", LATER));

        assertEquals("Thanks!", review.getReply().getText());
        assertEquals("Sam P.", review.getAuthorName());
    }

    @Test
    void replyCannotPredateTheReview() {
        Review review = inApp();

        assertThrows(IllegalArgumentException.class,
                () -> review.setReply(new Reply("Thanks!", EARLIER)));
        assertNull(review.getReply());
    }

    @Test
    void setReplyRejectsNull() {
        assertThrows(NullPointerException.class, () -> inApp().setReply(null));
    }
}
