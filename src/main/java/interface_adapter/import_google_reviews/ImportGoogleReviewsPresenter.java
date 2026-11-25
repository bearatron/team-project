package interface_adapter.import_google_reviews;

import use_case.import_google_reviews.ImportGoogleReviewsOutputBoundary;
import use_case.import_google_reviews.ImportGoogleReviewsOutputData;

/**
 * The presenter for the Import Google Maps Reviews use case.
 */
public class ImportGoogleReviewsPresenter implements ImportGoogleReviewsOutputBoundary {
    private final ImportGoogleReviewsViewModel viewModel;

    /**
     * Creates a new ImportGoogleReviewsPresenter.
     *
     * @param viewModel the Import Google Maps Reviews view model
     */
    public ImportGoogleReviewsPresenter(ImportGoogleReviewsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ImportGoogleReviewsOutputData outputData) {
        final ImportGoogleReviewsState state = viewModel.getState();
        String message = "Imported " + outputData.getImportedCount() + " review(s) to "
                + outputData.getTruckName();
        if (outputData.getSkippedCount() > 0) {
            message += " (" + outputData.getSkippedCount() + " skipped)";
        }
        state.setSuccessMessage(message + ".");
        state.setErrorMessage(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final ImportGoogleReviewsState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setSuccessMessage(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
