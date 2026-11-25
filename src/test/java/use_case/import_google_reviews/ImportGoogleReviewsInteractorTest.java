package use_case.import_google_reviews;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data_access.InMemoryReviewDataAccessObject;
import entities.Category;
import entities.FoodTruck;
import entities.Location;
import entities.Review;
import entities.User;
import entities.UserType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImportGoogleReviewsInteractorTest {

    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2026, 11, 1, 9, 30);

    private FoodTruck truck;
    private CountingDataAccess dataAccess;
    private FakeGateway gateway;
    private FakePresenter presenter;
    private ImportGoogleReviewsInteractor interactor;

    @BeforeEach
    void setUp() {
        truck = new FoodTruck("Taco Truck", new Location(43.66, -79.39),
                List.of("Mexican"), List.of());
        dataAccess = new CountingDataAccess();
        dataAccess.addUser(new User("olivia", UserType.OWNER));
        dataAccess.addUser(new User("alice", UserType.REVIEWER));
        dataAccess.addTruck(truck, "olivia");
        gateway = new FakeGateway();
        presenter = new FakePresenter();
        interactor = new ImportGoogleReviewsInteractor(dataAccess, gateway, presenter);
    }

    private static GoogleMapsReviewData googleReview(String name, int rating, int day) {
        return new GoogleMapsReviewData(name, rating, "Text from " + name,
                TEST_DATE.withDayOfMonth(day));
    }

    private void importAs(String username) {
        interactor.execute(new ImportGoogleReviewsInputData(username, "listing-123"));
    }

    // Main flow

    @Test
    void importsReviewsWithOnlyAnOverallRating() {
        gateway.reviews.add(googleReview("Sam", 4, 1));
        gateway.reviews.add(googleReview("Kai", 2, 2));

        importAs("olivia");

        assertNull(presenter.error);
        assertEquals(2, presenter.output.getImportedCount());
        assertEquals(0, presenter.output.getSkippedCount());
        assertEquals("Taco Truck", presenter.output.getTruckName());
        assertEquals("listing-123", gateway.requestedListing);
        assertEquals(1, dataAccess.saves);

        final Review review = truck.getReviews().get(0);
        assertTrue(review.isImported());
        assertEquals("Sam", review.getAuthorName());
        assertEquals(Map.of(Category.OVERALL, 4), review.getRatings());
        assertEquals("Text from Sam", review.getText());
        assertEquals(TEST_DATE.withDayOfMonth(1), review.getTimePosted());
    }

    @Test
    void importsAtMostTheGoogleLimit() {
        for (int day = 1; day <= ImportGoogleReviewsInteractor.GOOGLE_IMPORT_MAX + 2; day++) {
            gateway.reviews.add(googleReview("Person " + day, 5, day));
        }

        importAs("olivia");

        assertEquals(ImportGoogleReviewsInteractor.GOOGLE_IMPORT_MAX,
                presenter.output.getImportedCount());
        assertEquals(ImportGoogleReviewsInteractor.GOOGLE_IMPORT_MAX, truck.getReviews().size());
    }

    @Test
    void listingWithNoReviewsImportsNothing() {
        importAs("olivia");

        assertNull(presenter.error);
        assertEquals(0, presenter.output.getImportedCount());
        assertEquals(0, dataAccess.saves);
    }

    // Duplicates and bad data

    @Test
    void skipsReviewsThatWereAlreadyImported() {
        gateway.reviews.add(googleReview("Sam", 4, 1));
        importAs("olivia");
        gateway.reviews.add(googleReview("Kai", 3, 2));

        importAs("olivia");

        assertEquals(1, presenter.output.getImportedCount());
        assertEquals(1, presenter.output.getSkippedCount());
        assertEquals(2, truck.getReviews().size());
    }

    @Test
    void skipsDuplicatesWithinOneImport() {
        gateway.reviews.add(googleReview("Sam", 4, 1));
        gateway.reviews.add(googleReview("Sam", 4, 1));

        importAs("olivia");

        assertEquals(1, presenter.output.getImportedCount());
        assertEquals(1, presenter.output.getSkippedCount());
    }

    @Test
    void sameNameOnADifferentDateIsNotADuplicate() {
        gateway.reviews.add(googleReview("Sam", 4, 1));
        gateway.reviews.add(googleReview("Sam", 2, 9));

        importAs("olivia");

        assertEquals(2, presenter.output.getImportedCount());
    }

    @Test
    void skipsReviewsWithInvalidData() {
        gateway.reviews.add(googleReview("Sam", 0, 1));
        gateway.reviews.add(googleReview("Kai", 6, 2));
        gateway.reviews.add(googleReview(" ", 4, 3));
        gateway.reviews.add(new GoogleMapsReviewData("Lee", 4, null, null));
        gateway.reviews.add(googleReview("Ana", 5, 4));

        importAs("olivia");

        assertEquals(1, presenter.output.getImportedCount());
        assertEquals(4, presenter.output.getSkippedCount());
        assertEquals("Ana", truck.getReviews().get(0).getAuthorName());
    }

    // Who

    @Test
    void failsWhenUserIsNotAnOwner() {
        dataAccess.addTruck(new FoodTruck("Other", new Location(0, 0), List.of(), List.of()),
                "alice");

        importAs("alice");

        assertEquals("You must be an owner with a registered truck.", presenter.error);
        assertNull(gateway.requestedListing);
    }

    @Test
    void failsWhenOwnerHasNoTruck() {
        dataAccess.addUser(new User("noah", UserType.OWNER));

        importAs("noah");

        assertEquals("You must be an owner with a registered truck.", presenter.error);
    }

    @Test
    void failsWhenUserDoesNotExist() {
        importAs("nobody");

        assertEquals("You must be an owner with a registered truck.", presenter.error);
    }

    @Test
    void failsWhenListingIsBlank() {
        interactor.execute(new ImportGoogleReviewsInputData("olivia", "  "));

        assertEquals("Please enter your Google Maps listing.", presenter.error);
        assertNull(gateway.requestedListing);
    }

    // Google Maps errors

    @Test
    void failsWhenListingIsNotFound() {
        gateway.notFound = true;

        importAs("olivia");

        assertEquals("That Google Maps listing could not be found.", presenter.error);
        assertTrue(truck.getReviews().isEmpty());
    }

    @Test
    void importsNothingWhenTheServiceFails() {
        gateway.reviews.add(googleReview("Sam", 4, 1));
        gateway.serviceDown = true;

        importAs("olivia");

        assertEquals("Google Maps is unavailable right now, so no reviews were imported.",
                presenter.error);
        assertTrue(truck.getReviews().isEmpty());
        assertFalse(dataAccess.saves > 0);
    }

    /** The in-memory data access object, counting how often it saves. */
    private static final class CountingDataAccess extends InMemoryReviewDataAccessObject {
        private int saves;

        @Override
        public void save(FoodTruck savedTruck) {
            saves++;
        }
    }

    /** Returns canned reviews, or fails as told. */
    private static final class FakeGateway implements GoogleMapsReviewGateway {
        private final List<GoogleMapsReviewData> reviews = new ArrayList<>();
        private boolean notFound;
        private boolean serviceDown;
        private String requestedListing;

        @Override
        public List<GoogleMapsReviewData> fetchReviews(String listingId)
                throws ListingNotFoundException, ReviewImportException {
            requestedListing = listingId;
            if (notFound) {
                throw new ListingNotFoundException(listingId);
            }
            if (serviceDown) {
                throw new ReviewImportException("rate limited", null);
            }
            return new ArrayList<>(reviews);
        }
    }

    /** Records what the interactor sent to the presenter. */
    private static final class FakePresenter implements ImportGoogleReviewsOutputBoundary {
        private ImportGoogleReviewsOutputData output;
        private String error;

        @Override
        public void prepareSuccessView(ImportGoogleReviewsOutputData outputData) {
            output = outputData;
            error = null;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }
}
