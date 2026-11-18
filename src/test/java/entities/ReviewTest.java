package entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ReviewTest {

    private static final LocalDateTime POSTED = LocalDateTime.of(2026, 11, 17, 12, 0);
    private static final LocalDateTime LATER = POSTED.plusHours(1);
    private static final LocalDateTime EARLIER = POSTED.minusHours(1);

    private final User author = new User();

    private static List<Rating> ratings(Rating... ratings) {
        return Arrays.asList(ratings);
    }

    private Review posted() {
        return Review.posted(author,
                ratings(Rating.of(Category.SERVICE, 4), Rating.of(Category.WAITING_TIME, 2)),
                "Great tacos", List.of("photos/taco.jpg"), POSTED);
    }

    // Creation and validation

    @Test
    void postedReviewStoresItsFields() {
        Review review = posted();

        assertEquals(ReviewSource.APP, review.getSource());
        assertEquals(Optional.of(author), review.getAuthor());
        assertEquals(Optional.of("Great tacos"), review.getText());
        assertEquals(List.of("photos/taco.jpg"), review.getPhotoPaths());
        assertEquals(POSTED, review.getTimestamp());
        assertFalse(review.isEdited());
        assertEquals(Optional.empty(), review.getEditedTimestamp());
        assertEquals(Optional.empty(), review.getReply());
    }

    @Test
    void allowsRatingASubsetOfCategories() {
        Review review = Review.posted(author, ratings(Rating.of(Category.CLEANLINESS, 5)),
                null, null, POSTED);

        assertEquals(Optional.of(Rating.of(Category.CLEANLINESS, 5)),
                review.getRating(Category.CLEANLINESS));
        assertEquals(Optional.empty(), review.getRating(Category.SERVICE));
        assertEquals(1, review.getRatings().size());
    }

    @Test
    void requiresAtLeastOneRating() {
        assertThrows(IllegalArgumentException.class,
                () -> Review.posted(author, Collections.emptyList(), null, null, POSTED));
    }

    @Test
    void rejectsDuplicateCategories() {
        assertThrows(IllegalArgumentException.class, () -> Review.posted(author,
                ratings(Rating.of(Category.SPEED, 2), Rating.of(Category.SPEED, 5)),
                null, null, POSTED));
    }

    @Test
    void rejectsNullRequiredArguments() {
        List<Rating> valid = ratings(Rating.of(Category.OVERALL, 3));

        assertThrows(NullPointerException.class,
                () -> Review.posted(null, valid, null, null, POSTED));
        assertThrows(NullPointerException.class,
                () -> Review.posted(author, null, null, null, POSTED));
        assertThrows(NullPointerException.class,
                () -> Review.posted(author, ratings((Rating) null), null, null, POSTED));
        assertThrows(NullPointerException.class,
                () -> Review.posted(author, valid, null, null, null));
    }

    @Test
    void blankTextMeansNoText() {
        Review review = Review.posted(author, ratings(Rating.of(Category.OVERALL, 3)),
                "  ", null, POSTED);

        assertEquals(Optional.empty(), review.getText());
    }

    @Test
    void missingPhotosMeansEmptyList() {
        Review review = Review.posted(author, ratings(Rating.of(Category.OVERALL, 3)),
                null, null, POSTED);

        assertTrue(review.getPhotoPaths().isEmpty());
    }

    @Test
    void rejectsNullOrBlankPhotoPaths() {
        List<Rating> valid = ratings(Rating.of(Category.OVERALL, 3));

        assertThrows(NullPointerException.class, () -> Review.posted(author, valid, null,
                Arrays.asList("a.jpg", null), POSTED));
        assertThrows(IllegalArgumentException.class, () -> Review.posted(author, valid, null,
                List.of(" "), POSTED));
    }

    // Immutability

    @Test
    void changingInputListsDoesNotChangeReview() {
        List<Rating> inputRatings = new ArrayList<>(ratings(Rating.of(Category.OVERALL, 3)));
        List<String> inputPhotos = new ArrayList<>(List.of("a.jpg"));
        Review review = Review.posted(author, inputRatings, null, inputPhotos, POSTED);

        inputRatings.add(Rating.of(Category.SPEED, 1));
        inputPhotos.add("b.jpg");

        assertEquals(1, review.getRatings().size());
        assertEquals(List.of("a.jpg"), review.getPhotoPaths());
    }

    @Test
    void returnedCollectionsCannotBeChanged() {
        Review review = posted();

        assertThrows(UnsupportedOperationException.class,
                () -> review.getRatings().put(Category.SPEED, Rating.of(Category.SPEED, 1)));
        assertThrows(UnsupportedOperationException.class,
                () -> review.getPhotoPaths().add("b.jpg"));
    }

    // Scores

    @Test
    void averagesOnlyTheRatedCategories() {
        Review review = Review.posted(author, ratings(
                Rating.of(Category.SERVICE, 5),
                Rating.of(Category.FOOD_QUALITY, 4),
                Rating.of(Category.WAITING_TIME, 2)), null, null, POSTED);

        assertEquals(11.0 / 3, review.getAverageScore(), 1e-9);
    }

    @Test
    void ratingsAreReturnedInCategoryOrder() {
        Review review = Review.posted(author, ratings(
                Rating.of(Category.SPEED, 1),
                Rating.of(Category.OVERALL, 2)), null, null, POSTED);

        assertEquals(List.of(Category.OVERALL, Category.SPEED),
                new ArrayList<>(review.getRatings().keySet()));
    }

    // Privacy

    @Test
    void postedReviewsShowAnonymousName() {
        assertEquals(Review.ANONYMOUS, posted().getPublicAuthorName());
    }

    @Test
    void importedReviewsShowTheirDisplayName() {
        Review review = Review.imported("Sam from Google",
                ratings(Rating.of(Category.OVERALL, 4)), "Nice", null, POSTED);

        assertEquals("Sam from Google", review.getPublicAuthorName());
        assertEquals(ReviewSource.GOOGLE_MAPS, review.getSource());
        assertEquals(Optional.empty(), review.getAuthor());
    }

    @Test
    void importedReviewsNeedADisplayName() {
        List<Rating> valid = ratings(Rating.of(Category.OVERALL, 4));

        assertThrows(NullPointerException.class,
                () -> Review.imported(null, valid, null, null, POSTED));
        assertThrows(IllegalArgumentException.class,
                () -> Review.imported(" ", valid, null, null, POSTED));
    }

    @Test
    void ownershipIsCheckedAgainstTheAuthor() {
        Review review = posted();

        assertTrue(review.isAuthoredBy(author));
        assertFalse(review.isAuthoredBy(new User()));
        assertFalse(review.isAuthoredBy(null));
    }

    @Test
    void importedReviewsHaveNoInAppAuthor() {
        Review review = Review.imported("Sam", ratings(Rating.of(Category.OVERALL, 4)),
                null, null, POSTED);

        assertFalse(review.isAuthoredBy(author));
    }

    // Editing

    @Test
    void editReplacesContentAndKeepsIdentity() {
        Review original = posted();
        Review edited = original.edit(ratings(Rating.of(Category.OVERALL, 1)),
                "Changed my mind", null, LATER);

        assertNotSame(original, edited);
        assertTrue(edited.isAuthoredBy(author));
        assertEquals(POSTED, edited.getTimestamp());
        assertEquals(Optional.of(LATER), edited.getEditedTimestamp());
        assertTrue(edited.isEdited());
        assertEquals(Optional.of("Changed my mind"), edited.getText());
        assertEquals(1, edited.getRatings().size());
        assertTrue(edited.getPhotoPaths().isEmpty());
    }

    @Test
    void editLeavesTheOriginalUnchanged() {
        Review original = posted();
        original.edit(ratings(Rating.of(Category.OVERALL, 1)), "Changed", null, LATER);

        assertFalse(original.isEdited());
        assertEquals(Optional.of("Great tacos"), original.getText());
        assertEquals(2, original.getRatings().size());
    }

    @Test
    void editValidatesNewContent() {
        assertThrows(IllegalArgumentException.class,
                () -> posted().edit(Collections.emptyList(), null, null, LATER));
    }

    @Test
    void editCannotBeBeforeThePostOrThePreviousEdit() {
        Review review = posted();
        List<Rating> valid = ratings(Rating.of(Category.OVERALL, 3));

        assertThrows(IllegalArgumentException.class,
                () -> review.edit(valid, null, null, EARLIER));
        Review edited = review.edit(valid, null, null, LATER.plusHours(1));
        assertThrows(IllegalArgumentException.class,
                () -> edited.edit(valid, null, null, LATER));
    }

    @Test
    void importedReviewsCannotBeEdited() {
        Review review = Review.imported("Sam", ratings(Rating.of(Category.OVERALL, 4)),
                null, null, POSTED);

        assertThrows(IllegalStateException.class, () -> review.edit(
                ratings(Rating.of(Category.OVERALL, 1)), null, null, LATER));
    }

    @Test
    void editKeepsTheReply() {
        Reply reply = new Reply("Thanks!", LATER);
        Review edited = posted().withReply(reply)
                .edit(ratings(Rating.of(Category.OVERALL, 2)), null, null, LATER);

        assertEquals(Optional.of(reply), edited.getReply());
    }

    // Replies

    @Test
    void withReplyAddsOrReplacesTheOnlyReply() {
        Reply first = new Reply("Thanks!", LATER);
        Reply second = new Reply("Thanks again!", LATER.plusMinutes(5));

        Review replied = posted().withReply(first);
        assertEquals(Optional.of(first), replied.getReply());
        assertEquals(Optional.of(second), replied.withReply(second).getReply());
    }

    @Test
    void withReplyKeepsEverythingElse() {
        Review original = posted();
        Review replied = original.withReply(new Reply("Thanks!", LATER));

        assertEquals(Optional.empty(), original.getReply());
        assertEquals(original.getRatings(), replied.getRatings());
        assertEquals(original.getText(), replied.getText());
        assertEquals(original.getPhotoPaths(), replied.getPhotoPaths());
        assertEquals(original.getTimestamp(), replied.getTimestamp());
        assertTrue(replied.isAuthoredBy(author));
    }

    @Test
    void importedReviewsCanBeRepliedTo() {
        Review review = Review.imported("Sam", ratings(Rating.of(Category.OVERALL, 4)),
                null, null, POSTED).withReply(new Reply("Thanks!", LATER));

        assertEquals("Sam", review.getPublicAuthorName());
        assertTrue(review.getReply().isPresent());
    }

    @Test
    void replyCannotPredateTheReview() {
        assertThrows(IllegalArgumentException.class,
                () -> posted().withReply(new Reply("Thanks!", EARLIER)));
    }

    @Test
    void withoutReplyRemovesTheReply() {
        Review review = posted().withReply(new Reply("Thanks!", LATER)).withoutReply();

        assertEquals(Optional.empty(), review.getReply());
    }
}
