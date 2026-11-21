package entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final Image photo = new Image("photos/taco.jpg", "Fish taco");

    private static Map<Category, Integer> ratings(Object... categoryScorePairs) {
        Map<Category, Integer> map = new HashMap<>();
        for (int i = 0; i < categoryScorePairs.length; i += 2) {
            map.put((Category) categoryScorePairs[i], (Integer) categoryScorePairs[i + 1]);
        }
        return map;
    }

    private Review inApp() {
        return Review.fromInApp(author,
                ratings(Category.SERVICE, 8, Category.WAITING_TIME, 3),
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
        assertEquals(author, review.getAuthor());
        assertNull(review.getAuthorDisplayName());
        assertEquals("Great tacos", review.getText());
        assertEquals(List.of(photo), review.getPhotos());
        assertEquals(POSTED, review.getTimePosted());
        assertNull(review.getReply());
    }

    @Test
    void googleMapsReviewStoresItsFields() {
        Review review = googleMaps();

        assertEquals(ReviewSource.GOOGLE_MAPS, review.getSource());
        assertNull(review.getAuthor());
        assertEquals("Sam P.", review.getAuthorDisplayName());
        assertTrue(review.getPhotos().isEmpty());
    }

    @Test
    void publicConstructorRestoresAReviewWithAReply() {
        Reply reply = new Reply("Thanks!", LATER);
        Review review = new Review(author, null, ratings(Category.OVERALL, 9),
                null, null, reply, POSTED);

        assertEquals(ReviewSource.APP, review.getSource());

        assertEquals(reply, review.getReply());
    }

    @Test
    void textIsOptional() {
        Review review = Review.fromInApp(author, ratings(Category.OVERALL, 5),
                null, null, POSTED);

        assertNull(review.getText());
        assertTrue(review.getPhotos().isEmpty());
    }

    // Ratings

    @Test
    void allowsRatingASubsetOfCategories() {
        Review review = Review.fromInApp(author, ratings(Category.CLEANLINESS, 10),
                null, null, POSTED);

        assertEquals(10, review.getRating(Category.CLEANLINESS));
        assertNull(review.getRating(Category.SERVICE));
        assertEquals(1, review.getRatings().size());
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
                ratings(Category.OVERALL, score), null, null, POSTED));
    }

    @Test
    void requiresAtLeastOneRating() {
        assertThrows(IllegalArgumentException.class,
                () -> Review.fromInApp(author, ratings(), null, null, POSTED));
    }

    @Test
    void rejectsNullRatingsCategoriesAndScores() {
        assertThrows(NullPointerException.class,
                () -> Review.fromInApp(author, null, null, null, POSTED));
        assertThrows(NullPointerException.class,
                () -> Review.fromInApp(author, ratings(null, 5), null, null, POSTED));
        assertThrows(NullPointerException.class, () -> Review.fromInApp(author,
                ratings(Category.SPEED, null), null, null, POSTED));
    }

    @Test
    void averagesOnlyTheRatedCategories() {
        Review review = Review.fromInApp(author, ratings(
                Category.SERVICE, 10, Category.FOOD_QUALITY, 8,
                Category.WAITING_TIME, 3), null, null, POSTED);

        assertEquals(7.0, review.getAverageScore(), 1e-9);
    }

    @Test
    void ratingsAreReturnedInCategoryOrder() {
        Review review = Review.fromInApp(author, ratings(
                Category.SPEED, 1, Category.OVERALL, 2), null, null, POSTED);

        assertEquals(List.of(Category.OVERALL, Category.SPEED),
                new ArrayList<>(review.getRatings().keySet()));
    }

    // Authors

    @Test
    void sourceFollowsFromTheAuthor() {
        Map<Category, Integer> valid = ratings(Category.OVERALL, 5);

        assertEquals(ReviewSource.APP, new Review(author, null, valid, null,
                null, null, POSTED).getSource());
        assertEquals(ReviewSource.GOOGLE_MAPS, new Review(null, "Sam", valid,
                null, null, null, POSTED).getSource());
    }

    @Test
    void needsExactlyOneOfAuthorAndImportedName() {
        Map<Category, Integer> valid = ratings(Category.OVERALL, 5);

        assertThrows(NullPointerException.class,
                () -> Review.fromInApp(null, valid, null, null, POSTED));
        assertThrows(NullPointerException.class,
                () -> Review.fromGoogleMaps(null, valid, null, null, POSTED));
        assertThrows(IllegalArgumentException.class,
                () -> Review.fromGoogleMaps(" ", valid, null, null, POSTED));
        assertThrows(IllegalArgumentException.class, () -> new Review(author,
                "Sam", valid, null, null, null, POSTED));
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

    // Photos and immutability

    @Test
    void rejectsNullPhotos() {
        assertThrows(IllegalArgumentException.class, () -> Review.fromInApp(author,
                ratings(Category.OVERALL, 5), null, Arrays.asList(photo, null), POSTED));
    }

    @Test
    void changingInputsDoesNotChangeTheReview() {
        Map<Category, Integer> inputRatings = ratings(Category.OVERALL, 5);
        List<Image> inputPhotos = new ArrayList<>(List.of(photo));
        Review review = Review.fromInApp(author, inputRatings, null, inputPhotos, POSTED);

        inputRatings.put(Category.SPEED, 1);
        inputPhotos.add(new Image("photos/other.jpg", null));

        assertEquals(1, review.getRatings().size());
        assertEquals(1, review.getPhotos().size());
    }

    @Test
    void returnedCollectionsCannotBeChanged() {
        Review review = inApp();

        assertThrows(UnsupportedOperationException.class,
                () -> review.getRatings().put(Category.SPEED, 1));
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
        assertTrue(replied.isAuthoredBy(author));
    }

    @Test
    void googleMapsReviewsCanBeRepliedTo() {
        Review review = googleMaps().withReply(new Reply("Thanks!", LATER));

        assertEquals("Sam P.", review.getAuthorDisplayName());
        assertEquals("Thanks!", review.getReply().getText());
    }

    @Test
    void replyCannotPredateTheReview() {
        assertThrows(IllegalArgumentException.class,
                () -> inApp().withReply(new Reply("Thanks!", EARLIER)));
    }

    @Test
    void withReplyRejectsNull() {
        assertThrows(NullPointerException.class, () -> inApp().withReply(null));
    }

    @Test
    void withoutReplyRemovesTheReply() {
        Review review = inApp().withReply(new Reply("Thanks!", LATER)).withoutReply();

        assertNull(review.getReply());
    }
}
